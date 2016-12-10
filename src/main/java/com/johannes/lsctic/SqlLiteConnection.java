/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
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
    private final String JDBC = "jdbc:sqlite:";

    /**
     * Beispiel database: "settingsAndData.db"
     *
     * @param database
     * @param localDatabase
     */
    public SqlLiteConnection(String database, String localDatabase) {
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
                statement.executeUpdate("create table settings (id integer, setting string, description string)");

                statement.executeUpdate("create table internfields (id integer  Primary Key AUTOINCREMENT, number string, name string, callcount integer, favorit boolean)");

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

        File f2 = new File("localDatabase");
        if (f2.exists() && !f2.isDirectory()) {
            try {
                localConnection = DriverManager.getConnection(JDBC + localDatabase);
            } catch (SQLException ex) {
                Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Statement statement = null;
            try {
                // Erstelle die Datenbank für das Programm
                localConnection = DriverManager.getConnection(JDBC + localDatabase);
                statement = localConnection.createStatement();
                statement.setQueryTimeout(30);

                statement.executeUpdate("create table callhistory (id integer, number string, outgoing boolean)");
                statement.executeUpdate("create table phonebook (id integer, number string, name string, callcount integer, favorit boolean)");

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
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void update(String update) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            statement.executeUpdate(update);
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public ResultSet selectWhere(String attribut, String table, String whereAttribut, String whereValue) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            return statement.executeQuery("select " + attribut + " from " + table + " where " + whereAttribut + "=" + whereValue + "");
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public ResultSet select(String attribut, String table) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            return statement.executeQuery("select " + attribut + " from " + table);
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // value beispiel: "1, 'leo', 'test'"
    public void insert(String table, String value) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            statement.executeUpdate("insert into " + table + " values(" + value + ")");
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void updateOneAttribute(String table, String whereAttribut, String whereValue, String updateAttribut, String updateValue) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            statement.executeUpdate("UPDATE " + table + " SET " + updateAttribut + " = " + updateValue + " WHERE " + whereAttribut + " = " + whereValue);
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

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
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return internNumbers;
    }

    void queryNoReturn(String query) {

        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            ResultSet rs = statement.executeQuery(query);
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SqlLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
