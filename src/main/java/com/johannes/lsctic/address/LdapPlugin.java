package com.johannes.lsctic.address;

import com.johannes.lsctic.address.loaders.AddressLoader;
import com.johannes.lsctic.address.loaders.LdapLoader;
import com.johannes.lsctic.settings.DataSourceSettingsField;
import com.johannes.lsctic.settings.LDAPSettingsField;

/**
 * Created by johannes on 22.03.2017.
 */
public class LdapPlugin extends AdressPlugin {
    private LdapLoader loader;

    public LdapPlugin(String AUTHOR, String AUTHOR_CONTACT) {
        super(AUTHOR, AUTHOR_CONTACT);
        loader = new LdapLoader();
        LDAPSettingsField settingsField = new LDAPSettingsField(loader);
    }
}
