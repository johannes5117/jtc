package com.johannes.lsctic.panels.gui.plugins.MysqlPlugin;

import com.johannes.lsctic.panels.gui.plugins.AddressBookEntry;
import com.johannes.lsctic.panels.gui.plugins.AddressLoader;
import com.johannes.lsctic.panels.gui.plugins.AddressPlugin;
import com.johannes.lsctic.panels.gui.plugins.DataSource;
import com.johannes.lsctic.panels.gui.settings.SettingsField;

import java.util.ArrayList;

/**
 * Created by johannes on 22.03.2017.
 */
public class MysqlPlugin implements AddressPlugin {
    private MysqlLoader loader;
    private MysqlSettingsField settingsField;
    private String AUTHOR = "Johannes Engler";
    private String AUTHOR_CONTACT = "engler.johannes@posteo.de";
    private String PLUGIN_NAME= "MysqlPlugin";
    private String PLUGIN_TAG = "MySql";

    public MysqlPlugin() {

        DataSource source = new DataSource(PLUGIN_NAME, PLUGIN_TAG);
        loader = new MysqlLoader(source);
        settingsField = new MysqlSettingsField(loader);

    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public AddressLoader getLoader() {
        return loader;
    }

    @Override
    public void setLoader(AddressLoader loader) {
        this.loader = (MysqlLoader) loader;
    }

    @Override
    public SettingsField getSettingsField() {
        return settingsField;
    }

    @Override
    public void setSettingsField(SettingsField settingsField) {
        this.settingsField = (MysqlSettingsField) settingsField;
    }

    @Override
    public String getAuthor() {
        return AUTHOR;
    }

    @Override
    public String getAuthorContact() {
        return AUTHOR_CONTACT;
    }

    @Override
    public ArrayList<AddressBookEntry> getResults(String query, int number) {
        return loader.getResults(query, number);
    }

    @Override
    public void readFields(ArrayList<String[]> datasourceFields) {
        ArrayList<String> datasourceViewNames = new ArrayList<>();
        for (String[] datasourceField : datasourceFields) {
            datasourceViewNames.add(datasourceField[1]);
        }
        loader.getDataSource().setAvailableFields(datasourceViewNames);
        loader.getStorage().setMysqlFields(datasourceFields);
    }

}
