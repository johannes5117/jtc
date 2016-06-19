/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.deployment;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author johannesengler
 */
public class SqlLiteDeployment {
    private Connection connection;
    private String database;
    public SqlLiteDeployment(String database) {
        this.database = database;
        File f = new File(database);
        if (f.exists() && !f.isDirectory()) {
            
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:"+database);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
          
        } else {
            try {
                // Erstelle die Datenbank für das Programm
                connection = DriverManager.getConnection("jdbc:sqlite:"+database);
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);
                //Asterisk Optionen
                statement.executeUpdate("create table settings (id integer, setting string, description string)");
                
                
                statement.executeUpdate("create table internfields (id integer, number string, name string, callcount integer, favorit boolean)");
                            
               
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            } 
        }
        
    }
    public boolean writeSettingsToDatabase(HashMap<String, String> settings) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:"+database);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(10);
        int i = 0;
        for(Entry<String, String> entry : settings.entrySet()) {
              statement.executeUpdate("insert into settings values("+i+", '"+entry.getValue()+"', '"+entry.getKey()+"')");
              ++i;
        }
        
        /*
        statement.executeUpdate("insert into settings values(0, '"+settings.get(0)+"', 'amiAdress')");
        statement.executeUpdate("insert into settings values(1, '"+settings.get(1)+"','amiServerPort')");
        statement.executeUpdate("insert into settings values(2, '"+settings.get(2)+"','amiLogIn')");
        statement.executeUpdate("insert into settings values(3, '"+settings.get(3)+"','amiPassword')");
        // LDAP Optionen
        statement.executeUpdate("insert into settings values(4, '"+settings.get(4)+"', 'ldapAdress')");
        statement.executeUpdate("insert into settings values(5, '"+settings.get(5)+"','ldapServerPort')");
        statement.executeUpdate("insert into settings values(6, '"+settings.get(6)+"','ldapSearchBase')");
        statement.executeUpdate("insert into settings values(7, '"+settings.get(7)+"','ldapBase')");
     
        statement.executeUpdate("insert into settings values(8, '"+settings.get(8)+"','ownExtension')");
        statement.executeUpdate("insert into settings values(9, '"+settings.get(9)+"','activated')");
        statement.executeUpdate("insert into settings values(10, '"+settings.get(10)+"','time')");
             */

        return true;
    }
    public boolean writeInternsToDatabase(ArrayList<Intern> interns) throws SQLException{
        connection = DriverManager.getConnection("jdbc:sqlite:"+database);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(10);
        int i= 0;
        for(Intern intern : interns) {
            statement.executeUpdate("insert into internfields values("+i+", '"+intern.getExtension()+"', '"+intern.getName()+"', 0, 0 )");
            ++i;
        }
        return true;
    }
    
}
