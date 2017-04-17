/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;

import com.johannes.lsctic.messagestage.ErrorMessage;
import com.johannes.lsctic.messagestage.SuccessMessage;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johannesengler
 */
public class SqlLiteConnection {

    private static final String JDBC = "jdbc:sqlite:";
    private String database;

    /**
     * Beispiel database: "settingsAndData.db"
     *
     * @param database
     */
    public SqlLiteConnection(String database) {
        this.database = database;
        String[] createLines2 = {"create table settings (id integer, setting string, description string)",
            "create table internfields (id integer  Primary Key AUTOINCREMENT, number string, name string, callcount integer, favorit boolean)"};
        createDatabase(database, createLines2);
    }

    private void createDatabase(String database, String[] createLines) {
        File f = new File(database);
        if (f.exists() && !f.isDirectory()) {
            try(Connection connection = DriverManager.getConnection(JDBC + database)) {
                String query = "SELECT name FROM sqlite_master WHERE type=?";
                try (PreparedStatement ptsm = connection.prepareStatement(query)) {
                    ptsm.setString(1, "table");
                    ResultSet table = ptsm.executeQuery();
                    ArrayList<String> check = new ArrayList<>();
                    while(table.next()) {
                        Logger.getLogger(getClass().getName()).info(table.getString("name"));
                        check.add(table.getString("name"));
                    }
                    if(!(check.contains("settings") && check.contains("internfields") || (check.contains("callhistory") && check.contains("phonebook")))){
                        throw new SQLException("Database seems not to have the right scheme");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                new ErrorMessage("Database seems to exist but is not readable. Please check database integrity and if necessary delete it");
            }

        } else {
            try (Connection connection = DriverManager.getConnection(JDBC + database)) {
                try( Statement statement = connection.createStatement()) {
                    // Erstelle die Datenbank für das Programm
                    statement.setQueryTimeout(30);
                    //Asterisk Optionen
                    for (String create : createLines) {
                        statement.executeUpdate(create);
                    }
                }
                new SuccessMessage("Created new database.");
            } catch (SQLException e) {
                new ErrorMessage("Could not create local database in folder " + f.getAbsolutePath());
                Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, e);            }


        }

    }

    /**
     * Performs an insert or update statement on the settings table. Used to update or insert any settings
     *
     * @param description
     * @param settingValue
     */
    public void buildUpdateOrInsertStatementForSetting(String description, String settingValue) {
        int currentId = getMaxIdValueOfTable("Settings") + 1;
        Logger.getLogger(getClass().getName()).info(String.valueOf(currentId));
        try (Connection con = DriverManager.getConnection(JDBC + database); Statement statement = con.createStatement()) {
            statement.setQueryTimeout(10);
            con.setAutoCommit(false);
            final String query = "UPDATE Settings SET setting='" + settingValue + "' WHERE description='" + description + "'";
            final String query2 = "INSERT INTO Settings (id, setting , description) SELECT " + currentId + ",'" + settingValue + "','" + description + "' WHERE (Select Changes() = 0)";
            statement.addBatch(query);
            statement.addBatch(query2);
            statement.executeBatch();
            con.commit();
            statement.closeOnCompletion();
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, null, ex);
        }
    }


