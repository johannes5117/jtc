/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.address.loaders;

import com.johannes.lsctic.address.AddressBookEntry;
import com.johannes.lsctic.address.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johannes
 */
public interface AddressLoader {
    //Fetch results from the Loader
    public abstract ArrayList<AddressBookEntry> getResults(String ein, int n);
    //saves the settings if user clicks on save button also for the plugin
    public abstract void saved();
    //discards the setting if user clicks on discard
    public abstract void discarded();
    //get the DataSource
    public abstract DataSource getDataSource();
}
