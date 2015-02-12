package org.societies.rconomy;

import java.util.UUID;

/**
 *
 */
public interface Economy {

    double getBalance(UUID player);

    boolean has(UUID player, double value);

    Response withdraw(UUID player, double value);

    Response deposit(UUID player, double value);


    String format(double value);

    String format(UUID player);
}
