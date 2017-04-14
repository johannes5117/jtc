package com.johannes.lsctic.panels.gui.plugins.MysqlPlugin;

import com.johannes.lsctic.panels.gui.plugins.AddressBookEntry;
import com.johannes.lsctic.panels.gui.plugins.AddressLoader;
import com.johannes.lsctic.panels.gui.plugins.AddressPlugin;
import com.johannes.lsctic.panels.gui.plugins.DataSource;
import com.johannes.lsctic.panels.gui.settings.SettingsField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public AddressLoader getLoader() {
        return loader;
    }

    @Override
    public SettingsField getSettingsField() {
        return settingsField;
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
    public void setLoader(AddressLoader loader) {
        this.loader = (MysqlLoader) loader;
    }

    @Override
    public void setSettingsField(SettingsField settingsField) {
        this.settingsField = (MysqlSettingsField) settingsField;
    }

    @Override
    public ArrayList<AddressBookEntry> getResults(String query, int number) {
        return loader.getResults(query, number);
    }

    @Override
    public void readFields(Connection con) throws SQLException {
        ArrayList<String> nameOfOrderedView = new ArrayList<>();
        ArrayList<String> nameOfOrderedEntryPoint = new ArrayList<>();
        int i = 0;
        String quField = PLUGIN_NAME;
        while (true) {
            try (PreparedStatement statement = con.prepareStatement("select setting from settings where description = ?")) {
                statement.setString(1, quField + i);
                try (ResultSet fieldRS = statement.executeQuery()) {
                    if (fieldRS.next()) {
                        String field = fieldRS.getString("setting");
                        String[] parameter = field.split(";");
                        nameOfOrderedView.add(parameter[1]);
                        nameOfOrderedEntryPoint.add(parameter[0]);
                        ++i;
                    } else {
                        break;
                    }
                } catch (SQLException e) {
                    throw e;
                }

            }
        }
        loader.getDataSource().setAvailableFields(nameOfOrderedView);

    }

}
