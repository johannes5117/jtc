package com.johannes.lsctic.address;

import com.johannes.lsctic.address.loaders.LdapLoader;
import com.johannes.lsctic.address.loaders.MySqlLoader;
import com.johannes.lsctic.settings.LDAPSettingsField;
import com.johannes.lsctic.settings.MysqlSettingsField;

import java.util.ArrayList;

/**
 * Created by johannes on 22.03.2017.
 */
public class MysqlPlugin extends AdressPlugin {
    private MySqlLoader loader;


    public MysqlPlugin(String AUTHOR, String AUTHOR_CONTACT) {
        super(AUTHOR, AUTHOR_CONTACT);
        loader = new MySqlLoader();
        MysqlSettingsField settingsField = new MysqlSettingsField(loader);
    }


}
