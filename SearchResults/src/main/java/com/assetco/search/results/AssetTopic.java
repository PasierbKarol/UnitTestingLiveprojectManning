package com.assetco.search.results;

/**
 * Represents any point in the curated taxonomy of topics. It could be near the top of a hierarchy, like "Fashion". It could also be something very specific, like "Fashion/Men's Fashion/Trends/Summer/Tennis Attire".
 */
public class AssetTopic {
    private final String id;
    private final String displayName;

    public AssetTopic(String id, String displayName) {

        this.id = id;
        this.displayName = displayName;
    }

    /**
     * The technical identity of this topic. Effectively, it's rowid in the database.
     */
    public String getId() {
        return id;
    }

    /**
     * The display name as should be presented to the user.
     */
    public String getDisplayName() {
        return displayName;
    }
}
