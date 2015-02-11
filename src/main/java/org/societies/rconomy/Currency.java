package org.societies.rconomy;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a CurrencyConfiguration
 */
public class Currency {
    private final HashMap<CurrencyItem, Double> currencies = new HashMap<CurrencyItem, Double>();

    public void add(int id, short damage, double value) {
        currencies.put(new CurrencyItem(id, damage), value);
    }

    public double getValue(int id, short damage) {
        return currencies.get(new CurrencyItem(id, damage));
    }

    public Map<CurrencyItem, Double> getCurrencies() {
        return currencies;
    }
}
