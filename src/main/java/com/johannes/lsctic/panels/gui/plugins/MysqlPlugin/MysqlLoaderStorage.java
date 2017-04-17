package com.johannes.lsctic.panels.gui.plugins.MysqlPlugin;

import java.util.ArrayList;

/**
 * Created by johannes on 31.03.2017.
 */
public class MysqlLoaderStorage {

    private String serverAddress = "localhost";
    private int serverPort = 3306;
    private String database = "database";
    private int mobile = -1;
    private int telephone = -1;
    // the fields that match database value name and view name eg. tel in database would be showed as telephone in the program
    private ArrayList<String[]> mysqlFields;

    /**
     * Copy Constructor, deep Copy is required for the safe discard functionality
     * @param old
     */
    public MysqlLoaderStorage(MysqlLoaderStorage old) {
        this.serverAddress = old.getServerAddress();
        this.serverPort = old.getServerPort();
        this.database = old.getDatabase();
        mysqlFields = new ArrayList<>();
        for(String[] field: old.getMysqlFields()) {
            String[] newField = new String[field.length];
            int i = 0;
            for(String inner: field) {
                newField[i] = inner;
                ++i;
            }
        }
    }

    public MysqlLoaderStorage() {
        mysqlFields = new ArrayList<>();
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public ArrayList<String[]> getMysqlFields() {
        return mysqlFields;
    }

    public void setMysqlFields(ArrayList<String[]> mysqlFields) {
        this.mysqlFields = mysqlFields;
    }

    public void removeFromMysqlFields(String text, String text1) {
        for (String[] entry : mysqlFields) {
            if (entry[0].equals(text) && entry[1].equals(text1)) {
                mysqlFields.remove(entry);
                return;
            }
        }
    }

    public boolean addToMysqlFields(String text, String text1) {
        for (String[] entry : mysqlFields) {
            if (entry[0].equals(text) || entry[1].equals(text1)) {
                //One of the entries is already available
                return false;
            }
        }
        String[] g = {text, text1};
        mysqlFields.add(g);
        return true;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }
}
