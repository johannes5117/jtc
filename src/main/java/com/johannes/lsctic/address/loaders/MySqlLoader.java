/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.address.loaders;

import com.johannes.lsctic.OptionsStorage;
import com.johannes.lsctic.address.AddressBookEntry;
import com.johannes.lsctic.address.DataSource;
import java.util.ArrayList;


/**
 *
 * @author johannes
 */
public class MySqlLoader extends AddressLoader {

    ArrayList<AddressBookEntry> en = new ArrayList<>();
    
    public MySqlLoader() {
        ArrayList<String> infos = new ArrayList<>();
        infos.add("Johannes");
        infos.add("Bad Krozingen");
        DataSource s = new DataSource();
        s.setDataSource("mysql");
        en.add(new AddressBookEntry(infos, "Testname",s));
    }
    
    // TODO implement Funciton
    @Override
    public ArrayList<AddressBookEntry> getN(String ein, int n) {
        return en;
    }
    
}
