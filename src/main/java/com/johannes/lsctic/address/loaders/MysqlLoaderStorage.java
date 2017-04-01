package com.johannes.lsctic.address.loaders;

import java.util.ArrayList;

/**
 * Created by johannes on 31.03.2017.
 */
public class MysqlLoaderStorage {

    private String serverAddress = "localhost";
    private int serverPort = 3306;
    private String database = "database";
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
    }

    public boolean addToMysqlFields(String text, String text1) {
        return true;
    }
}
