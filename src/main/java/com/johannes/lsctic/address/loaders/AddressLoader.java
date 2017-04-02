/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.address.loaders;

import com.johannes.lsctic.address.AddressBookEntry;
import java.util.ArrayList;

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
      
}
