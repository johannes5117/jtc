package com.johannes.lsctic;

import com.johannes.lsctic.address.AddressPlugin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johannes.lsctic.address.LoaderRegister;
import com.johannes.lsctic.address.loaders.AddressLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * @author johannesengler
 */
public final class OptionsStorage {

    private String amiAddress;        //AMI Server Address
    private int amiServerPort;       //AMI Server Port
    private String amiLogIn;         //AMI Login
    private String amiPassword;      //AMI Password
    
    private String ownExtension;     // eigene Extension asterisk
    private long time;               // TimeStamp

    private String amiAddressTemp;        //AMI Server Addresse
    private int amiServerPortTemp;       //AMI Server Port
    private String amiLogInTemp;         //AMI Login
    private String amiPasswordTemp;      //AMI Password
    private String ownExtensionTemp;     // eigene Extension asterisk
    private long timeTemp;               // TimeStamp

    private String pluginFolder ="plugin";

    private static final String DATABASE_CONNECTION = "jdbc:sqlite:settingsAndData.db";
    private static final String SETTING = "setting";

    private LoaderRegister loaderRegister;

    private ArrayList<String> activatedDataSources = new ArrayList<>();
    private ArrayList<String> activatedDataSourcesTemp = new ArrayList<>();

    public OptionsStorage(Button accept, Button reject ) {
        this.loaderRegister = new LoaderRegister();
        readSettingsFromDatabase();
        this.loaderRegister.explorePluginFolder(this.pluginFolder);

        ArrayList<String> pl = new ArrayList<>();
        pl.add("MysqlPlugin");
        this.loaderRegister.registerHardCodedPlugins(pl);
        //TODO: Delete after Test -> Only for Test purpose

        this.loaderRegister.loadPlugins(activatedDataSources, pluginFolder);
        try (Connection con = DriverManager.getConnection(DATABASE_CONNECTION); Statement statement = con.createStatement()) {
            this.loaderRegister.activateAllPlugins(statement,con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        accept.setOnAction(event -> accept());
        reject.setOnAction(event -> setTempVariables());
    }

        /**
     * Used to store the temp. vars.
     */
    public void setTempVariables() {
        loaderRegister.discardAllPlugins();
        amiAddressTemp = amiAddress;
        amiServerPortTemp = amiServerPort;
        amiLogInTemp = amiLogIn;
        amiPasswordTemp = amiPassword;
        ownExtensionTemp = ownExtension;
        activatedDataSourcesTemp = activatedDataSources;
    }

    /**
     * User accepts the changes and wants to store the changes in database
     */
    public void accept() {
        loaderRegister.acceptAllPlugins();
        setVariables();
        writeSettingsToDatabase();
        //TODO: Implement Write Back Activated Datasource
    }

    /**
     * Sets the real variables to the temps. That will happen if user clicks on
     * accept
     */
    public void setVariables() {
        amiAddress = amiAddressTemp;
        amiServerPort = amiServerPortTemp;
        amiLogIn = amiLogInTemp;
        amiPassword = amiPasswordTemp;
        ownExtension = ownExtensionTemp;
        activatedDataSources = activatedDataSourcesTemp;
    }

    /**
     * Saves the settings in the sqlite database
     */
    private void writeSettingsToDatabase() {

        try (Connection con = DriverManager.getConnection(DATABASE_CONNECTION); Statement statement = con.createStatement()) {
            statement.setQueryTimeout(10);
            final String query = "UPDATE Settings SET setting = '";
            statement.executeUpdate(query + amiAddress + "' WHERE description = 'amiAddress'");
            statement.executeUpdate(query + amiServerPort + "' WHERE description = 'amiServerPort'");
            statement.executeUpdate(query + amiLogIn + "' WHERE description = 'amiLogIn'");
            statement.executeUpdate(query + amiPassword + "' WHERE description = 'amiPassword'");
            

        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, null, ex);
        }
    }

    /**
     * reads the settings saved in the sqlite database
     */
    public void readSettingsFromDatabase() {

        try (Connection con = DriverManager.getConnection(DATABASE_CONNECTION); Statement statement = con.createStatement()) {
            statement.setQueryTimeout(10);
            //Safely read in all Settings. If a setting isnt found a default value will be taken
            final String query = "select setting from settings where description = ?";
            readAmiFields(query, con);

            readDatabaseForSources(con, statement);
            
            try (PreparedStatement ptsm = con.prepareStatement(query)) {
                ptsm.setString(1, "ownExtension");
                ResultSet amiAddressRS=  ptsm.executeQuery();
                ownExtension = !amiAddressRS.next() ? "201" : amiAddressRS.getString(SETTING);
            }
            try (PreparedStatement ptsm = con.prepareStatement(query)) {
                ptsm.setString(1, "time");
                ResultSet amiAddressRS = ptsm.executeQuery();
                time = Long.valueOf(!amiAddressRS.next() ? Long.toString(System.currentTimeMillis()) : amiAddressRS.getString(SETTING));
            }

            setTempVariables();
        } catch (SQLException ex) {
            Logger.getLogger(OptionsStorage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Reads the AMI settings from the sqlite database
     *
     * @param query
     * @param con
     * @throws SQLException
     */
    private void readAmiFields(String query, Connection con) throws SQLException {
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "amiAddress");
            ResultSet amiAddressRS = ptsm.executeQuery();
            amiAddress = !amiAddressRS.next() ? "localhost" : amiAddressRS.getString(SETTING);
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "amiServerPort");
            ResultSet amiAddressRS = ptsm.executeQuery();
            amiServerPort = Integer.valueOf(!amiAddressRS.next() ? "12350" : amiAddressRS.getString(SETTING));
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "amiLogIn");
            ResultSet amiAddressRS = ptsm.executeQuery();
            amiLogIn = !amiAddressRS.next() ? "admin" : amiAddressRS.getString(SETTING);
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "amiPassword");
            ResultSet amiAddressRS = ptsm.executeQuery();
            amiPassword = !amiAddressRS.next() ? "" : amiAddressRS.getString(SETTING);
        }
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
                    String dataSourceName = fieldRS.getString("setting");
                    activatedDataSources.add(dataSourceName);
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


   
    public String getAmiAddress() {
        return amiAddress;
    }

