package com.assetco.hotspots.optimization;

import com.assetco.search.results.*;

/**
 * Make determinations about individual assets.
 */
public interface AssetAssessments {
    /**
     * Determines if an asset is has special eligibility for a deal.
     */
    boolean isAssetDealEligible(Asset asset);
}
