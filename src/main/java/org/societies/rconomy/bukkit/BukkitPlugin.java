package org.societies.rconomy.bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.societies.rconomy.Currency;
import org.societies.rconomy.Economy;

import java.util.List;
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

        List<Map<?, ?>> rawValues = config.getMapList("currencies");

        if (rawValues == null) {
            return;
        }

        for (Map<?, ?> map : rawValues) {
            int id;
            short damage = 0;
            double value;

            Object rawDamage = map.get("damage");

            if (rawDamage != null) {
                damage = ((Number) rawDamage).shortValue();
            }

            id = ((Number) map.get("id")).intValue();
            value = ((Number) map.get("value")).doubleValue();

            currency.add(id, damage,value);
        }

        economy = new BukkitItemEconomy(getServer(), currency);


        ServicesManager services = getServer().getServicesManager();

        getLogger().info("[Economy] Enabling Vault support...");
        services.register(net.milkbowl.vault.economy.Economy.class, new VaultEconomy(economy), this, ServicePriority.Lowest);
    }

    public Economy getEconomy() {
        return economy;
    }
}
