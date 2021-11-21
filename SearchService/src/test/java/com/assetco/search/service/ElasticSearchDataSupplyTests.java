package com.assetco.search.service;

import com.assetco.search.results.*;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;

import java.net.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ElasticSearchDataSupplyTests {


    private int id;
    private ArrayList<Map<String, Object>> resultDocuments;
    private ElasticSearchProxy elasticSearch;
    private ElasticSearchDataSupply dataSupply;

    @BeforeEach
    public void beforeEach() {
        this.id = 0;
        resultDocuments = new ArrayList<>();
        elasticSearch = mock(ElasticSearchProxy.class);
        dataSupply = new ElasticSearchDataSupply(elasticSearch);
    }

    @Test
    public void invokesElasticSearch() throws Throwable{
        var searchString = "any search string";
        var assets = givenResultsForSearch(
                searchString,
                givenSimpleAsset(AssetVendorRelationshipLevel.Basic, "topic01", "topic02"),
                givenSimpleAsset(AssetVendorRelationshipLevel.Gold, "topic01", "topic03"),
                givenSimpleAsset(AssetVendorRelationshipLevel.Partner, "topic02", "topic03"));

        var expectedResults = givenResultsSet(assets);

        var actualResults = whenExecuteSearch(searchString);

        thenResultsAre(expectedResults, actualResults);
    }

    private void thenResultsAre(SearchResults expectedResults, SearchResults actualResults) {
        assertEquals(expectedResults.getFound().size(), actualResults.getFound().size());

        for (var i = 0; i < expectedResults.getFound().size(); ++i) {
            var expectedAsset = expectedResults.getFound().get(i);
            var actualAsset = actualResults.getFound().get(i);

            assetAssetsAreEqual(expectedAsset, actualAsset);
        }
    }

    private void assetAssetsAreEqual(Asset expectedAsset, Asset actualAsset) {
        assertEquals(expectedAsset.getId(), actualAsset.getId());
        assertEquals(expectedAsset.getPreviewURI(), actualAsset.getPreviewURI());
        assertEquals(expectedAsset.getThumbnailURI(), actualAsset.getThumbnailURI());
        assertEquals(expectedAsset.getTitle(), actualAsset.getTitle());
        assertEquals(expectedAsset.getTopics().size(), actualAsset.getTopics().size());

        for (var i = 0; i < expectedAsset.getTopics().size(); ++i)
            assertEquals(expectedAsset.getTopics().get(i).getId(), actualAsset.getTopics().get(i).getId());
    }

    private SearchResults whenExecuteSearch(String searchString) {
        return dataSupply.execute(searchString);
    }

    private SearchResults givenResultsSet(Asset... assets) {
        var expectedResults = new SearchResults();
        for (var asset : assets)
            expectedResults.addFound(asset);
        return expectedResults;
    }

    private Asset[] givenResultsForSearch(String searchString, Asset... assets) throws Exception {
        var document = new ArrayList<>();

        for (var asset : assets)
            document.add(toDocument(asset));

        var queryDocument = "{\"query\":{\"match\":\"searchText\":{\"query\":\""+ searchString+ "\",\"operator\":\"AND\"}}}";

        when(elasticSearch.execute("assets/_search", queryDocument)).thenReturn(new ObjectMapper().writeValueAsString(document));
        return assets;
    }

    private Asset givenSimpleAsset(AssetVendorRelationshipLevel vendorLevel, String... topics) {
        int id = this.id++;
        return givenAsset(
                id,
                "asset #" + id,
                "https://dev.null/preview" + id + ".jpg",
                "https://dev.null/thumb" + id + ".jpg",
                "1234-" + id,
                vendorLevel,
                topics);
    }

    private Asset givenAsset(
            int id,
            String title,
            String previewUrl,
            String thumbnailUrl,
            String vendorId, AssetVendorRelationshipLevel vendorLevel, String... topicIds) {

        var topics = Arrays.asList(Arrays.stream(topicIds).map(topicId -> new AssetTopic(topicId, null)).toArray(AssetTopic[]::new));

        return new Asset(id, title, URI.create(previewUrl), URI.create(thumbnailUrl), null, null, topics, new AssetVendor(vendorId, null, vendorLevel, 0));
    }

    private HashMap<String, Object> toDocument(Asset asset) {
        var document = new HashMap<String, Object>();
        document.put("id", asset.getId());
        document.put("title", asset.getTitle());
        document.put("thumbnailUrl", asset.getThumbnailURI().toString());
        document.put("previewUrl", asset.getPreviewURI().toString());
        document.put("topics", asset.getTopics().stream().map(AssetTopic::getId).toArray(String[]::new));
        HashMap<String, Object> vendor = new HashMap<>();
        vendor.put("id", asset.getVendor().getId());
        vendor.put("level", asset.getVendor().getRelationshipLevel().name());
        document.put("vendor", vendor);
        return document;
    }
}
