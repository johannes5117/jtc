package com.johannes.lsctic.panels.gui.fields.otherevents;

/**
 * Created by johannes on 14.04.2017.
 */
public class StartConnectionEvent {
    private String address;
    private int port;
    private String id;
    private String pw;
    private boolean hash;

    public StartConnectionEvent(String address, int port, String id, String pw, boolean hash) {
        this.address = address;
        this.port = port;
        this.id = id;
        this.pw = pw;
        this.hash = hash;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public boolean isHash() {
        return hash;
    }
}
