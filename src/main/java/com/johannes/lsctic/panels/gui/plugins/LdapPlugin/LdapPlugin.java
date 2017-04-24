package com.johannes.lsctic.panels.gui.plugins.LdapPlugin;

import com.johannes.lsctic.panels.gui.plugins.*;

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
        this.setPluginSettingsField(settingsField);
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public AddressLoader getLoader() {
        return this.loader;
    }

    @Override
    public void setLoader(AddressLoader loader) {
        this.loader = (LdapLoader) loader;
    }

    @Override
    public PluginSettingsField getPluginSettingsField() {
        return settingsField;
    }

    @Override
    public void setPluginSettingsField(PluginSettingsField settingsField) {
        this.settingsField = (LDAPSettingsField) settingsField;
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
        return loader.getResults(query,number);
    }

    @Override
    public ArrayList<PluginDataField> getDataFields() {
        return null;
    }

    @Override
    public void setDataFields(ArrayList<PluginDataField> datasourceFields) {
           loader.getDataSource().setAvailableFields(datasourceFields);
    }

    @Override
    public ArrayList<String> getOptions() {
        return null;
    }

    @Override
    public void setOptions(ArrayList<String> options) {

    }
}
