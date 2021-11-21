package com.assetco.search.results;

import org.junit.jupiter.api.*;

import java.math.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyTests {
    private static final String anyAmount = "123.40";
    private Money money;

    @Test
    public void hardcodedToUSD() {
        givenMoneyWithAmount(anyAmount);

        thenCurrencyIs(money, "USD");
    }

    @Test
    public void returnsAmount(){
        givenMoneyWithAmount(anyAmount);

        thenAmountIs(anyAmount);
    }

    private void givenMoneyWithAmount(String amount) {
        money = new Money(new BigDecimal(amount));
    }

    private void thenCurrencyIs(Money money, String expectedCurrency) {
        assertEquals(Currency.getInstance(expectedCurrency), money.getCurrency());
    }

    private void thenAmountIs(String expectedAmount) {
        assertEquals(new BigDecimal(expectedAmount), money.getAmount());
    }
}
