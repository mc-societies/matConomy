package org.societies.rconomy.bukkit;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.societies.rconomy.CurrencyItem;
import org.societies.rconomy.Economy;
import org.societies.rconomy.Response;

import java.util.*;

public class BukkitItemEconomy implements Economy {

    private final Server server;
    private final org.societies.rconomy.Currency currency;
    private ArrayList<Map.Entry<CurrencyItem, Double>> sortedCurrencies;

    public BukkitItemEconomy(Server server, org.societies.rconomy.Currency currency) {
        this.server = server;
        this.currency = currency;

        sortedCurrencies = new ArrayList<Map.Entry<CurrencyItem, Double>>(currency.getCurrencies().entrySet());

        Collections.sort(sortedCurrencies, new Comparator<Map.Entry<CurrencyItem, Double>>() {
            @Override
            public int compare(Map.Entry<CurrencyItem, Double> o1, Map.Entry<CurrencyItem, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
    }

    private double getSingleValue(ItemStack itemStack) {
        if (itemStack == null) {
            return 0;
        }

        return currency.getValue(itemStack.getTypeId(), itemStack.getDurability());
    }

    private double getValue(ItemStack itemStack) {
        if (itemStack == null) {
            return 0;
        }

        return getSingleValue(itemStack) * itemStack.getAmount();
    }

    private List<IndexInfo> indices(ItemStack[] itemStacks) {
        ArrayList<IndexInfo> info = new ArrayList<IndexInfo>(itemStacks.length);

        for (int i = 0, length = itemStacks.length; i < length; i++) {
            ItemStack item = itemStacks[i];

            if (item == null) {
                continue;
            }

            double current = getSingleValue(item);

            if (current == 0) {
                continue;
            }

            info.add(new IndexInfo(i, current, item.getAmount()));
        }

        return info;
    }

    private void removeOne(ItemStack[] items, IndexInfo info) {
        ItemStack item = items[info.index];

        //remove item
        if (item.getAmount() == 1) {
            items[info.index] = null;
        } else {
            item.setAmount(item.getAmount() - 1);
        }
    }

    public double withdrawInventory(ItemStack[] contents, final double value) {
        List<IndexInfo> info = indices(contents);
        Collections.sort(info);


        double left = value;

        for (IndexInfo indexInfo : info) {
            for (int i = 0; i < indexInfo.amount; i++) {

                if (indexInfo.value <= left) {
                    left -= indexInfo.value;

                    removeOne(contents, indexInfo);
                }
            }
        }

        if (left > 0) {
            IndexInfo higher = nextHigher(left, info, contents);

            if (higher == null) {
                throw new RuntimeException("Failed to withdraw money! Check your settings!");
            }

            removeOne(contents, higher);

            return higher.value - left;
        }

        return left;
    }


    public IndexInfo nextHigher(double value, List<IndexInfo> in, ItemStack[] contents) {
        IndexInfo previous = null;

        for (IndexInfo indexInfo : in) {
            if (contents[indexInfo.index] == null) {
                continue;
            }

            if (indexInfo.value < value) {
                return previous;
            }

            previous = indexInfo;
        }

        return previous;
    }

    private void addItem(CurrencyItem currencyItem, Player player) {
        ItemStack item = new ItemStack(currencyItem.getID(), 1, currencyItem.getDamage());

        HashMap<Integer, ItemStack> noFit = player.getInventory().addItem(item);

        if (!noFit.isEmpty()) {
            World world = player.getWorld();
            Location location = player.getLocation();

            for (ItemStack itemStack : noFit.values()) {
                world.dropItemNaturally(location, itemStack);
            }
        }
    }

    private Response deposit(Player player, double value) {
        ArrayList<CurrencyItem> items = new ArrayList<CurrencyItem>();

        double actual = items(value, items);

        for (CurrencyItem item : items) {
            addItem(item, player);
        }

        return new Response(value - actual, true);
    }

    private double items(double value, List<CurrencyItem> items) {
        double previous = Double.NaN;

        double left = value;

        while (left > 0) {
            for (Map.Entry<CurrencyItem, Double> sorted : sortedCurrencies) {
                Double v = sorted.getValue();

                if (left >= v) {
                    CurrencyItem key = sorted.getKey();

                    items.add(key);

                    left -= v;
                }
            }

            //We need to cancel if the coins don't add up
            if (left == previous) {
                break;
            }

            previous = left;
        }

        return left;
    }

    @Override
    public Response withdraw(UUID uuid, final double value) {
        if (!has(uuid, value)) {
            return new Response(false);
        }

        Player player = server.getPlayer(uuid);

        if (player == null) {
            return new Response(false);
        }

        PlayerInventory inventory = player.getInventory();


        ItemStack[] contents = inventory.getContents();
        double left = withdrawInventory(contents, value);
        inventory.setContents(contents);

        Response response = deposit(player, left);

        return new Response(Math.abs(value - left + response.getValue()), true);
    }

    @Override
    public Response deposit(UUID uuid, double value) {
        Player player = server.getPlayer(uuid);

        if (player == null) {
            return new Response(false);
        }

        return deposit(player, value);
    }

    @Override
    public double getBalance(UUID uuid) {
        double value = 0;

        Player player = server.getPlayer(uuid);

        if (player == null) {
            return 0;
        }

        for (ItemStack itemStack : player.getInventory().getContents()) {
            value += getValue(itemStack);
        }

        return value;
    }

    @Override
    public boolean has(UUID player, double value) {
        return getBalance(player) > value;
    }

    @Override
    public String format(double value) {
        ArrayList<CurrencyItem> items = new ArrayList<CurrencyItem>();
        items(value, items);

        StringBuilder format = new StringBuilder();

        for (CurrencyItem item : items) {
            format.append(item.getID());
        }

        return format.toString();
    }

    @Override
    public String format(UUID uuid) {
        Player player = server.getPlayer(uuid);

        if (player == null) {
            return "Nothing";
        }

        StringBuilder format = new StringBuilder();
        ItemStack[] itemStacks = player.getInventory().getContents();

        for (ItemStack item : itemStacks) {
            if (item == null) {
                continue;
            }

            double current = getSingleValue(item);

            if (current == 0) {
                continue;
            }

            format.append(item.getTypeId());
        }

        return format.toString();
    }
}
