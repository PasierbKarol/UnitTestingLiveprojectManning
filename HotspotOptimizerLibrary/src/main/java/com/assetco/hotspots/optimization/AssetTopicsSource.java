package com.assetco.hotspots.optimization;

import com.assetco.search.results.*;

/**
 * Acquires a list of topics, such as the current, ordred list of hot topics.
 */
public interface AssetTopicsSource {
    /**
     * Fetch the list of topics encapsulated by this object.
     */
    Iterable<AssetTopic> getTopics();
}
