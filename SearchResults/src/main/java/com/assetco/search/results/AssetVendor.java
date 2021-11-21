package com.assetco.search.results;

/**
 * Represents an individual or clearinghouse that licenses content to us and describes the nature of our relationship with them.
 */
public class AssetVendor {
    private final String id;
    private final String displayName;
    private final AssetVendorRelationshipLevel relationshipLevel;
    private final float royaltyRate;

    public AssetVendor(
            String id,
            String displayName,
            AssetVendorRelationshipLevel relationshipLevel,
            float royaltyRate) {
        this.id = id;
        this.displayName = displayName;
        this.relationshipLevel = relationshipLevel;
        this.royaltyRate = royaltyRate;
    }

    /**
     * The unique ID for the vendor.
     */
    public String getId() {
        return id;
    }

    /**
     * The vendor's display name as should be shown to a customer.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * The vendor's relationship level, which imposes contractual obligations on how their assets are handled.
     */
    public AssetVendorRelationshipLevel getRelationshipLevel() {
        return relationshipLevel;
    }

    /**
     * This vendor's negotiated royalty rate. If no negotiations took place, this returns the default for their contract level.
     */
    public float getRoyaltyRate() {
        return royaltyRate;
    }
}
