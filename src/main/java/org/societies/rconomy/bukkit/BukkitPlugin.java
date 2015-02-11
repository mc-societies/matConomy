package org.societies.rconomy.bukkit;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.societies.rconomy.Currency;
import org.societies.rconomy.Economy;

import java.util.Map;

/**
 * Represents a BukkitPlugin
 */
public class BukkitPlugin extends JavaPlugin {

    private Economy economy;

    @Override
    public void onLoad() {
        Currency currency = new Currency();

        FileConfiguration config = getConfig();

        config.options().copyDefaults(true);
        saveConfig();

        ConfigurationSection section = config.getConfigurationSection("currencies");

        if (section == null) {
            return;
        }

        Map<String, Object> rawValues = section.getValues(true);

        for (Map.Entry<String, Object> entry : rawValues.entrySet()) {
            String[] parts = entry.getKey().split(":");

            int id;
            short damage = 0;

            if (parts.length == 1) {
                id = Integer.parseInt(parts[0]);
            } else if (parts.length == 2) {
                id = Integer.parseInt(parts[0]);
                damage = Short.parseShort(parts[1]);
            } else {
                continue;
            }

            currency.add(id, damage, ((Number) entry.getValue()).doubleValue());
        }

        economy = new BukkitItemEconomy(getServer(), currency);
    }

    public Economy getEconomy() {
        return economy;
    }
}
