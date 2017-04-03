package com.johannes.lsctic.address;

import com.johannes.lsctic.address.loaders.AddressLoader;
import com.johannes.lsctic.settings.DataSourceSettingsField;
import com.johannes.lsctic.settings.SettingsField;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public void setSettingsField (SettingsField settingsField) {
        this.settingsField = settingsField;
    }

    public abstract ArrayList<AddressBookEntry> getResults(String query, int number);

    public void readFields(Statement statement, Connection con) throws SQLException {
        statement.setQueryTimeout(10);
        ArrayList<String> nameOfOrderedView = new ArrayList<>();
        ArrayList<String> nameOfOrderedEntryPoint = new ArrayList<>();

        int i = 0;
        String quField = this.pluginName;
        Logger.getLogger(getClass().getName()).info("TamTamTam: "+quField);
        while (true) {
            PreparedStatement statement2 = con.prepareStatement("select setting from settings where description = ?");
            statement2.setString(1, quField + i);

            Logger.getLogger(getClass().getName()).log(Level.INFO, "{0}{1}", new Object[]{quField, i});

            try (ResultSet fieldRS = statement2.executeQuery()) {
                if (fieldRS.next()) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Gefunden");
                    String field = fieldRS.getString("setting");
                    String[] parameter = field.split(";");
                    nameOfOrderedView.add(parameter[1]);
                    nameOfOrderedEntryPoint.add(parameter[0]);
                    ++i;
                } else {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Break");
                    statement2.close();
                    break;
                }
            } finally {
                statement2.close();
            }
        }
        loader.getDataSource().setAvailableFields(nameOfOrderedView);
    }
}
