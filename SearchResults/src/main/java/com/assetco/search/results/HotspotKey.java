package com.assetco.search.results;

/**
 * The categories of hotspot groups. Each is used for a different purpose.
 */
public enum HotspotKey {
    /**
     * A group of assets that we want to give the highest level of attention. They are placed in a lightbox at the top of the page and given more screen real estate than other assets.
     */
    Showcase,
    /**
     * A group of assets that should get priority assignment to certain high-value cells on the search grid, such as the top-right cell, and the row initially just above the bottom of the page.
     */
    HighValue,
    /**
     * A group of assets that require special treatment wherever they happen to be placed on the results grid.
     */
    Highlight,
    /**
     * A group of assets that should be prioritized into a second lightbox initially just below the bottom of the search results page.
     */
    Fold,
    /**
     * Assets that should be prioritized over other assets in the general sort order.
     */
    TopPicks,
    /**
     * Assets that should be offered as part of a special bundle.
     */
    Deals
}
