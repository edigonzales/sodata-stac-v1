package ch.so.agi.sodata.stac.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.UUID;

@JsonInclude(Include.NON_NULL)
public class FileFormat {

    /**
     * Sprechender ("schöner") Name
     */
    private String name;
    /**
     * Technischer Name des Formats.
     */
    private String mimetype;
    /**
     * Abkürzung des Formats.
     */
    private String abbreviation;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMimetype() {
        return mimetype;
    }
    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }
    public String getAbbreviation() {
        return abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
