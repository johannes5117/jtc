/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johannes
 */
public class DataSourceFields {
    
    private final HashMap<String, ArrayList<String[]>> fields;
    
    public DataSourceFields() {
        this.fields = new HashMap<>();
    }
    
    /**
     * Starts the reading of fields with a specific name fieldName from the Database and saves the fields in the Hashmap
     * 
     * @param fieldName
     * @param con
     * @param statement 
     */
    public void addFieldsFromDatabase(String fieldName,Connection con, Statement statement) {
        try {        
            fields.put(fieldName,readinLdapFieldsInner(fieldName,statement, con));
        } catch (SQLException ex) {
            Logger.getLogger(DataSourceFields.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Returns the fields for a specific field class
     * 
     * @param fieldName
     * @return 
     */
    public ArrayList<String[]> getFields(String fieldName) {
        return fields.get(fieldName);
    }
  
    /**
     * Reads in fields from the database which start with the word from @param fieldname
     *
     * @param fieldName
     * @param statement
     * @param con
     * @throws SQLException
     */
    private ArrayList<String[]> readinLdapFieldsInner(String fieldName, Statement statement, Connection con) throws SQLException {
        statement.setQueryTimeout(10);
        ArrayList<String[]> readInFields = new ArrayList<>();
        int i = 0;
        String quField = fieldName;
        while (true) {
            PreparedStatement statement2 = con.prepareStatement("select setting from settings where description = ?");
            statement2.setString(1, quField + i);

            Logger.getLogger(getClass().getName()).log(Level.INFO, "{0}{1}", new Object[]{quField, i});

            try (ResultSet fieldRS = statement2.executeQuery()) {
                if (fieldRS.next()) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Gefunden");
                    String field = fieldRS.getString("setting");
                    readInFields.add(field.split(";"));
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
        return readInFields;
    }

    
}
