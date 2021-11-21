package com.assetco.search.results;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssetPurchaseInfoTests {

    @Test
    public void storesAndReturnsValues() {
        var timesShown = Any.anyLong();
        long timesPurchased = Any.anyLong();
        var totalRevenue = Any.money();
        var royaltiesOwed = Any.money();

        var info = new AssetPurchaseInfo(timesShown, timesPurchased, totalRevenue, royaltiesOwed);

        assertEquals(timesShown, info.getTimesShown());
        assertEquals(timesPurchased, info.getTimesPurchased());
        assertEquals(totalRevenue, info.getTotalRevenue());
        assertEquals(royaltiesOwed, info.getTotalRoyaltiesOwed());
    }
}
