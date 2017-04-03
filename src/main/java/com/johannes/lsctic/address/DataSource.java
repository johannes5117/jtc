/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.address;

import java.util.ArrayList;

/**
 *
 * @author johannes
 */
public class DataSource {
    private String source;
    private ArrayList<String> availableFields = new ArrayList<>();

    public DataSource(String source) {
        this.source = source;
    }

    public ArrayList<String> getAvailableFields() {
        return availableFields;
    }

    public void setAvailableFields(ArrayList<String> availableFields) {
        this.availableFields = availableFields;
    }
    
    public void setDataSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