    private int getMaxIdValueOfTable(String table) {
        try (Connection con = DriverManager.getConnection(JDBC + database); Statement statement = con.createStatement()) {
            statement.setQueryTimeout(10);
            final String query = "Select max(id) from " + table;

            ResultSet set = statement.executeQuery(query);
            return set.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, null, ex);
        }
        return 0;
    }


    /**
     * Runs a SQL query and returns the results
     *
     * @param query
     * @return resultset
     */
    public String query(String query) {
        try(Connection connection = DriverManager.getConnection(JDBC + database); Statement statement = connection.createStatement()) {
            statement.setQueryTimeout(10);
            return statement.executeQuery(query).getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            new ErrorMessage("There was a database error with the query: \""+query+"\"");
            return null;
        }
    }


    /**
     * Select specific attribut where specific attribute in database
     *
     * @param attribut
     * @param table
     * @param whereAttribut
     * @param whereValue
     * @return
     */
    public ResultSet selectWhere(String attribut, String table, String whereAttribut, String whereValue) {
        String state = "select ? from ? where ?=?";
        if(!(whereAttribut == null && whereValue == null)) {
            state = "select ? from ?";
        }
        try(Connection connection = DriverManager.getConnection(JDBC + database); PreparedStatement statement = connection.prepareStatement(state)) {
            if (whereAttribut == null && whereValue == null) {
                statement.setString(1, attribut);
                statement.setString(2, table);
                statement.setString(3, whereAttribut);
                statement.setString(4, whereValue);
                statement.setQueryTimeout(10);
                return statement.executeQuery();
            } else {
                statement.setString(1, attribut);
                statement.setString(2, table);
                statement.setQueryTimeout(10);
                return statement.executeQuery();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            new ErrorMessage("There was a database error with the query");
            return null;
        }
    }

    /**
     * Insert values into table value beispiel: "1, 'leo', 'test'"
     *
     * @param table
     * @param value
     */
    public void insert(String table, String value) {
        try(Connection connection = DriverManager.getConnection(JDBC + database); Statement statement = connection.createStatement()) {
            statement.setQueryTimeout(10);
            statement.executeUpdate("insert into " + table + " values(" + value + ")");
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        new ErrorMessage("There was a database error with the query");

    }

    /**
     * Updates one attribute
     *
     * @param table
     * @param whereAttribut
     * @param whereValue
     * @param updateAttribut
     * @param updateValue
     */
    public void updateOneAttribute(String table, String whereAttribut, String whereValue, String updateAttribut, String updateValue) {
        try(Connection connection = DriverManager.getConnection(JDBC + database); Statement statement = connection.createStatement()) {
            statement.setQueryTimeout(10);
            statement.executeUpdate("UPDATE " + table + " SET " + updateAttribut + " = " + updateValue + " WHERE " + whereAttribut + " = " + whereValue);
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            new ErrorMessage("There was a database error with the query");
        }
    }

    public ArrayList<String[]> getFieldsForDataSource(String datasource) {
        ArrayList<String[]> dataSourceFields = new ArrayList<>();
        int i = 0;
        String quField = datasource + "Field";
        while (true) {
            try (Connection connection = DriverManager.getConnection(JDBC + database); PreparedStatement statement = connection.prepareStatement("select setting from settings where description = ?")) {
                statement.setString(1, quField + i);
                try (ResultSet fieldRS = statement.executeQuery()) {
                    if (fieldRS.next()) {
                        String field = fieldRS.getString("setting");
                        String[] parameter = field.split(";");
                        dataSourceFields.add(parameter);
                        ++i;
                    } else {
                        break;
                    }
                } catch (SQLException e) {
                    throw new SQLException();
                }
            } catch (SQLException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return dataSourceFields;
    }


    public ArrayList<String> getOptionsForDataSource(String name) {
        ArrayList<String> options = new ArrayList<>();
        int i = 0;
        String quField = name + "Setting";
        while (true) {
            try (Connection connection = DriverManager.getConnection(JDBC + database); PreparedStatement statement = connection.prepareStatement("select setting from settings where description = ?")) {
                statement.setString(1, quField + i);
                try (ResultSet fieldRS = statement.executeQuery()) {
                    if (fieldRS.next()) {
                        String field = fieldRS.getString("setting");
                        options.add(field);
                        ++i;
                    } else {
                        break;
                    }
                } catch (SQLException e) {
                    throw new SQLException();
                }
            } catch (SQLException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return options;
    }


    public void writePluginSettingsToDatabase(String name, ArrayList<String> options, ArrayList<String[]> linkFields) {
        queryNoReturn("Delete from settings where description LIKE '" + name + "Field_%%%%%%%%%%%%';");
        int i = 0;
        for (String[] strings : linkFields) {
            buildUpdateOrInsertStatementForSetting(name + "Field" + i, strings[0] + ";" + strings[1]);
            ++i;
        }
        i = 0;
        for (String option : options) {
            buildUpdateOrInsertStatementForSetting(name + "Setting" + i, option);
            ++i;
        }
    }


    /**
     *
     * @return Map with all interns
     */
    public Map<String, PhoneNumber> getInterns() {
        Map<String, PhoneNumber> internNumbers = new TreeMap<>();

        try(Connection connection = DriverManager.getConnection(JDBC + database); Statement statement = connection.createStatement()) {
            statement.setQueryTimeout(10);
            try (ResultSet rs = statement.executeQuery("select * from internfields")) {
                while (rs.next()) {
                    internNumbers.put(rs.getString(2), new PhoneNumber(true, rs.getString(2), rs.getString(3), rs.getInt(4)));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            new ErrorMessage("There was a database error with the query. The data may not be complete");
            return internNumbers;
        }
        return internNumbers;
    }

    /**
     * Only a query without a return
     *
     * @param query
     */
    public void queryNoReturn(String query) {
        try(Connection connection = DriverManager.getConnection(JDBC + database); Statement statement = connection.createStatement()) {
            statement.setQueryTimeout(10);
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            new ErrorMessage("There was a database error with the query: \""+query+"\"");
        }

    }

    public String getConnection() {
        return JDBC + database;
    }


}
