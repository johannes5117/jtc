package com.johannes.lsctic.panels.gui.plugins;

import com.johannes.lsctic.panels.gui.settings.SettingsField;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by johannes on 22.03.2017.
 */
public interface AddressPlugin {

    AddressLoader getLoader();

    SettingsField getSettingsField();

    String getAuthor();

    String getAuthorContact();

    void setLoader(AddressLoader loader);

    void setSettingsField(SettingsField settingsField);

    ArrayList<AddressBookEntry> getResults(String query, int number);

    void readFields(Connection con) throws SQLException;

}
