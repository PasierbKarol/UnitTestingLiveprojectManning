package com.assetco.search.results;

/**
 * Used to describe sales statistics over a period of time.
 */
public class AssetPurchaseInfo {
    private final long timesShown;
    private final long timesPurchased;
    private final Money totalRevenue;
    private final Money totalRoyaltiesOwed;

    public AssetPurchaseInfo(
            long timesShown,
            long timesPurchased,
            Money totalRevenue,
            Money totalRoyaltiesOwed) {
        this.timesShown = timesShown;
        this.timesPurchased = timesPurchased;
        this.totalRevenue = totalRevenue;
        this.totalRoyaltiesOwed = totalRoyaltiesOwed;
    }

    /**
     * How many times a customer has seen this asset in the period.
     */
    public long getTimesShown() {
        return timesShown;
    }

    /**
     * How many times this asset has been purchased in the period.
     */
    public long getTimesPurchased() {
        return timesPurchased;
    }

    /**
     * The total amount of revenue collected in sales of this asset over the period.
     */
    public Money getTotalRevenue() {
        return totalRevenue;
    }

    /**
     * The total royalties paid for this asset in the period.
     */
    public Money getTotalRoyaltiesOwed() {
        return totalRoyaltiesOwed;
    }
}
