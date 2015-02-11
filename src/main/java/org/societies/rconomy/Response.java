package org.societies.rconomy;

/**
 * Represents a Response
 */
public class Response {

    private final double value;
    private final boolean success;

    public Response(double value, boolean success) {
        this.value = value;
        this.success = success;
    }

    public Response(boolean success) {
        this(0, success);
    }

    public double getValue() {
        return value;
    }

    public boolean isSuccess() {
        return success;
    }
}
