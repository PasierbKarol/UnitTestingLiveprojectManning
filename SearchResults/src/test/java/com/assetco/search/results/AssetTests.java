package com.assetco.search.results;

import org.junit.jupiter.api.*;

import java.math.*;
import java.net.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssetTests {

    @Test
    public void returnsPropertyValues() {
        var id = Any.string();
        var title = Any.string();
        var thumbnailURI = Any.URI();
        var previewURI = Any.URI();
        var last30Days = Any.assetPurchaseInfo();
        var last24Hours = Any.assetPurchaseInfo();
        var topics = new ArrayList<AssetTopic>();
        topics.add(Any.anyTopic());
        topics.add(Any.anyTopic());
        var vendor = Any.vendor();

        var asset = new Asset(id, title, thumbnailURI, previewURI, last30Days, last24Hours, topics, vendor);

        assertEquals(id, asset.getId());
        assertEquals(title, asset.getTitle());
        assertEquals(thumbnailURI, asset.getThumbnailURI());
        assertEquals(previewURI, asset.getPreviewURI());
        assertEquals(last30Days, asset.getPurchaseInfoLast30Days());
        assertEquals(last24Hours, asset.getPurchaseInfoLast24Hours());
        assertEquals(topics, asset.getTopics());
        assertEquals(vendor, asset.getVendor());
    }
}

