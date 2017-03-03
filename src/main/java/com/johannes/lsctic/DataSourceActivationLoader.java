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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johannes
 */
public class DataSourceActivationLoader {

    private boolean ldap = false;
    private boolean mysql = false;
    private boolean textFile = false;

    public DataSourceActivationLoader() {
    }

    public boolean isLdap() {
        return ldap;
    }

    public void setLdap(boolean ldap) {
        this.ldap = ldap;
    }

    public boolean isMysql() {
        return mysql;
    }

    public void setMysql(boolean mysql) {
        this.mysql = mysql;
    }

    public boolean isTextFile() {
        return textFile;
    }

    public void setTextFile(boolean textFile) {
        this.textFile = textFile;
    }

    void readDatabaseForSources(Connection con, Statement statement) throws SQLException {
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
                    checkOption(field,true);
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

    public void checkOption(String text, boolean val) {
        switch (text.toLowerCase()) {
            case ("mysql"):
                mysql = val;
                break;
            case ("ldap"):
                ldap = val;
                break;
            case ("textFile"):
                textFile = val;
                break;
        }
    }

}
