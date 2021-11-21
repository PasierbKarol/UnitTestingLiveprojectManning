package com.assetco.hotspots.optimization;

import com.assetco.search.results.*;

import java.util.*;

// master optimization process
/**
 * The high-level process for optimizing search results. Ensures all optimizers are used in the correct order.
 */
public class SearchResultHotspotOptimizer {
    private final TopicsBasedOptimizer hotTopicsBasedOptimizer = new TopicsBasedOptimizer();
    private final RelationshipBasedOptimizer relationshipBasedOptimizer = new RelationshipBasedOptimizer();
    private final SalesInfoBasedOptimizer salesInfoBasedOptimizer = new SalesInfoBasedOptimizer();
    private final DealsOptimizer dealsOptimizer = new DealsOptimizer();
    private AssetTopicsSource hotTopics = ArrayList::new;
    private AssetAssessments assessments = asset -> true;

    public void optimize(SearchResults results) {
        results.clearHotspots();
        // per ACW-18114: hot topics take precedence in the showcase even over relationships
        hotTopicsBasedOptimizer.optimize(results, hotTopics);
        salesInfoBasedOptimizer.optimize(results);
        relationshipBasedOptimizer.optimize(results);
        dealsOptimizer.optimize(results, assessments);
    }

    public void setHotTopics(AssetTopicsSource hotTopics) {
        this.hotTopics = hotTopics;
    }
    public void setAssessments(AssetAssessments assessments) { this.assessments = assessments; }
}
