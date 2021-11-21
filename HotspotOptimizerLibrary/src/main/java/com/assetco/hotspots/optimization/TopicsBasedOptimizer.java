package com.assetco.hotspots.optimization;

import com.assetco.search.results.*;

import java.util.*;
import java.util.function.*;

import static com.assetco.search.results.HotspotKey.*;

/**
 * Makes optimization-decisions based on the topics associated with an asset.
 */
class TopicsBasedOptimizer {
    // make assets with hot topics fill showcase
    // returns true if showcase filled, false otherwise
    // outer process uses return value to prevent application of other rules
    public boolean optimize(SearchResults searchResults, AssetTopicsSource hotTopicsSource) {
        int showcased = 0;
        var hotTopics = new ArrayList<AssetTopic>();
        AssetTopic hotTopic = null;

        // just get one iterator so we can keep looping after we've started filling teh showcase
        Iterator<Asset> iterator = searchResults.getFound().iterator();
        var showcaseAssets = new ArrayList<Asset>();

        while (iterator.hasNext()) {
            Asset asset = iterator.next();

            // make sure our hot topics list is up to date
            if (hotTopics.size() == 0)
                hotTopicsSource.getTopics().forEach(hotTopics::add);

            // make sure to at least highlight
            if (getHottestTopicIn(asset, hotTopics) != null)
                searchResults.getHotspot(Highlight).addMember(asset);

            // any topic wins first time
            if (hotTopic == null)
                hotTopic = getHottestTopicIn(asset, hotTopics);

            // get hottest topic for this asset and start processing for showcase/top picks
            AssetTopic assetHotTopic = getHottestTopicIn(asset, hotTopics);
            if (assetHotTopic != null) {
                if (isHotterTopic(assetHotTopic, hotTopic, hotTopics)) {
                    // move showcase assets to top picks & switch to this topic
                    for (var surplusAsset : showcaseAssets)
                        searchResults.getHotspot(TopPicks).addMember(surplusAsset);
                    // done. reset showcase and switch to tpic
                    showcaseAssets.clear();
                    hotTopic = assetHotTopic;
                    showcased = 0;
                }

                // add asset to showcase candidates list
                if (assetHotTopic == hotTopic)
                    showcaseAssets.add(asset);
            } else {
                // try another
                continue;
            }

            // enough to claim
            // TODO - is this right? don't we still need to take care of everything that happens after this?
            // Examples of other things we do in this loop - adding to top picks and highlighting hotspot groups.
            // -johnw
            if (++showcased > 2)
                break;
        }

        // add our showcase assets to the hotspot
        var showcase = searchResults.getHotspot(Showcase);
        boolean result = false;
        for (var asset : showcaseAssets) {
            showcase.addMember(asset);
            result = true;
        }

        // process remaining assets to fill showcase if possible
        // TODO - this really looks wrong to me. While we are filling a claimed showcase, we just ignore all the other requirements, like highlighting?
        // -johnw
        while (iterator.hasNext()) {
            Asset asset = iterator.next();

            if (asset.getTopics().stream().anyMatch(getAssetTopicPredicate(hotTopic)))
            {
                showcase.addMember(asset);
                if (++showcased >= 5)
                    break;
            }
        }

        // per johnw's commend above, make sure that remaining hot topic assets are added
        // remaining go in top picks
        while (iterator.hasNext()) {
            Asset asset = iterator.next();

            if (getHottestTopicIn(asset, hotTopics) != null)
                searchResults.getHotspot(TopPicks).addMember(asset);
        }

        return result;
    }

    /**
     * Checks to see if the topic on the left is higher-priority than the topc on the right.
     */
    private boolean isHotterTopic(AssetTopic left, AssetTopic right, ArrayList<AssetTopic> hotTopics) {
        var canonicalLeft = hotTopics.stream().filter(getAssetTopicPredicate(left)).findFirst().get();
        var canonicalRight = hotTopics.stream().filter(getAssetTopicPredicate(right)).findFirst().get();

        return hotTopics.indexOf(canonicalLeft) < hotTopics.indexOf(canonicalRight);
    }

    /**
     * If the asset has any associated topics that are in the hot topics list, find the highest priority one.
     */
    private AssetTopic getHottestTopicIn(Asset asset, ArrayList<AssetTopic> hotTopics) {
        for (var topic : hotTopics) {
            if (asset.getTopics().stream().anyMatch(getAssetTopicPredicate(topic)))
                return topic;
        }

        return null;
    }

    /**
     * Returns a predicate for asset topics that will return true if they are equivalent to the passed-in topic.
     * 
     * Note: Passing in a null topic will produce a predicate that always returns false.
     */
    private Predicate<AssetTopic> getAssetTopicPredicate(AssetTopic topic) {
        if (topic == null)
            return assetTopic -> false;

        return assetTopic -> topicsEquivalent(topic, assetTopic);
    }

    /**
     * Checks if two topic objects represent the same topic by comparing their IDs.
     */
    private boolean topicsEquivalent(AssetTopic topic, AssetTopic assetTopic) {
        return Objects.equals(topic.getId(), assetTopic.getId());
    }
}
