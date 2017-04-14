package com.johannes.lsctic.panels.gui.plugins.LdapPlugin;

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
public class LdapPlugin implements AddressPlugin {
    private LdapLoader loader;
    private LDAPSettingsField settingsField;
    private String AUTHOR = "Johannes Engler";
    private String AUTHOR_CONTACT = "engler.johannes@posteo.de";
    private String PLUGIN_NAME= "LdapPlugin";
    private String PLUGIN_TAG = "LDAP";

    public LdapPlugin() {
        DataSource source = new DataSource(PLUGIN_NAME, PLUGIN_TAG);
        loader = new LdapLoader(source);
        settingsField = new LDAPSettingsField(loader);
        this.setLoader(loader);
        this.setSettingsField(settingsField);
    }

    @Override
    public AddressLoader getLoader() {
        return this.loader;
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
        this.loader = (LdapLoader) loader;
    }

    @Override
    public void setSettingsField(SettingsField settingsField) {
        this.settingsField = (LDAPSettingsField) settingsField;
    }

    @Override
    public ArrayList<AddressBookEntry> getResults(String query, int number) {
        return loader.getResults(query,number);
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
