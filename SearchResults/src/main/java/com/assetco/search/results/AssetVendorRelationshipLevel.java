package com.assetco.search.results;

/**
 * The kinds of contracts we keep with vendors.
 */
public enum AssetVendorRelationshipLevel {
    /**
     * Basic vendors have provisioned self-serve accounts are selling their images through our platform.
     */
    Basic,
    /**
     * Silver vendors are vendors with a high-volume of sale who have applied for and received silver status. They get additional promotional consideration in exchange for a lower royalty rate.
     */
    Silver,
    /**
     * A gold vendor has been invited to Gold status and accepted. They are considered high-value contributors. They receive better royalty rates and placement in search results.
     */
    Gold,
    /**
     * A strategic partner. These vendors receive the best royalty rates, sometimes even generating losses, but add some intangible benefit that drives up overall revenue.
     */
    Partner,
}
