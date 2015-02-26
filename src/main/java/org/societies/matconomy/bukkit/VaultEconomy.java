package org.societies.matconomy.bukkit;

import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.societies.matconomy.Economy;
import org.societies.matconomy.Response;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static net.milkbowl.vault.economy.EconomyResponse.ResponseType;

/**
 * Represents a VaultEconomy
 */
public class VaultEconomy extends AbstractEconomy {

    private final Economy economy;

    public VaultEconomy(Economy economy) {this.economy = economy;}

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "matConomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return economy.format(v);
    }

    @Override
    public String currencyNamePlural() {
        return "";
    }

    @Override
    public String currencyNameSingular() {
        return "";
    }

    @Override
    public double getBalance(String s) {
        return economy.getBalance(Bukkit.getPlayer(s).getUniqueId());
    }

    @Override
    public boolean has(String s, double v) {
        return economy.has(Bukkit.getPlayer(s).getUniqueId(), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        UUID uuid = Bukkit.getPlayer(s).getUniqueId();
        Response withdraw = economy.withdraw(uuid, v);
        return new EconomyResponse(withdraw.getValue(),
                economy.getBalance(uuid), withdraw.isSuccess() ? ResponseType.SUCCESS : ResponseType.FAILURE,
                "Transaction failed.");
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        UUID uuid = Bukkit.getPlayer(s).getUniqueId();
        Response deposit = economy.deposit(uuid, v);
        return new EconomyResponse(deposit.getValue(),
                economy.getBalance(uuid), deposit.isSuccess() ? ResponseType.SUCCESS : ResponseType.FAILURE,
                "Transaction failed.");
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return withdrawPlayer(s, v);
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return depositPlayer(s, v);
    }

    @Override
    public double getBalance(String s, String s1) {
        return getBalance(s);
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return has(s, v);
    }

    @Override
    public boolean hasAccount(String s) {
        return true;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return true;
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return true;
    }


}
