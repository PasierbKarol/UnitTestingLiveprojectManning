package com.assetco.search.results;

import java.net.*;
import java.util.*;

// For someone who is new to the media library business, an "asset" is the term we use
// to describe something we can sell. We want to move into video but, right now, most
// of our highest performing assets are photographs. We make the most money off of
// high-resolution photos taken by freelance photjournalists and inserted into news
// feeds around the world.
//
// An asset could be anything - we've even experimented with some interactive Flash content.
// So that's the way we built this data store.
//
// However, for now, you can treat "asset" and "large picture file" as interchangeable terms.

/**
 * Describes a sellable piece of content; e.g., a picture or a video file.
 */
public class Asset {
    private final Object id;
    private final String title;
    private final URI thumbnailURI;
    private final URI previewURI;

    private final AssetPurchaseInfo purchaseInfoLast30Days;
    private final AssetPurchaseInfo purchaseInfoLast24Hours;
    private final List<AssetTopic> topics;
    private final AssetVendor vendor;

    public Asset(
            Object id,
            String title,
            URI thumbnailURI,
            URI previewURI,
            AssetPurchaseInfo purchaseInfoLast30Days,
            AssetPurchaseInfo purchaseInfoLast24Hours,
            List<AssetTopic> topics,
            AssetVendor vendor) {
        this.id = id;
        this.title = title;
        this.thumbnailURI = thumbnailURI;
        this.previewURI = previewURI;
        this.purchaseInfoLast30Days = purchaseInfoLast30Days;
        this.purchaseInfoLast24Hours = purchaseInfoLast24Hours;
        this.topics = topics;
        this.vendor = vendor;
    }

    /**
     * The technical identity of this asset. Effectively, it's rowid in the database.
     */
    public Object getId() {
        return id;
    }

    /**
     * The title of the asset as displayed on the website.
     */
    public String getTitle() {
        return title;
    }

    /**
     * A URI to the thumbnail image. The image should be stored behind a resizer, so you can provide hints with query parameters like. ?s=256x256
     */
    public URI getThumbnailURI() {
        return thumbnailURI;
    }

    /**
     * A URI to the preview image. The image should be stored behind a resizer, so you can provide hints with query parameters like. ?s=256x256
     */
    public URI getPreviewURI() {
        return previewURI;
    }

    /**
     * Statistics about the asset's performance over the previous 30-day period. These are used to reorder assets an optimize sell-through.
     */
    public AssetPurchaseInfo getPurchaseInfoLast30Days() {
        return purchaseInfoLast30Days;
    }

    /**
     * Statistics about the asset's performance over the previous 24-hour period. These are used to reorder assets an optimize sell-through.
     */
    public AssetPurchaseInfo getPurchaseInfoLast24Hours() {
        return purchaseInfoLast24Hours;
    }

    /**
     * The topics associated with this asset. This list cannot be changed.
     */
    public List<AssetTopic> getTopics() {
        return Collections.unmodifiableList(topics);
    }

    /**
     * The vendor or clearinghouse with whom we have licensed this asset.
     */
    public AssetVendor getVendor() {
        return vendor;
    }
}
