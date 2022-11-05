package ch.so.agi.sodata.stac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ch.so.agi.meta2file.model.BoundingBox;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ConfigService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String PYTHON = "python";
    private static final String VENV_EXECUTABLE = ConfigService.class.getClassLoader().getResource(Paths.get("venv", "bin", "graalpy").toString()).getPath();
    private static final String SOURCE_FILE_NAME = "staccreator.py";

    @org.springframework.beans.factory.annotation.Value("${app.configFile}")
    private String CONFIG_FILE;   

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
    
    public void readXml() throws XMLStreamException, IOException {
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
                    var identifier = themePublication.getIdentifier();
                    var items = themePublication.getItems();
                    
                    log.debug("Identifier: "+ themePublication.getIdentifier());
                    
                    collections.add(themePublication.getIdentifier());
                    
                    // Convert LV95-BoundingBox to WGS84-BoundingBox
                    BoundingBox bbox = themePublication.getBbox();
                    double bottom = ApproxSwissProj.CHtoWGSlat(bbox.getLeft(), bbox.getBottom());
                    double left = ApproxSwissProj.CHtoWGSlng(bbox.getLeft(), bbox.getBottom());
                    double top = ApproxSwissProj.CHtoWGSlat(bbox.getRight(), bbox.getTop());
                    double right = ApproxSwissProj.CHtoWGSlng(bbox.getRight(), bbox.getTop());
                    BoundingBox bboxWGS = new BoundingBox();
                    bboxWGS.setBottom(bottom);
                    bboxWGS.setLeft(left);
                    bboxWGS.setTop(top);
                    bboxWGS.setRight(right);
                    themePublication.setBbox(bboxWGS);
                    
                    stacCreator.create("/Users/stefan/tmp/staccreator/", themePublication);


                }
            }
        }
        
        stacCreator.create_catalog("/Users/stefan/tmp/staccreator/", collections);
        
        context.close();
    }
}
