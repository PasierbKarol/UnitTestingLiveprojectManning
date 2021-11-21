package com.assetco.hotspots.optimization;

import com.assetco.search.results.*;

import java.math.*;

import static com.assetco.search.results.HotspotKey.*;

// [jmj, 2017/dec/24]
// HACK: Please don't judge me. Sam was breathing down my neck to get this done ASAP so she could say we put out
//       the fire and were back on schedule. I was already here late and she said we needed to stay later to finish.
//       It works perfectly, now, but it is ugly. She said we could make it pretty, later.
//       TODO: REWRITE!

/**
 * Assigns assets to hotspots based on contractual obligations derived from a vendor's relationship level.
 */
class DealsOptimizer {
    public void optimize(SearchResults results, AssetAssessments assessments) {
        var highestRelationshipLevelOptional = results.getFound().stream().map(a -> a.getVendor().getRelationshipLevel())
                .max(Enum::compareTo);

        if (highestRelationshipLevelOptional.isPresent()) {
            var highestRelationshipLevel = highestRelationshipLevelOptional.get();

            for (var asset : results.getFound()) {
                switch (highestRelationshipLevel) {
                    case Silver:
                        switch (asset.getVendor().getRelationshipLevel()) {
                            // [jmj, 2017/dev/24] we don't need the other case
//                            case Silver:
//                                if (assessments.isAssetDealEligible(asset) || hasProfitMargin(asset, "1", "2"))
//                                    if (hasProfitMargin(asset, "7", "10"))
//                                        if (hasTopLine(asset, "1000.00"))
//                                            results.getHotspot(Deals).addMember(asset);
//                                break;
                            case Silver:
                                if (assessments.isAssetDealEligible(asset) || hasProfitMargin(asset, "1", "2"))
                                    if (hasProfitMargin(asset, "7", "10") && hasTopLine(asset, "1500"))
                                        results.getHotspot(Deals).addMember(asset);
                                break;
                        }
                    case Gold:
                        switch (asset.getVendor().getRelationshipLevel()) {
                            case Gold:
                                if (assessments.isAssetDealEligible(asset) || hasProfitMargin(asset, "1", "2"))
                                    if (hasProfitMargin(asset, "7", "10"))
                                        if (hasTopLine(asset, "1000.00"))
                                            results.getHotspot(Deals).addMember(asset);
                                break;
                            case Silver:
                                if (assessments.isAssetDealEligible(asset) && hasProfitMargin(asset, "1", "2") && hasTopLine(asset, "1500"))
                                    results.getHotspot(Deals).addMember(asset);
                                break;
                        }
                        break;
                    // [jmj, 2017/dev/24] contractual
                    case Partner:
                        switch (asset.getVendor().getRelationshipLevel()) {
                            case Partner:
                                if (hasProfitMargin(asset, "7", "10"))
                                    results.getHotspot(Deals).addMember(asset);
                                break;
                            case Gold:
                                if (assessments.isAssetDealEligible(asset) && hasProfitMargin(asset, "1", "2"))
                                    if (hasTopLine(asset, "1000"))
                                        results.getHotspot(Deals).addMember(asset);
                                break;
                            case Silver:
                                if (assessments.isAssetDealEligible(asset))
                                    if (hasTopLine(asset, "10000"))
                                        if (hasProfitMargin(asset, "1", "4"))
                                            results.getHotspot(Deals).addMember(asset);
                                break;
                        }
                        break;
                }
            }
        }
    }

    // TODO: more helper methods
    // [jmj, 2017/dev/24] started cleaning up but got too tired...still gotta put together a bike!
    private boolean hasTopLine(Asset asset, String amount) {
        return asset.getPurchaseInfoLast30Days().getTotalRevenue().getAmount().compareTo(new BigDecimal(amount)) >= 0;
    }

    // [jmj, 2017/dev/24] see above
    private boolean hasProfitMargin(Asset asset, String denominator, String numerator) {
        var scaledRevenue = asset.getPurchaseInfoLast30Days().getTotalRevenue().getAmount().multiply(new BigDecimal(denominator));
        var scaledRoyalties = asset.getPurchaseInfoLast30Days().getTotalRoyaltiesOwed().getAmount().multiply(new BigDecimal(numerator));
        return scaledRevenue.compareTo(scaledRoyalties) >= 0;
    }
}
