package org.societies.rconomy;

/**
* Represents a CurrencyItem
*/
public final class CurrencyItem {
    final int id;
    final short damage;

    CurrencyItem(int id, short damage) {
        this.id = id;
        this.damage = damage;
    }

    CurrencyItem(int id) {
        this(id, ((short) 0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyItem that = (CurrencyItem) o;

        return damage == that.damage && id == that.id;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) damage;
        return result;
    }

    public short getDamage() {
        return damage;
    }

    public int getID() {
        return id;
    }
}
