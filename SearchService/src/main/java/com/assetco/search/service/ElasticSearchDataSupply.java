package com.assetco.search.service;

import com.assetco.search.results.*;
import com.fasterxml.jackson.databind.*;
import org.apache.juli.logging.*;

import java.net.*;
import java.util.*;

public class ElasticSearchDataSupply {

    private final ElasticSearchProxy elasticSearch;
    private final ObjectMapper mapper = new ObjectMapper();

    public ElasticSearchDataSupply(ElasticSearchProxy elasticSearch) {
        this.elasticSearch = elasticSearch;
    }

    public SearchResults execute(String searchString) {
        var query = "{\"query\":{\"match\":\"searchText\":{\"query\":\""+ searchString+ "\",\"operator\":\"AND\"}}}";
        var document = elasticSearch.execute("assets/_search", query);

        SearchResults searchResults = new SearchResults();
        try {
            var results = mapper.readValue(document, SearchAsset[].class);
            for (var result : results) {
                var topics = Arrays.stream(result.topics).map(topicId -> new AssetTopic(topicId, null)).toArray(AssetTopic[]::new);

                searchResults.addFound(
                        new Asset(
                                result.id,
                                result.title,
                                URI.create(result.thumbnailUrl),
                                URI.create(result.previewUrl),
                                null,
                                null,
                                Arrays.asList(topics),
                                new AssetVendor(
                                        result.vendor.id,
                                        null,
                                        AssetVendorRelationshipLevel.valueOf(result.vendor.level),
                                        0)));
            }
        } catch (Exception ex) {
            LogFactory.getLog(ElasticSearchDataSupply.class).error(ex);
        }

        return searchResults;
    }
}

