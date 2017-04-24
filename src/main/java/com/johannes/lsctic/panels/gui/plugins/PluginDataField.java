package com.johannes.lsctic.panels.gui.plugins;

/**
 * Created by johannes on 23.04.17.
 */
public class PluginDataField {
    private String fieldname;
    private String fieldvalue;
    private boolean telephone;
    private boolean mobile;

    public PluginDataField(String fieldname, String fieldvalue) {
        this.fieldname = fieldname;
        this.fieldvalue = fieldvalue;
        this.telephone = false;
        this.mobile = false;
    }

    public String getFieldname() {
        return fieldname;
    }

    public String getFieldvalue() {
        return fieldvalue;
    }

    public boolean isTelephone() {
        return telephone;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setTelephone(boolean telephone) {
        this.telephone = telephone;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }
}
