/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.address;

import com.johannes.lsctic.OptionsStorage;
import com.johannes.lsctic.address.loaders.AddressLoader;
import com.johannes.lsctic.address.loaders.LoaderRegister;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johannes
 */
public class DataSourceActivationDatabaseTool {

    DataSourceFields fields = new DataSourceFields();
    private OptionsStorage os;

    public DataSourceActivationDatabaseTool(OptionsStorage os) {
        this.os = os;
    }

    public void readDatabaseForSources(Connection con, Statement statement) throws SQLException {
        statement.setQueryTimeout(10);
        int i = 0;
        String quField = "datasource";
        while (true) {
            PreparedStatement statement2 = con.prepareStatement("select setting from settings where description = ?");
            statement2.setString(1, quField + i);
            try (ResultSet fieldRS = statement2.executeQuery()) {
                if (fieldRS.next()) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Gefunden");
                    String field = fieldRS.getString("setting");
                    checkOption(field, true, con, statement);
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
    }

    public void checkOption(String text, boolean val, Connection con, Statement statement) {
        if(con!=null) {
            fields.addFieldsFromDatabase(text, con, statement);
            if(fields.getFields(text).isEmpty()==false){
                LoaderRegister.addNewLoader(text);
            }
        }

    }

    public DataSourceFields getFields() {
        return fields;
    }
    
    

}
