package com.johannes.lsctic.panels.gui.plugins;

import com.google.common.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by johannes on 22.03.2017.
 */
public interface AddressPlugin {

    AddressLoader getLoader();

    void setLoader(AddressLoader loader);

    PluginSettingsField getPluginSettingsField();

    void setPluginSettingsField(PluginSettingsField settingsField);

    String getAuthor();

    String getName();

    String getAuthorContact();

    ArrayList<AddressBookEntry> getResults(String query, int number);

    ArrayList<PluginDataField> getDataFields();

    void setDataFields(ArrayList<PluginDataField> datasourceValues);

    ArrayList<String> getOptions();

    void setOptions(ArrayList<String> options);

}
