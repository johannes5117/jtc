package com.johannes.lsctic.address;

import com.johannes.lsctic.address.loaders.LdapLoader;
import com.johannes.lsctic.settings.LDAPSettingsField;
import com.johannes.lsctic.settings.SettingsField;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by johannes on 22.03.2017.
 */
public class LdapPlugin extends AddressPlugin {
    private LdapLoader loader;
    private LDAPSettingsField settingsField;

    public LdapPlugin(String AUTHOR, String AUTHOR_CONTACT) {
        super(AUTHOR, AUTHOR_CONTACT, "LdapPlugin");
        loader = new LdapLoader();
        settingsField = new LDAPSettingsField(loader);
        super.setLoader(loader);
        super.setSettingsField(settingsField);
    }

    @Override
    public SettingsField getSettingsField() {
        return settingsField;
    }

    @Override
    public ArrayList<AddressBookEntry> getResults(String query, int number) {
        return loader.getResults(query,number);
    }

    @Override
    public void readFields(Statement statement, Connection con) throws SQLException {

    }
}
