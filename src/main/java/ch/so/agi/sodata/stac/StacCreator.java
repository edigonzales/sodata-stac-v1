package ch.so.agi.sodata.stac;

import java.util.List;

import ch.so.agi.meta2file.model.ThemePublication;

public interface StacCreator {
    public void create(String collectionFilePath, ThemePublication themePublication);
    
    public void create_catalog(String collectionFilePath, List<String> collections);
}
