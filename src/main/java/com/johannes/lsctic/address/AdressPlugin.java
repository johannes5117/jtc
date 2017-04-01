package com.johannes.lsctic.address;

import com.johannes.lsctic.address.loaders.AddressLoader;
import com.johannes.lsctic.settings.DataSourceSettingsField;

/**
 * Created by johannes on 22.03.2017.
 */
public class AdressPlugin {
    private AddressLoader loader;
    private DataSourceSettingsField settingsField;
    private String AUTHOR;
    private String AUTHOR_CONTACT;

    public AdressPlugin(String AUTHOR, String AUTHOR_CONTACT) {
        this.loader = loader;
        this.settingsField = settingsField;
        this.AUTHOR = AUTHOR;
        this.AUTHOR_CONTACT = AUTHOR_CONTACT;
    }

    public AddressLoader getLoader() {
        return loader;
    }

    public DataSourceSettingsField getSettingsField() {
        return settingsField;
    }

    public String getAUTHOR() {
        return AUTHOR;
    }

    public String getAUTHOR_CONTACT() {
        return AUTHOR_CONTACT;
    }
}
