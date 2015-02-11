package org.societies.rconomy;

import java.util.UUID;

/**
 *
 */
public interface Economy {

    double getBalance(UUID uuid);

    boolean has(UUID uuid, double value);

    Response withdraw(UUID uuid, double value);

    Response deposit(UUID uuid, double value);


    String format(double value);

    String plural();

    String singular();
}
