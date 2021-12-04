import com.assetco.hotspots.optimization.SearchResultHotspotOptimizer;
import com.assetco.search.results.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BugsTest {

    private SearchResults searchResults;
    private SearchResultHotspotOptimizer optimizer;
    private AssetVendor partnerVendor;

    @BeforeEach
    void setup() {
        optimizer = new SearchResultHotspotOptimizer();
        searchResults = new SearchResults();
        partnerVendor = makeVendor(AssetVendorRelationshipLevel.Partner);
    }

    @Test
    public void precedingPartnerWithLongTrailingAssetsDoesNotWin() {
        Asset missing = givenAssetInResultsWithVendor(partnerVendor);
        givenAssetInResultsWithVendor(makeVendor(AssetVendorRelationshipLevel.Partner));

        ArrayList<Asset> expected = new ArrayList<>();
        expected.add(missing);
        expected.add(givenAssetInResultsWithVendor(partnerVendor));
        expected.add(givenAssetInResultsWithVendor(partnerVendor));
        expected.add(givenAssetInResultsWithVendor(partnerVendor));
        expected.add(givenAssetInResultsWithVendor(partnerVendor));

        whenOptimise();
        thenHotspotDoesNotHave(HotspotKey.Showcase, missing);
        thenHotspotHasExactly(HotspotKey.Showcase, expected);
    }

    private AssetVendor makeVendor(AssetVendorRelationshipLevel level) {
        return Any.vendorWithLevel(level);
    }

    private Asset givenAssetInResultsWithVendor(AssetVendor assetVendor) {
        Asset asset = Any.assetWithVendorAndId(assetVendor, UUID.randomUUID().toString());
        searchResults.addFound(asset);
        return asset;
    }

    private void whenOptimise() {
        optimizer.optimize(searchResults);
    }

    private void thenHotspotDoesNotHave(HotspotKey key, Asset... forbidden) {
        for (var asset : forbidden)
            assertFalse(searchResults.getHotspot(key).getMembers().contains(asset));
    }

    private void thenHotspotHasExactly(HotspotKey hotspotKey, List<Asset> expected) {
        Assertions.assertArrayEquals(expected.toArray(), searchResults.getHotspot(hotspotKey).getMembers().toArray());
    }
}
