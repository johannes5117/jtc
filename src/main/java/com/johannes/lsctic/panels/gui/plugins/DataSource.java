/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.plugins;

import java.util.ArrayList;

/**
 *
 * @author johannes
 */
public class DataSource {
    private String source;
    private ArrayList<PluginDataField> availableFields = new ArrayList<>();
    private String tag;

    public DataSource(String source, String tag) {
        this.source = source;
        this.tag = tag;
    }

    public ArrayList<PluginDataField> getAvailableFields() {
        return availableFields;
    }

    public void setAvailableFields(ArrayList<PluginDataField> availableFields) {
        this.availableFields = availableFields;
    }
    
    public String getTag() {return this.tag;}
    
    public void setDataSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
