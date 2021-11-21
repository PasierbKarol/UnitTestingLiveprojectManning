package com.assetco.search.results;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchResultsTests {
    private SearchResults results;

    @BeforeEach
    public void setup() {
        results = new SearchResults();
    }

    @Test
    public void startsEmpty() {
        thenFoundItemCountIs(0);
    }

    @Test
    public void addingAnItem() {
        var count = givenFoundItemCount();

        var asset = whenAddAsset();

        thenFoundItemCountIs(count + 1);
        thenFoundContains(asset);
    }

    @Test
    public void hotspotsStartEmpty() {
        thenHotspotItemCountIs(Any.hotspotKey(), 0);
    }

    @Test
    public void addingAnItemToAHotspot() {
        var key = givenHotspotKey();
        var originalHotspotMemberCount = givenHotspotMemberCount(key);

        var asset = whenAddItemToHotspot(key);

        thenHotspotItemCountIs(key, originalHotspotMemberCount + 1);
        thenHotspotHasMember(key, asset);
    }

    @Test
    public void clearingHotspots() {
        var key = Any.hotspotKey();
        givenItemAddedToHotspot(key);

        whenClearHotspots();

        thenHotspotItemCountIs(key, 0);
    }

    private void whenClearHotspots() {
        results.clearHotspots();
    }

    private Asset givenItemAddedToHotspot(HotspotKey key) {
        return addItemToHotspot(key);
    }

    private Asset whenAddItemToHotspot(HotspotKey key) {
        return addItemToHotspot(key);
    }

    private Asset addItemToHotspot(HotspotKey key) {
        var asset = Any.asset();
        results.getHotspot(key).addMember(asset);
        return asset;
    }

    private int givenHotspotMemberCount(HotspotKey key) {
        return results.getHotspot(key).getMembers().size();
    }

    private void thenHotspotHasMember(HotspotKey key, Asset asset) {
        assertTrue(results.getHotspot(key).getMembers().contains(asset));
    }

    private void thenHotspotItemCountIs(HotspotKey key, int expected) {
        assertEquals(expected, results.getHotspot(key).getMembers().size());
    }

    private HotspotKey givenHotspotKey() {
        return Any.hotspotKey();
    }

    private void thenFoundContains(Asset asset) {
        assertTrue(results.getFound().contains(asset));
    }

    private Asset whenAddAsset() {
        Asset asset = Any.asset();
        results.addFound(asset);

        return asset;
    }

    private int givenFoundItemCount() {
        return results.getFound().size();
    }

    private void thenFoundItemCountIs(int expected) {
        assertEquals(expected, results.getFound().size());
    }
}
