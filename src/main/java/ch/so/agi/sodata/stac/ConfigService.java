package ch.so.agi.sodata.stac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ch.so.agi.meta2file.model.BoundingBox;
import ch.so.agi.meta2file.model.Item;
import ch.so.agi.meta2file.model.ThemePublication;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ConfigService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private WKTReader wktReader = new WKTReader();
    private GeoJsonWriter geoJsonWriter = new GeoJsonWriter();

    private static final String PYTHON = "python";
    private static final String VENV_EXECUTABLE = ConfigService.class.getClassLoader().getResource(Paths.get("venv", "bin", "graalpy").toString()).getPath();
    private static final String SOURCE_FILE_NAME = "staccreator.py";

    @org.springframework.beans.factory.annotation.Value("${app.configFile}")
    private String CONFIG_FILE;   

    @org.springframework.beans.factory.annotation.Value("${app.rootHref}")
    private String ROOT_HREF; 
    
    @org.springframework.beans.factory.annotation.Value("${app.filesServerUrl}")
    private String FILES_SERVER_URL;   

    @Autowired
    private Context context;
    
    private StacCreator stacCreator;
    
    @PostConstruct
    public void init() {
        InputStreamReader code = new InputStreamReader(ConfigService.class.getClassLoader().getResourceAsStream(SOURCE_FILE_NAME));

        Source source;
        try {
            source = Source.newBuilder(PYTHON, code, SOURCE_FILE_NAME).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        context.eval(source);
        
        Value pystacCreatorClass = context.getPolyglotBindings().getMember("StacCreator");
        Value pystacCreator = pystacCreatorClass.newInstance();
        
        stacCreator = pystacCreator.as(StacCreator.class);
    }
    
    public void readXml() throws XMLStreamException, IOException, ParseException {
        var xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        var xif = XMLInputFactory.newInstance();
        var xr = xif.createXMLStreamReader(new FileInputStream(new File(CONFIG_FILE)));

        var collections = new ArrayList<String>();
        while (xr.hasNext()) {
            xr.next();
            if (xr.getEventType() == XMLStreamConstants.START_ELEMENT) {
                if ("themePublication".equals(xr.getLocalName())) {
                    var themePublication = xmlMapper.readValue(xr, ThemePublication.class);                    
                    log.debug("Identifier: "+ themePublication.getIdentifier());
                    
                    // Verwenden wir später, um aus sämtlichen Collections einen Catalog zu machen.
                    collections.add(themePublication.getIdentifier());
                    
                    // Sowohl die BBOX des Themas (der Collection) wie auch der Items
                    // müssen nach WGS84 transformiert werden. Bei den Items muss zusätzlich
                    // ebenfalls die Geometrie (der Footprint) transformiert werden.
                    BoundingBox bboxWGS = convertBboxToWGS(themePublication.getBbox());
                    themePublication.setBbox(bboxWGS);
                    
                    var itemsList = new ArrayList<Item>();
                    for (Item item : themePublication.getItems()) {
                        BoundingBox itemBboxWGS = convertBboxToWGS(item.getBbox());
                        item.setBbox(itemBboxWGS);
                        
                        var geom = wktReader.read(item.getGeometry());
                        var geomWGS = convertGeometryToWGS(geom);
                        geoJsonWriter.setEncodeCRS(false);
                        var geomGeoJson = geoJsonWriter.write(geomWGS);
                        item.setGeometry(geomGeoJson);
                        
                        itemsList.add(item);
                    }
                    themePublication.setItems(itemsList);
                    
                    stacCreator.create("/Users/stefan/tmp/staccreator/", themePublication, FILES_SERVER_URL);
                }
            }
        }
        
        // Weil es kein Request gibt, funktioniert 'ServletUriComponentsBuilder'... nicht.
        // Die Anwendung weiss so nichts von einem möglichen Reverse Proxy / API-Gateway etc.
        // Root_href ist somit Teil der Konfiguration. 
        stacCreator.create_catalog("/Users/stefan/tmp/staccreator/", collections, ROOT_HREF);
        
        context.close();
    }
    
    // https://github.com/edigonzales-archiv/geokettle_freeframe_plugin/blob/master/src/main/java/org/catais/plugin/freeframe/FreeFrameTransformator.java
    private Geometry convertGeometryToWGS(Geometry sourceGeometry) {
        Geometry targetGeometry = null;

        if (sourceGeometry instanceof MultiPolygon) {
            int num = sourceGeometry.getNumGeometries();
            Polygon[] polys = new Polygon[num];
            for(int j=0; j<num; j++) {
                polys[j] = transformPolygon((Polygon) sourceGeometry.getGeometryN(j));
            }    
            targetGeometry = (Geometry) new GeometryFactory().createMultiPolygon(polys);
            
        } else if (sourceGeometry instanceof Polygon) {
            targetGeometry = (Geometry) transformPolygon((Polygon) sourceGeometry);
        } else {
            targetGeometry = sourceGeometry;
        }
        return targetGeometry;
    }
    
    private Polygon transformPolygon(Polygon p) {
        LineString shell = (LineString) p.getExteriorRing();
        LineString shellTransformed = transformLineString(shell);
        
        LinearRing[] rings = new LinearRing[p.getNumInteriorRing()];
        int num = p.getNumInteriorRing();
        for(int i=0; i<num; i++) {
            LineString line = transformLineString(p.getInteriorRingN(i));   
            rings[i] = new LinearRing(line.getCoordinateSequence(), new GeometryFactory()); 
        }               
        return new Polygon(new LinearRing(shellTransformed.getCoordinateSequence(), new GeometryFactory()), rings, new GeometryFactory());
    }

    private LineString transformLineString(LineString l) {
        Coordinate[] coords = l.getCoordinates();
        int num = coords.length;

        Coordinate[] coordsTransformed = new Coordinate[num];
        for(int i=0; i<num; i++) {
            coordsTransformed[i] = transformCoordinate(coords[i]);
        }
        CoordinateArraySequence sequence = new CoordinateArraySequence(coordsTransformed);
        return new LineString(sequence, new GeometryFactory());
    }
    
    private Coordinate transformCoordinate(Coordinate coord) {
        double x = ApproxSwissProj.CHtoWGSlat(coord.getX(), coord.getY());
        double y = ApproxSwissProj.CHtoWGSlng(coord.getX(), coord.getY());
        
        return new Coordinate(x, y);
    }

    private BoundingBox convertBboxToWGS(BoundingBox bbox) {
        double bottom = ApproxSwissProj.CHtoWGSlat(bbox.getLeft(), bbox.getBottom());
        double left = ApproxSwissProj.CHtoWGSlng(bbox.getLeft(), bbox.getBottom());
        double top = ApproxSwissProj.CHtoWGSlat(bbox.getRight(), bbox.getTop());
        double right = ApproxSwissProj.CHtoWGSlng(bbox.getRight(), bbox.getTop());
        BoundingBox bboxWGS = new BoundingBox();
        bboxWGS.setBottom(bottom);
        bboxWGS.setLeft(left);
        bboxWGS.setTop(top);
        bboxWGS.setRight(right);

        return bboxWGS;
    }
}
