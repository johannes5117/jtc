package com.johannes.lsctic.address.loaders;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johannes.lsctic.address.AddressBookEntry;
import com.johannes.lsctic.address.DataSource;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author johannes
 */
public class MysqlLoader implements AddressLoader {
    //TODO: DELTE only for Test
    private ArrayList<AddressBookEntry> en = new ArrayList<>();

    private String name = "MysqlPlugin";
    private MysqlLoaderStorage storageTemp;
    private MysqlLoaderStorage storage;
    private DataSource source;

    public MysqlLoader() {
        source = new DataSource("MysqlPlugin");
        ArrayList<String> infos = new ArrayList<>();
        ArrayList<String> test = new ArrayList<>();




        test.add("Wilhelm");
        test.add("Meier");
        test.add("Berlin");

        en.add(new AddressBookEntry(test, "Wilhelm Meier", source));
        infos.add("Johannes");
        infos.add("Engler");
        infos.add("Bad Krozingen");
        en.add(new AddressBookEntry(infos, "Johannes Engler", source));


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

    public DataSource getDataSource() {
        return source;
    }

}
