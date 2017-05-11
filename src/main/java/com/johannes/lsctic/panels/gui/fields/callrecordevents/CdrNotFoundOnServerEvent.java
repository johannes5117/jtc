package com.johannes.lsctic.panels.gui.fields.callrecordevents;

/**
 * Created by johannesengler on 12.05.17.
 */
public class CdrNotFoundOnServerEvent {
    private final String searched;

    public CdrNotFoundOnServerEvent(String searched) {
        this.searched = searched;
    }

    public String getSearched() {
        return searched;
    }
}
