package com.johannes.lsctic.address;

import com.johannes.lsctic.address.loaders.MySqlLoader;
import com.johannes.lsctic.settings.MysqlSettingsField;
import com.johannes.lsctic.settings.SettingsField;

import java.util.ArrayList;

/**
 * Created by johannes on 22.03.2017.
 */
public class MysqlPlugin extends AddressPlugin {
    private MySqlLoader loader;
    private MysqlSettingsField settingsField;


    public MysqlPlugin() {
        super("Johannes Engler","engler.johannes@posteo.de");
        loader = new MySqlLoader();
        settingsField = new MysqlSettingsField(loader);

    }

    @Override
    public SettingsField getSettingsField() {
        return settingsField;
    }

    @Override
    public ArrayList<AddressBookEntry> getResults(String query, int number) {
        return loader.getResults(query, number);
    }




}
