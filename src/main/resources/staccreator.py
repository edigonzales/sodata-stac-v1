import polyglot
import os
import math
import re
import pystac
from datetime import date, datetime, timezone, timedelta

class StacCreator:
    def create(self, collection_file_path, theme_publication):
        print("Hello from Python.")
        print(collection_file_path)
        print(theme_publication.getIdentifier())

        # Collection
        collection_id = theme_publication.getIdentifier()
        collection_description = theme_publication.getShortDescription()
        collection_licence = theme_publication.getLicence().toString()

        spatial_extent = pystac.SpatialExtent(bboxes=[[theme_publication.getBbox().getLeft(), 
                                    theme_publication.getBbox().getBottom(),
                                    theme_publication.getBbox().getRight(),
                                    theme_publication.getBbox().getTop()]])
        # Umweg via date notwendig, da ein Java-(Local-)Date-Objekt nicht gemappt wird. Es bleibt 'foreign'.
        # replace(tzinfo=...) funktioniert nicht mit date, sondern nur mit datetime.
        collection_interval = sorted([
                                    datetime.fromisoformat(theme_publication.getSecondToLastPublishingDate().toString()).replace(tzinfo=timezone(timedelta(hours=2))), 
                                    datetime.fromisoformat(theme_publication.getLastPublishingDate().toString()).replace(tzinfo=timezone(timedelta(hours=2)))
                                    ]) 
        temporal_extent = pystac.TemporalExtent(intervals=[collection_interval])
        collection_extent = pystac.Extent(spatial=spatial_extent, temporal=temporal_extent)

        collection = pystac.Collection(id=collection_id,
                                    description=collection_description,
                                    extent=collection_extent,
                                    license=collection_licence)

        # Item(s)



        # Save everything to disk
        collection.normalize_and_save(root_href=os.path.join(collection_file_path, collection_id),
                           catalog_type=pystac.CatalogType.SELF_CONTAINED)


        # catalog = pystac.Catalog(id='test-catalog', description='Tutorial catalog.')
        # catalog.add_child(child=collection)
        # catalog.normalize_and_save(root_href=os.path.join(collection_file_path),catalog_type=pystac.CatalogType.SELF_CONTAINED);

    def create_catalog(self, collection_file_path, collections):
        print("Hello from create_catalog")

        catalog = pystac.Catalog(id='test-catalog', description='Tutorial catalog.')

        for collection_id in collections:
            print(collection_id)
            collection = pystac.Collection.from_file(os.path.join(collection_file_path,collection_id, "collection.json"))
            catalog.add_child(child=collection)
            catalog.normalize_and_save(root_href=os.path.join(collection_file_path),catalog_type=pystac.CatalogType.SELF_CONTAINED);            


polyglot.export_value("StacCreator", StacCreator)

