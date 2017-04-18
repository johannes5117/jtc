package com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents;

/**
 * Created by johannes on 17.04.17.
 */
public class ReceivedOwnExtensionEvent {
    private String ownExtension;

    public ReceivedOwnExtensionEvent(String ownExtension) {
        this.ownExtension = ownExtension;
    }

    public String getOwnExtension() {
        return ownExtension;
    }
}
