package com.assetco.search.results;

import java.util.*;

/**
 * A group of assets that require some special treatment, such as highlighting or being placed in a high-value cell, by the UI in search results.
 * 
 * This structure is mutable so that it can be tuned by later parts of the search-results-delivery pipeline.
 */
public class Hotspot {
    private final List<Asset> members = new ArrayList<>();

    /**
     * Add a member to the hotspot.
     */
    public void addMember(Asset asset) {
        members.add(asset);
    }

    /**
     * Get all the members in this hotspot group. The resulting list cannot be modified.
     */
    public List<Asset> getMembers() {
        return Collections.unmodifiableList(members);
    }
}
