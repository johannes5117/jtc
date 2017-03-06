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
public interface AddressLoaderInterface {
    public ArrayList<AddressBookEntry> getN(String ein, int n);
}
