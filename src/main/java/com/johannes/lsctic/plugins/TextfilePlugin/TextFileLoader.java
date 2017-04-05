/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.plugins.TextfilePlugin;

import com.johannes.lsctic.plugins.AddressBookEntry;
import com.johannes.lsctic.plugins.AddressLoader;
import com.johannes.lsctic.plugins.DataSource;

import java.util.ArrayList;

/**
 *
 * @author johannes
 */
public class TextFileLoader implements AddressLoader {

    private DataSource source;

    public TextFileLoader() {
        this.source = new DataSource("TextFilePlugin");
    }

    @Override
    public ArrayList<AddressBookEntry> getResults(String ein, int n) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saved() {

    }

    @Override
    public void discarded() {

    }

    @Override
    public DataSource getDataSource() {
        return source;
    }

}
