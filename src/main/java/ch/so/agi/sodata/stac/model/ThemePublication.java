package ch.so.agi.sodata.stac.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

/**
 *  Eine Themenpubliaktion entspricht einer Zeile in der Datensuche 
 */
@JacksonXmlRootElement(localName = "themePublication")
@JsonInclude(Include.NON_NULL)
public class ThemePublication {
    /**
     *  Eindeutiger Identifier, z.B. ch.so.arp.waldreservate
     */
    private String identifier; 
    /** 
     * INTERLIS-Datenmodell
     */
    private String model;
    /**
     * Zeitpunkt der letzten Publikation der Daten.
     * Den eigentlichen Nachführungsstand kennen wir nicht.
     * Aggregiert aus den den Nachführungsdaten der Items.
     */
    private LocalDate lastPublishingDate;
    /**
     * Zeitpunkt der vorletzten Publikation. Damit man 
     * das "Gültigkeitsintervall" einer publizierten
     * Themenpublikation angeben kann.
     * Aggregiert aus den den Nachführungsdaten der Items.
     */
    private LocalDate secondToLastPublishingDate = LocalDate.of(1848, 9, 12);
    /**
     * Sprechender Titel
     */
    private String title;
    /**
     * Kurze Beschreibung. In SIMI auf 1000 Zeichen limitiert. Vorgabe
     * circa 500 Zeichen.
     */
    @JacksonXmlCData
    private String shortDescription;
    /**
     * Schlüsselwörter
     */
    @JacksonXmlElementWrapper(localName = "keywords")
    @JacksonXmlProperty(localName = "keyword")
    private List<String> keywordsList;

    /**
     * Synonyme
     */
    @JacksonXmlElementWrapper(localName = "synonyms")
    @JacksonXmlProperty(localName = "synonym")
    private List<String> synonymsList;
    /**
     * Datenherr / zuständige Stelle
     */
    private Office owner;
    /**
     * Verantwortliche technische Stelle
     */
    private Office servicer; 
    /**
     * Link zu weiteren Informationen
     */
    private URI furtherInformation;
    /**
     * Lizenzbestimmungen und/oder Nutzungsbestimmungen
     */
    private URI licence;

    /**
     * Angebotene Datenformate
     */
    @JacksonXmlElementWrapper(localName = "fileFormats")
    @JacksonXmlProperty(localName = "fileFormat")
    private List<FileFormat> fileFormats; 
    /**
     * Liste der vorhandenen Daten-Einheiten in 
     * der Themenpublikation. Jede Orthofoto-Kachel
     * entspricht einem Item. Für viele Datenthemen
     * gibt es nur ein Item (=Kanton).
     */
    @JacksonXmlElementWrapper(localName = "items")
    @JacksonXmlProperty(localName = "item")
    private List<Item> items;
    /**
     * Informationen zu den Tabellen, inkl. die Beschreibung
     * der Attribute.
     */
    @JacksonXmlElementWrapper(localName = "tablesInfo")
    @JacksonXmlProperty(localName = "tableInfo")
    private List<TableInfo> tablesInfo; 
    /**
     * Informationen zu den Diensten, in welchen dieses
     * Thema in irgendeiner Form vorkommt.
     */
    @JacksonXmlElementWrapper(localName = "services")
    @JacksonXmlProperty(localName = "service")
    private List<Service> services;
    /** 
     * Boundingbox der Themenpublikation
     */
    private BoundingBox bbox;

    /**
     * Preview-Layer for the whole themepublication. Can be null.
     */
    private Layer wgcPreviewLayer;

    
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public LocalDate getLastPublishingDate() {
        return lastPublishingDate;
    }
    public void setLastPublishingDate(LocalDate lastPublishingDate) {
        this.lastPublishingDate = lastPublishingDate;
    }
    public LocalDate getSecondToLastPublishingDate() {
        return secondToLastPublishingDate;
    }
    public void setSecondToLastPublishingDate(LocalDate secondToLastPublishingDate) {
        this.secondToLastPublishingDate = secondToLastPublishingDate;
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
    public List<String> getSynonymsList() {
        return synonymsList;
    }
    public void setSynonymsList(List<String> synonymsList) {
        this.synonymsList = synonymsList;
    }
    public List<String> getKeywordsList() {
        return keywordsList;
    }
    public void setKeywordsList(List<String> keywordsList) {
        this.keywordsList = keywordsList;
    }
    public Office getOwner() {
        return owner;
    }
    public void setOwner(Office owner) {
        this.owner = owner;
    }
    public Office getServicer() {
        return servicer;
    }
    public void setServicer(Office servicer) {
        this.servicer = servicer;
    }
    public URI getFurtherInformation() {
        return furtherInformation;
    }
    public void setFurtherInformation(URI furtherInformation) {
        this.furtherInformation = furtherInformation;
    }
    public URI getLicence() {
        return licence;
    }
    public void setLicence(URI licence) {
        this.licence = licence;
    }

    public List<FileFormat> getFileFormats() {
        return fileFormats;
    }
    public void setFileFormats(List<FileFormat> fileFormats) {
        this.fileFormats = fileFormats;
    }
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    public List<TableInfo> getTablesInfo() {
        return tablesInfo;
    }
    public void setTablesInfo(List<TableInfo> tablesInfo) {
        this.tablesInfo = tablesInfo;
    }
    public List<Service> getServices() {
        return services;
    }
    public void setServices(List<Service> services) {
        this.services = services;
    }
    public BoundingBox getBbox() {
        return bbox;
    }
    public void setBbox(BoundingBox bbox) {
        this.bbox = bbox;
    }
    public Layer getWgcPreviewLayer() {
        return wgcPreviewLayer;
    }
    public void setWgcPreviewLayer(Layer wgcPreviewLayer) {
        this.wgcPreviewLayer = wgcPreviewLayer;
    }
}
