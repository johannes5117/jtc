package com.johannes.lsctic.address;

import com.johannes.lsctic.address.loaders.AddressLoader;
import com.johannes.lsctic.settings.DataSourceSettingsField;
import com.johannes.lsctic.settings.SettingsField;

import java.util.ArrayList;

/**
 * Created by johannes on 22.03.2017.
 */
public abstract class AddressPlugin {
    private AddressLoader loader;
    private DataSourceSettingsField settingsField;
    private String AUTHOR;
    private String AUTHOR_CONTACT;

    public AddressPlugin(String AUTHOR, String AUTHOR_CONTACT) {
        this.loader = loader;
        this.settingsField = settingsField;
        this.AUTHOR = AUTHOR;
        this.AUTHOR_CONTACT = AUTHOR_CONTACT;
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

    public abstract ArrayList<AddressBookEntry> getResults(String query, int number);



}
