package com.johannes.lsctic.panels.gui.plugins.MysqlPlugin;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johannes.lsctic.panels.gui.plugins.AddressBookEntry;
import com.johannes.lsctic.panels.gui.plugins.DataSource;
import com.johannes.lsctic.panels.gui.plugins.AddressLoader;

import java.util.ArrayList;


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

    public MysqlLoader(DataSource source) {
        this.source = source;

        ArrayList<String> infos = new ArrayList<>();
        ArrayList<String> test = new ArrayList<>();

        test.add("Wilhelm");
        test.add("Meier");
        test.add("Berlin");
        test.add("0132323");

        en.add(new AddressBookEntry(test, "Wilhelm Meier", source));
        infos.add("Johannes");
        infos.add("Engler");
        infos.add("Bad Krozingen");
        infos.add("O76641234");
        en.add(new AddressBookEntry(infos, "Johannes Minister Prinz und König von Swasiland", source));


        storage = new MysqlLoaderStorage();

        //load the parameters from the userdatabase (sqlite)
        // TODO: Implement Function

        storageTemp = new MysqlLoaderStorage(storage);
    }

    @Override
    public ArrayList<AddressBookEntry> getResults(String query, int number) {
        // TODO: Implement function
        ArrayList<AddressBookEntry> found = new ArrayList<>();
        for(AddressBookEntry entry : en) {
            if(entry.getName().contains(query)) {
                found.add(entry);
            }
        }
        return found;
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

    protected void setTag(String tag) {

    }

}
