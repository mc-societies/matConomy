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
        Double value = currencies.get(new CurrencyItem(id, damage));

        if (value == null) {
            return 0;
        }

        return value;
    }

    public Map<CurrencyItem, Double> getCurrencies() {
        return currencies;
    }
}
