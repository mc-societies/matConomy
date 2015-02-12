package org.societies.rconomy.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can have money, fucker ;)!");
                return true;
            }

            if (!sender.hasPermission("rconomy.money")) {
                sender.sendMessage("Insufficient permission!");
                return true;
            }

            sender.sendMessage(economy.format(((Player) sender).getUniqueId()));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("rconomy.reload")) {
                sender.sendMessage("Insufficient permission!");
                return true;
            }

            sender.sendMessage("Reloading AK-47! Please wait patiently...");
            reloadConfig();
            onLoad();
            return true;
        }

        sender.sendMessage("Command not found!");

        return true;
    }

    public Economy getEconomy() {
        return economy;
    }
}
