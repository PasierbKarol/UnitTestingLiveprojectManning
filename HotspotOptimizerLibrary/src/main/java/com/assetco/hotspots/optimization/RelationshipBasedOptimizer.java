package com.assetco.hotspots.optimization;

import com.assetco.search.results.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

import static com.assetco.search.results.AssetVendorRelationshipLevel.*;
import static com.assetco.search.results.HotspotKey.*;

// This code manages filling the showcase if it's not already set
// it make sure the first partner-lvl vendor with enough assets on the page gets the showcase
//
// From Jamie's reqs:
//   1. If a Partner-level vendor has at least three (3) assets in the result set, that partner's assets shall own the showcase
//   2. If two (2) Partner-level vendors meet the criteria to own the showcase, the first vendor to meet the criteria shall own the showcase
//   3. If a Partner-level has more than five (5) showcase assets, additional assets shall be treated as Top Picks
//
// -johnw
// 1/3/07

/**
 * Assigns assets to the showcase hotspot group based on their vendor status.
 */
class RelationshipBasedOptimizer {
    public void optimize(SearchResults searchResults) {
        Iterator<Asset> iterator = searchResults.getFound().iterator();
        // don't affect a showcase built by an earlier rule
        var showcaseFull = searchResults.getHotspot(Showcase).getMembers().size() > 0;
        var showcaseAssets = new ArrayList<Asset>();
        var partnerAssets = new HashMap<String, ArrayList<Asset>>();
        var goldAssets = new ArrayList<Asset>();
        var silverAssets = new ArrayList<Asset>();

        while (iterator.hasNext()) {
            Asset asset = iterator.next();
            // HACK! trap gold and silver assets for use later
            if (asset.getVendor().getRelationshipLevel() == Gold)
                goldAssets.add(asset);
            else if (asset.getVendor().getRelationshipLevel() == Silver)
                silverAssets.add(asset);

            if (asset.getVendor().getRelationshipLevel() != Partner)
                continue;

            // remember this partner asset
            addOrUpdatePartnerAsset(partnerAssets, asset);

            // too many assets in showcase - put in top picks instead...
            if (showcaseAssets.size() >= 5) {
                if (hasAssetTheSameVendor(showcaseAssets, asset))
                    searchResults.getHotspot(TopPicks).addMember(asset);
            } else {
                // TODO:
                // ***** MUST NOT FORGET! SHOWCASING BUG! CONTRACTUAL! *****
                // I asked Jamie about rule #2 of the requirements and she didn't get back to me. I made by best guess.
                // Just before release, we find out that my guess was wrong. It's the first partner TO REACH the 3-asset
                // minimum for a set of search results.
                // 
                // Right now, if a sequence of partner assets is broken by an asset for another partner, we stop counting
                // the first partner's assets. So it's possible for a partner to lose their showcase or for another, lower
                // priority result to "steal" the showcase from them.
                //
                // We need to be keeping track of a map of lists - one for each partner - and "lock in" the first partner
                // that gets three assets in their list.
                //
                // This is OBVIOUSLY very bad but we can't do anything about it, right now. We have to release the product
                // as is. This needs to be one of the very first things we address after we release.
                //
                // I'm leaving this huge comment here to ensure we don't forget it because...
                //
                // ...WE ABSOLUTELY MUST NOT LET THIS SLIP THROUGH THE CRACKS!!!
                //
                // -jownw
                // 2/9/07
                // ***** MUST NOT FORGET! SHOWCASING BUG! CONTRACTUAL! *****

                // if there are already assets from a different vendor but not enough to hold showcase,
                // clear showcase
                if (showcaseAssets.size() != 0)
                    if (!hasAssetTheSameVendor(showcaseAssets, asset))
                        if (showcaseAssets.size() < 3) {
                            showcaseAssets.clear();
                        }

                // add this asset to an empty showcase or showcase with same vendor in it
                // if there's already another vendor, that vendor should take precedence!
                if (showcaseAssets.size() == 0 || hasAssetTheSameVendor(showcaseAssets, asset)) {
                    String vendorId = asset.getVendor().getId();
                    showcaseAssets.add(asset);
                }
            }
        }

        // [DBV], 4/14/2014: 
        // need added this here even though it's not about this rules
        // frm Jamie,
        // 1. All partner assets should be eligible for high-value slots in the main grid.
        // 2. All partner assets should be eligible to appear in the fold.

        // todo - this does not belong here!!!
        var highValueHotspot = searchResults.getHotspot(HighValue);
        var a = new ArrayList<Asset>();
        for (var asset : partnerAssets.values().stream().flatMap(Collection::stream).collect(Collectors.toList()))
            if (!highValueHotspot.getMembers().contains(asset))
                highValueHotspot.addMember(asset);

        // TODO - this needs to be moved to something that only manages the fold
        for (var asset : partnerAssets.values().stream().flatMap(Collection::stream).collect(Collectors.toList()))
            searchResults.getHotspot(Fold).addMember(asset);

        // only copy showcase assets into hotspot if there are enough for a partner to claim the showcase
        if (!showcaseFull && showcaseAssets.size() >= 3) {
            Hotspot showcaseHotspot = searchResults.getHotspot(Showcase);
            for (Asset asset : showcaseAssets)
                showcaseHotspot.addMember(asset);
        }

        // acw-14339: gold assets should be in high value hotspots if there are no partner assets in search
        for (var asset : goldAssets)
            if (!highValueHotspot.getMembers().contains(asset))
                highValueHotspot.addMember(asset);

        // acw-14341: gold assets should appear in fold box when appropriate
        for (var asset : goldAssets)
            searchResults.getHotspot(Fold).addMember(asset);

        // LOL acw-14511: gold assets should appear in fold box when appropriate
        for (var asset : silverAssets)
            searchResults.getHotspot(Fold).addMember(asset);
    }

    private boolean hasAssetTheSameVendor(ArrayList<Asset> showcaseAssets, Asset asset) {
        var showcaseAssetVendor = showcaseAssets.get(0).getVendor();
        return Objects.equals(showcaseAssetVendor, asset.getVendor());
    }

    private void addOrUpdatePartnerAsset(HashMap<String, ArrayList<Asset>> partners, Asset asset) {
        String vendorKey = asset.getVendor().getId();
        if (partners.containsKey(vendorKey)) {
            partners.get(vendorKey).add(asset);
        } else {
            var assetsList = new ArrayList<Asset>();
            assetsList.add(asset);
            partners.put(vendorKey, assetsList);
        }
    }
}
