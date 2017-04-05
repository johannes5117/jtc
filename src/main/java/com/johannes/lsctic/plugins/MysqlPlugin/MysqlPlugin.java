package com.johannes.lsctic.plugins.MysqlPlugin;

import com.johannes.lsctic.plugins.AddressBookEntry;
import com.johannes.lsctic.plugins.AddressPlugin;
import com.johannes.lsctic.settings.SettingsField;

import java.util.ArrayList;

/**
 * Created by johannes on 22.03.2017.
 */
public class MysqlPlugin extends AddressPlugin {
    private MysqlLoader loader;
    private MysqlSettingsField settingsField;


    public MysqlPlugin() {
        super("Johannes Engler","engler.johannes@posteo.de", "MysqlPlugin");
        loader = new MysqlLoader();
        settingsField = new MysqlSettingsField(loader);
        super.setLoader(loader);
        super.setSettingsField(settingsField);

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
