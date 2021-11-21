package com.assetco.search.results;

import java.util.*;

/**
 * Describes an overall set of search results. This object is mutable.
 */
public class SearchResults {
    private final List<Asset> found = new ArrayList<>();
    private Map<HotspotKey, Hotspot> hotspots = new HashMap<>();

    /**
     * Add another found asset to the results set.
     */
    public void addFound(Asset asset) {
        found.add(asset);
    }

    /**
     * All of the found items. This list cannot be changed.
     */
    public List<Asset> getFound() {
        return found;
    }

    /**
     * Get this results set's hotspot group for a particular key. This will create a Hotspot object if necessary.
     */
    public Hotspot getHotspot(HotspotKey key) {
        if (hotspots.containsKey(key))
            return hotspots.get(key);

        var result = new Hotspot();
        hotspots.put(key, result);

        return result;
    }

    /**
     * Erases all hotspot data so a later step in the search-results delivery process can re-assign them differently.
     */
    public void clearHotspots() {
        hotspots = new HashMap<>();
    }
}
