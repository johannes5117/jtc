package com.johannes.lsctic;

import com.google.common.eventbus.EventBus;
import com.johannes.lsctic.panels.gui.fields.StartConnectionEvent;
import com.johannes.lsctic.panels.gui.plugins.PluginRegister;
import com.johannes.lsctic.panels.gui.settings.AsteriskSettingsField;
import com.johannes.lsctic.panels.gui.settings.DataSourceSettingsField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private String ownExtensionTemp;     // eigene Extension asterisk
    private long timeTemp;               // TimeStamp
    private String pluginFolder;
    private static final String DATABASE_CONNECTION = "jdbc:sqlite:settingsAndData.db";
    private static final String SETTING = "setting";
    private PluginRegister pluginRegister;
    private ArrayList<String> activatedDataSources = new ArrayList<>();
    private AsteriskSettingsField asteriskSettingsField;
    private DataSourceSettingsField dataSourceSettingsField;
    private EventBus bus;


    public OptionsStorage(Button accept, Button reject, VBox panelD, EventBus bus) {
        this.asteriskSettingsField = new AsteriskSettingsField();
        this.pluginRegister = new PluginRegister();
        this.dataSourceSettingsField = new DataSourceSettingsField();
        this.bus = bus;
        readSettingsFromDatabase();
        this.pluginRegister.explorePluginFolder(this.pluginFolder);

        ArrayList<String> pl = new ArrayList<>();
        pl.add("MysqlPlugin");
        pl.add("LdapPlugin");
        pl.add("TextFilePlugin");
        this.pluginRegister.registerHardCodedPlugins(pl);
        //TODO: Delete after manual Test -> Only for Test purpose

        this.pluginRegister.loadPlugins(activatedDataSources, pluginFolder);
        try (Connection con = DriverManager.getConnection(DATABASE_CONNECTION); Statement statement = con.createStatement()) {
            this.pluginRegister.activateAllPlugins(con);
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        dataSourceSettingsField.setCheckBoxes(pluginRegister.getPluginsFound(), activatedDataSources);
        panelD.getChildren().addAll(asteriskSettingsField, dataSourceSettingsField);
        panelD.getChildren().addAll(this.getPluginRegister().getAllSettingsFields());

        accept.setOnAction(event -> accept());
        reject.setOnAction(event -> setTempVariables());
    }

        /**
     * Used to store the temp. vars.
     */
    public void setTempVariables() {
        pluginRegister.discardAllPlugins();
        String[] options = {amiAddress, Integer.toString(amiServerPort), amiLogIn, amiPassword};
        this.asteriskSettingsField.setOptions(options);
        ownExtensionTemp = ownExtension;
        dataSourceSettingsField.setCheckBoxes(pluginRegister.getPluginsFound(),activatedDataSources);
        dataSourceSettingsField.setPluginFolder(pluginFolder);
    }
    /**
     * User accepts the changes and wants to store the changes in database
     */
    public void accept() {
        pluginRegister.acceptAllPlugins();
        setVariables();
        writeSettingsToDatabase();
        refreshProgram();
        //TODO: Implement Write Back Activated Datasource
    }

    private void refreshProgram() {
        if(asteriskSettingsField.hasChanged()) {
            bus.post(new StartConnectionEvent("localhost", 12345, "Tset", "Test"));
        } else if(dataSourceSettingsField.hasChanged()) {
            //TODO: Implement Reload for Plugins
            Logger.getLogger(getClass().getName()).info("ÄNDERUNG");
        }

    }

    /**
     * Sets the real variables to the temps. That will happen if user clicks on
     * accept
     */
    public void setVariables() {
        // compare the old with the new values -> if no change happened its not necessary to restart server connection
        String[] optionsCompare = {amiAddress, Integer.toString(amiServerPort), amiLogIn, amiPassword};
        String[] options = asteriskSettingsField.getOptions(optionsCompare);
        amiAddress = options[0];
        amiServerPort = Integer.valueOf(options[1]);
        amiLogIn = options[2];
        amiPassword = options[3];

        ownExtension = ownExtensionTemp;

        // compare the old with the new values -> if no change happened its not necessary to reload the plugins
        activatedDataSources = (ArrayList<String>) this.dataSourceSettingsField.getChecked(activatedDataSources);
        pluginFolder = this.dataSourceSettingsField.getPluginFolder(pluginFolder);
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
            statement.executeUpdate(query + pluginFolder+"'WHERE description = 'pluginFolder'");
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

            readDatabaseForSources(con);
            
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
            try (PreparedStatement ptsm = con.prepareStatement(query)) {
                ptsm.setString(1, "pluginFolder");
                ResultSet amiAddressRS = ptsm.executeQuery();
                pluginFolder = !amiAddressRS.next() ? "" : amiAddressRS.getString(SETTING);
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



    public void readDatabaseForSources(Connection con) throws SQLException {
        int i = 0;
        String quField = "datasource";
        while (true) {
            try(PreparedStatement statement = con.prepareStatement("select setting from settings where description = ?")) {
                statement.setString(1, quField + i);
                try (ResultSet fieldRS = statement.executeQuery()) {
                    if (fieldRS.next()) {
                        String dataSourceName = fieldRS.getString("setting");
                        activatedDataSources.add(dataSourceName);
                        ++i;
                    } else {
                        break;
                    }
                } catch (SQLException e) {
                    throw e;
                }
            }
        }
    }


   
    public String getAmiAddress() {
        return amiAddress;
    }

    public int getAmiServerPort() {
        return amiServerPort;
    }

    public String getAmiLogIn() {
        return amiLogIn;
    }

    public String getAmiPassword() {
        return amiPassword;
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

   public List<String> getActivatedDataSources() {
        return this.activatedDataSources;
   }


    public PluginRegister getPluginRegister() {
        return pluginRegister;
    }

    public void setAsteriskSettingsField(AsteriskSettingsField asteriskSettingsField) {
        this.asteriskSettingsField = asteriskSettingsField;
        String[] options = {amiAddress, Integer.toString(amiServerPort), amiLogIn, amiPassword};
        this.asteriskSettingsField.setOptions(options);
    }

}
