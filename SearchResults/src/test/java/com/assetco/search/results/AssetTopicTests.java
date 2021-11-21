package com.assetco.search.results;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssetTopicTests {
    @Test
    public void storesAndRetrievesValues() {
        var id = Any.string();
        var displayName = Any.string();

        var topic = new AssetTopic(id, displayName);

        assertEquals(id, topic.getId());
        assertEquals(displayName, topic.getDisplayName());
    }
}
