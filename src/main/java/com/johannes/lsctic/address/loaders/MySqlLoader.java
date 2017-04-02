package com.johannes.lsctic.address.loaders;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johannes.lsctic.address.AddressBookEntry;
import com.johannes.lsctic.address.DataSource;

import java.util.ArrayList;


/**
 * @author johannes
 */
public class MySqlLoader implements AddressLoader {

    ArrayList<AddressBookEntry> en = new ArrayList<>();
    MysqlLoaderStorage storageTemp;
    MysqlLoaderStorage storage;

    public MySqlLoader() {
        ArrayList<String> infos = new ArrayList<>();
        ArrayList<String> test = new ArrayList<>();
        test.add("Wilhelm");
        test.add("Berlin");
        DataSource s = new DataSource();
        s.setDataSource("mysql");
        en.add(new AddressBookEntry(test, "Wilhelm", s));
        infos.add("Johannes");
        infos.add("Bad Krozingen");

        en.add(new AddressBookEntry(infos, "Testname", s));


        storage = new MysqlLoaderStorage();

        //load the parameters from the userdatabase (sqlite)
        // TODO: Implement Function

        storageTemp = new MysqlLoaderStorage(storage);
    }

    @Override
    public ArrayList<AddressBookEntry> getResults(String query, int number) {
        // TODO: Implement function
        return en;
    }

    public void saved() {
        this.storage = this.storageTemp;
    }

    public void discarded() {
        this.storageTemp = this.storage;
    }

    public MysqlLoaderStorage getStorageTemp() {
        return storageTemp;
    }

    public MysqlLoaderStorage getStorage() {
        return storage;
    }
}
