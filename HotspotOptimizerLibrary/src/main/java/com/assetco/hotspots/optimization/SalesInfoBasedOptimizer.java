package com.assetco.hotspots.optimization;

import com.assetco.search.results.*;

import java.math.*;

import static com.assetco.search.results.HotspotKey.HighValue;

// [jmj, 2017/jun/5]
// Again, we have what seems like a pretty serious bug. I've pinged everyone I can think of and nobody's getting back to me.
// In this file, it's possible for an asset to take up multiple spots in the high-value hotspot group. Depending on how the
// UI interprets that information, it could results in an asset consuming multiple high-value pieces of screen real estate.
// It seems pretty obvious that the intent was for hotspots to be a set - and I have no idea why they aren't; maybe order matters? -
// but what if the UI is using a hotspot group as an actual list of assets to work from for something like high-value assets?
// 
// If they're doing that, then, like I said, an asset showing up twice in the list would be a serious problem. If they're just
// going off the main search results, like they should be, and using the hotspot groups to test for whether or not an asset
// should get special treatment, this isn't an issue.
//
// Again, everyone is so busy with whatever they're working on that I cannot get a clear answer from anyone...
// Does this really happen, ultimately? Nobody will tell me. It's a difficult case to find in production, or I'd check myself.
//
// Is it really a problem if it is happening? Absolute radio-silence from product & marketing on this matter.
//
// Given that I don't know if this is a problem or if it should be fixed here, I'm not going to take the risk of fixing it.
// I'll just kick the rock down the road and let it be the next person's problem. If that person's you, sorry. This is where you would fix
// the problem...

/**
 * uses sales data for slotting assets if a slot has not already been filled
 */
class SalesInfoBasedOptimizer {
    public void optimize(SearchResults searchResults) {
        for (var asset : searchResults.getFound()) {
            // bug?
            if (searchResults.getHotspot(HighValue).getMembers().contains(asset))
                break;

            var delta = asset.getPurchaseInfoLast30Days().getTotalRevenue().getAmount()
                    .subtract(asset.getPurchaseInfoLast30Days().getTotalRoyaltiesOwed().getAmount());

            // bug?
            if (asset.getPurchaseInfoLast30Days().getTotalRevenue().getAmount().compareTo(new BigDecimal("5000.00")) >= 0
                    && delta.compareTo(new BigDecimal("4000.00")) >= 0)
                searchResults.getHotspot(HighValue).addMember(asset);
        }


        //acw-11301:
        //for (var asset : searchResults.getFound()) {
        //    if (searchResults.getHotspot(HighValue).getMembers().size() > 0)
        //        continue;
        //
        //    if (asset.getPurchaseInfoLast24Hours().getTimesShown() >= 1000 &&
        //            asset.getPurchaseInfoLast24Hours().getTimesPurchased() * 200 >= asset.getPurchaseInfoLast24Hours().getTimesShown())
        //        searchResults.getHotspot(HighValue).addMember(asset);
        //}
        for (var asset : searchResults.getFound()) {
            if (searchResults.getHotspot(HighValue).getMembers().size() > 0)
                return;

            if (asset.getPurchaseInfoLast24Hours().getTimesShown() >= 1000 &&
                    asset.getPurchaseInfoLast24Hours().getTimesPurchased() * 200 >= asset.getPurchaseInfoLast24Hours().getTimesShown())
                searchResults.getHotspot(HighValue).addMember(asset);
        }

        for (var asset : searchResults.getFound()) {
            if (asset.getPurchaseInfoLast30Days().getTimesShown() >= 50000 &&
                    asset.getPurchaseInfoLast30Days().getTimesPurchased() * 125 >= asset.getPurchaseInfoLast30Days().getTimesShown())
                // [jmj, 2017/jun/5]: This is where you would fix the problem.
                // Make sure the asset isn't already in the hotspot before adding
                searchResults.getHotspot(HighValue).addMember(asset);
        }
    }
}
