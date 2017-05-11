package com.johannes.lsctic.panels.gui.fields.callrecordevents;

/**
 * Created by johannesengler on 12.05.17.
 */
public class SearchCdrInDatabaseEvent {
    private final String number;
    private int amount;
    public SearchCdrInDatabaseEvent(String number, int amount) {
        this.number = number;
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public int getAmount() {
        return amount;
    }
}
