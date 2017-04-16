package com.johannes.lsctic.panels.gui.plugins;

import com.johannes.lsctic.panels.gui.settings.SettingsField;

import java.util.ArrayList;

/**
 * Created by johannes on 22.03.2017.
 */
public interface AddressPlugin {

    AddressLoader getLoader();

    void setLoader(AddressLoader loader);

    SettingsField getSettingsField();

    void setSettingsField(SettingsField settingsField);

    String getAuthor();

    String getName();

    String getAuthorContact();

    ArrayList<AddressBookEntry> getResults(String query, int number);

    void readFields(ArrayList<String[]> datasourceValues);

}
