package com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents;

/**
 * Created by johannes on 28.04.17.
 */
public class OrderCDRsEvent {
    private int start;
    private int amount = 10;

    public OrderCDRsEvent(int start) {
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public int getAmount() {
        return amount;
    }
}
