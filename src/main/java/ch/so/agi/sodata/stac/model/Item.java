package ch.so.agi.sodata.stac.model;

import java.time.LocalDate;


/**
 * Ein Item entspricht einer Untereinheit einer Themenpublikation.
 * Jede Themenpublikation hat mindestens eine Untereinheit: der Kanton.
 * Falls die Daten in kleineren Einheiten nachgeführt oder verwaltet 
 * wird (z.B. amtliche Vermessung, Nutzungsplanung oder Orthofotos) 
 * hat die Themenpublikation eine entsprechende Anzahl Items.
 */
public class Item {
    /**
     * Identifier des Items. Z.B. ch.so.agi.amtliche_vermessung.2601
     */
    private String identifier;
    /**
     * Sprechender Name
     */
    private String title;
    /**
     * Zeitpunkt der letzten Publikation des Item.
     * Den eigentlichen Nachführungsstand kennen wir nicht.
     */
    private LocalDate lastPublishingDate;
    /**
     * Zeitpunkt der vorletzten Publikation. Damit man 
     * das "Gültigkeitsintervall" eines Items angeben kann.
     */
    private LocalDate secondToLastPublishingDate = LocalDate.of(1848, 9, 12);
    /**
     * Darf Null sein, wird momentan bei Bedarf aus Geometrie abgeleitet.
     */
    private BoundingBox bbox;
    /**
     * Ausdehnung / Perimeter des Item. WKT-String, EPSG:2056.
     */
    private String geometry;
    
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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
    public BoundingBox getBbox() {
        return bbox;
    }
    public void setBbox(BoundingBox bbox) {
        this.bbox = bbox;
    }
    public String getGeometry() {
        return geometry;
    }
    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }
}
