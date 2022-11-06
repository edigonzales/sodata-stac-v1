package ch.so.agi.sodata.stac;

import java.util.List;

import ch.so.agi.sodata.stac.model.ThemePublication;

public interface StacCreator {
    public void create(String collectionFilePath, ThemePublication themePublication, String filesServerUrl);
    
    public void create_catalog(String collectionFilePath, List<String> collections, String rootUrl);
}
