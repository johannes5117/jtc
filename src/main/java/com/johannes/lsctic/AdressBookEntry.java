/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;

import java.util.ArrayList;

/**
 *
 * @author johannesengler
 */
public class AdressBookEntry {
    private String name;
    private ArrayList<String> data;
    public AdressBookEntry(ArrayList<String> data, String name) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }
    public String get(int i) {
        return data.get(i);
    }
    
    
}
