package com.johannes.lsctic.panels.gui.plugins;

import com.johannes.lsctic.panels.gui.settings.SettingsField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by johannes on 22.03.2017.
 */
public abstract class AddressPlugin {
    private AddressLoader loader;
    private SettingsField settingsField;
    private String AUTHOR;
    private String AUTHOR_CONTACT;
    private String pluginName;

    public AddressPlugin(String AUTHOR, String AUTHOR_CONTACT, String pluginName) {
        this.AUTHOR = AUTHOR;
        this.AUTHOR_CONTACT = AUTHOR_CONTACT;
        this.pluginName = pluginName;
    }

    public AddressLoader getLoader() {
        return loader;
    }

    public abstract SettingsField getSettingsField();

    public String getAuthor() {
        return AUTHOR;
    }

    public String getAuthorContact() {
        return AUTHOR_CONTACT;
    }

    public void setLoader(AddressLoader loader) {
        this.loader = loader;
    }

    public void setSettingsField(SettingsField settingsField) {
        this.settingsField = settingsField;
    }

    public abstract ArrayList<AddressBookEntry> getResults(String query, int number);

    public void readFields(Connection con) throws SQLException {
        ArrayList<String> nameOfOrderedView = new ArrayList<>();
        ArrayList<String> nameOfOrderedEntryPoint = new ArrayList<>();
        int i = 0;
        String quField = this.pluginName;
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