    public void setAmiAddress(String amiAddress) {
        this.amiAddress = amiAddress;
    }

    public int getAmiServerPort() {
        return amiServerPort;
    }

    public void setAmiServerPort(int amiServerPort) {
        this.amiServerPort = amiServerPort;

    }

    public String getAmiLogIn() {
        return amiLogIn;
    }

    public void setAmiLogIn(String amiLogIn) {
        this.amiLogIn = amiLogIn;
    }

    public String getAmiPassword() {
        return amiPassword;
    }

    public void setAmiPassword(String amiPassword) {
        this.amiPassword = amiPassword;
    }

    
    public void setAmiAddressTemp(String amiAddressTemp) {
        this.amiAddressTemp = amiAddressTemp;
    }

    public void setAmiServerPortTemp(int amiServerPortTemp) {
        this.amiServerPortTemp = amiServerPortTemp;
    }

    public void setAmiLogInTemp(String amiLogInTemp) {
        this.amiLogInTemp = amiLogInTemp;
    }

    public void setAmiPasswordTemp(String amiPasswordTemp) {
        this.amiPasswordTemp = amiPasswordTemp;
    }

   

    public String getOwnExtension() {
        return ownExtension;
    }

    public void setOwnExtension(String ownExtension) {
        this.ownExtension = ownExtension;
    }

    public void setOwnExtensionTemp(String ownExtensionTemp) {
        this.ownExtensionTemp = ownExtensionTemp;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTimeTemp(long timeTemp) {
        this.timeTemp = timeTemp;
    }

   public void activateDatasource(String datasource) {
        activatedDataSourcesTemp.add(datasource);
   }
   public void deactivateDatasource(String datasource) {
        activatedDataSourcesTemp.remove(datasource);
   }


    public LoaderRegister getLoaderRegister() {
        return loaderRegister;
    }

}
