/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.plugins;

import java.util.List;

/**
 *
 * @author johannes
 */
public interface AddressLoader {
    //Fetch results from the Loader
    List<AddressBookEntry> getResults(String ein, int n);
    //saves the settings if user clicks on save button also for the plugin
    void saved();
    //discards the setting if user clicks on discard
    void discarded();
    //get the DataSource
    DataSource getDataSource();


}
