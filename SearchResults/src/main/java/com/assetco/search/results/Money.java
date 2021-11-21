package com.assetco.search.results;

import java.math.*;
import java.util.*;

/**
 * Represents an amount of money, such as the sale price of an asset, or the royalty amount owed on it.
 */
public class Money {
    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * The unit of currency. Currently, USD, but could vary later.
     */
    public Currency getCurrency() {
        return Currency.getInstance(Locale.US);
    }

    /**
     * The total amount of currency represented.
     */
    public BigDecimal getAmount() {
        return amount;
    }
}
