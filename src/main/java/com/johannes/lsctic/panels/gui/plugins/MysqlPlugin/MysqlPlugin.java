package com.johannes.lsctic.panels.gui.plugins.MysqlPlugin;

import com.johannes.lsctic.panels.gui.plugins.AddressBookEntry;
import com.johannes.lsctic.panels.gui.plugins.AddressLoader;
import com.johannes.lsctic.panels.gui.plugins.AddressPlugin;
import com.johannes.lsctic.panels.gui.plugins.DataSource;
import com.johannes.lsctic.panels.gui.settings.SettingsField;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by johannes on 22.03.2017.
 */
public class MysqlPlugin extends AddressPlugin {
    private MysqlLoader loader;
    private MysqlSettingsField settingsField;


    public MysqlPlugin() {
        super("Johannes Engler","engler.johannes@posteo.de", "MysqlPlugin");

        DataSource source = new DataSource("MysqlPlugin", "MySql");
        loader = new MysqlLoader(source);

        settingsField = new MysqlSettingsField(loader);

    }

    @Override
    public AddressLoader getLoader() {
        return null;
    }

    @Override
    public SettingsField getSettingsField() {
        return settingsField;
    }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public String getAuthorContact() {
        return null;
    }

    @Override
    public void setLoader(AddressLoader loader) {

    }

    @Override
    public void setSettingsField(SettingsField settingsField) {

    }

    @Override
    public ArrayList<AddressBookEntry> getResults(String query, int number) {
        return loader.getResults(query, number);
    }

    @Override
    public void readFields(Connection con) throws SQLException {

    }

}
