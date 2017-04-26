package com.johannes.lsctic;

/**
 * Created by johannes on 27.04.17.
 */
public class OptionTuple {
    private boolean activated;
    private String name;

    public OptionTuple(boolean activated, String name) {
        this.activated = activated;
        this.name = name;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getName() {
        return name;
    }
}
