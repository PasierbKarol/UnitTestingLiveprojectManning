import com.assetco.hotspots.optimization.SearchResultHotspotOptimizer;
import com.assetco.search.results.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BugsTest {

    protected SearchResults searchResults;
    protected SearchResultHotspotOptimizer sut;

    @BeforeEach
    void setup() {
        searchResults = new SearchResults();
        sut = new SearchResultHotspotOptimizer();
    }

    @Test
    public void precedingPartnerWithLongTrailingAssetsDoesNotWin() {
        AssetVendor bigShotsPartner = makeVendor(AssetVendorRelationshipLevel.Partner);
        AssetVendor celebPartner = makeVendor(AssetVendorRelationshipLevel.Partner);
        Asset missingBigShots = givenAssetInResultsWithVendor(bigShotsPartner);
        Asset celeb = givenAssetInResultsWithVendor(celebPartner);

        ArrayList<Asset> expected = new ArrayList<>();
       expected.add(celeb);
       expected.add(givenAssetInResultsWithVendor(bigShotsPartner));
       expected.add(givenAssetInResultsWithVendor(bigShotsPartner));
       expected.add(givenAssetInResultsWithVendor(bigShotsPartner));

        whenOptimise();
        thenHotspotDoesNotHave(HotspotKey.Showcase, missingBigShots);
        thenHotspotHasExactly(HotspotKey.Showcase, expected);
    }

    private AssetVendor makeVendor(AssetVendorRelationshipLevel level) {
        return Any.vendor();
    }

    private Asset givenAssetInResultsWithVendor(AssetVendor assetVendor) {
        Asset asset = Any.assetWithVendorAndId(assetVendor, UUID.randomUUID().toString());
        searchResults.addFound(asset);
        return asset;
    }

    private void whenOptimise() {
        sut.optimize(searchResults);
    }

    private void thenHotspotDoesNotHave(HotspotKey key, Asset asset) {
        var members = searchResults.getHotspot(key).getMembers();
        for (Asset member : members) {
            assertNotEquals(member, asset);
        }
    }

    private void thenHotspotHasExactly(HotspotKey key, List<Asset> expected) {
        var members = searchResults.getHotspot(key).getMembers().toArray();
        assertArrayEquals(members, expected.toArray());
    }
}
