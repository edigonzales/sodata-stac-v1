package ch.so.agi.sodata.stac.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

/**
 * Informationen zu einer einzelnen Tabelle
 */
@JsonInclude(Include.NON_NULL)
public class TableInfo {
    /**
     * Name der Tabelle in der PostgreSQL-Datenbank
     */
    private String sqlName;
    /**
     * Sprechender ("schöner") Name der Tabelle
     */
    private String title;

    /**
     * Kurze thematische Beschreibung. 
     * 
     * Der Text wird in einem CDATA-Abschnitt serialisiert, darf aber 
     * nur valide XHTML-Tags enthalten. 
     */
    @JacksonXmlCData
    private String shortDescription;
    /**
     * Liste sämtlicher Attribut-Infos
     */
    @JacksonXmlElementWrapper(localName = "attributesInfo")
    @JacksonXmlProperty(localName = "attributeInfo")
    private List<AttributeInfo> attributesInfo;
    
    public String getSqlName() {
        return sqlName;
    }
    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getShortDescription() {
        return shortDescription;
    }
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    public List<AttributeInfo> getAttributesInfo() {
        return attributesInfo;
    }
    public void setAttributesInfo(List<AttributeInfo> attributesInfo) {
        this.attributesInfo = attributesInfo;
    }
}
