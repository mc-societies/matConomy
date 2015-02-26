package org.societies.matconomy.bukkit;

/**
* Represents a IndexInfo
*/
final class IndexInfo implements Comparable<IndexInfo> {
    final int index;
    final double value;
    final int amount;

    IndexInfo(int index, double value, int amount) {
        this.index = index;
        this.value = value;
        this.amount = amount;
    }

    @Override
    public int compareTo(IndexInfo o) {
        return Double.compare(o.value, value);
    }
}
