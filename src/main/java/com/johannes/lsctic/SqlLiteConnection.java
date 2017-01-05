/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johannesengler
 */
public class SqlLiteConnection {

    private Connection connection;
    private Connection localConnection;
    private static final String JDBC = "jdbc:sqlite:";

    /**
     * Beispiel database: "settingsAndData.db"
     *
     * @param database
     * @param localDatabase
     */
    public SqlLiteConnection(String database, String localDatabase) {
        String[] createLines = {"create table callhistory (id integer, number string, outgoing boolean)",
            "create table phonebook (id integer, number string, name string, callcount integer, favorit boolean)"};
        createDatabase(localDatabase, createLines);
        String[] createLines2 = {"create table settings (id integer, setting string, description string)",
            "create table internfields (id integer  Primary Key AUTOINCREMENT, number string, name string, callcount integer, favorit boolean)"};
        createDatabase(database, createLines2);
    }

    private void createDatabase(String database, String[] createLines) {
        File f = new File(database);
        if (f.exists() && !f.isDirectory()) {
            try {
                connection = DriverManager.getConnection(JDBC + database);
            } catch (SQLException ex) {
                Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            Statement statement = null;
            try {
                // Erstelle die Datenbank für das Programm
                connection = DriverManager.getConnection(JDBC + database);
                statement = connection.createStatement();
                statement.setQueryTimeout(30);
                //Asterisk Optionen
                for (String create : createLines) {

                    statement.executeUpdate(create);

                }

            } catch (SQLException e) {
                Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

    }

    /**
     * Closes the Connections to the SQLite Databases
     */
    public void closeConnections() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (localConnection != null) {
            try {
                localConnection.close();
            } catch (SQLException ex) {
                Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Runs a SQL query and returns the results
     *
     * @param query
     * @return resultset
     */
    public ResultSet query(String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            return statement.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Perform a update query on the server
     *
     * @param update
     */
    public void update(String update) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            statement.executeUpdate(update);
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
        PreparedStatement statement = null;
        try {
            if (whereAttribut == null && whereValue == null) {
                String state = "select ? from ? where ?=?";
                statement = connection.prepareStatement(state);
                statement.setString(1, attribut);
                statement.setString(2, table);
                statement.setString(3, whereAttribut);
                statement.setString(4, whereValue);
                statement.setQueryTimeout(10);
                return statement.executeQuery();
            } else {
                String state = "select ? from ?";
                statement = connection.prepareStatement(state);
                statement.setString(1, attribut);
                statement.setString(2, table);
                statement.setQueryTimeout(10);
                return statement.executeQuery();
            }

        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Insert values into table value beispiel: "1, 'leo', 'test'"
     *
     * @param table
     * @param value
     */
    public void insert(String table, String value) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            statement.executeUpdate("insert into " + table + " values(" + value + ")");
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
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
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            statement.executeUpdate("UPDATE " + table + " SET " + updateAttribut + " = " + updateValue + " WHERE " + whereAttribut + " = " + whereValue);
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * get the connection
     *
     * @return
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     *
     * @return Map with all interns
     */
    Map<String, PhoneNumber> getInterns() {
        Map<String, PhoneNumber> internNumbers = new TreeMap<>();

        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            try (ResultSet rs = statement.executeQuery("select * from internfields")) {
                while (rs.next()) {
                    internNumbers.put(rs.getString(2), new PhoneNumber(true, rs.getString(2), rs.getString(3), rs.getInt(4)));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            return internNumbers;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return internNumbers;
    }

    /**
     * Only a query without a return
     *
     * @param query
     */
    void queryNoReturn(String query) {

        try (Statement statement = connection.createStatement()) {
            statement.setQueryTimeout(10);
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
