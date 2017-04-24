package com.johannes.lsctic;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.johannes.lsctic.panels.gui.fields.callrecordevents.FoundCdrNameInDataSourceEvent;
import com.johannes.lsctic.panels.gui.fields.callrecordevents.NotFoundCdrNameInDataSourceEvent;
import com.johannes.lsctic.panels.gui.fields.callrecordevents.SearchDataSourcesForCdrEvent;
import com.johannes.lsctic.panels.gui.fields.otherevents.CloseApplicationSafelyEvent;
import com.johannes.lsctic.panels.gui.fields.otherevents.StartConnectionEvent;
import com.johannes.lsctic.panels.gui.fields.otherevents.UpdateAddressFieldsEvent;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.UserLoginStatusEvent;
import com.johannes.lsctic.panels.gui.plugins.AddressBookEntry;
import com.johannes.lsctic.panels.gui.plugins.AddressPlugin;
import com.johannes.lsctic.panels.gui.plugins.PluginRegister;
import com.johannes.lsctic.panels.gui.plugins.PluginSettingsField;
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

    private static final String SETTING = "setting";
    private String amiAddress;        //AMI Server Address
    private int amiServerPort;       //AMI Server Port
    private String amiLogIn;         //AMI Login
    private String amiPasswordHash;
    private long time;               // TimeStamp
    private String pluginFolder;
    private PluginRegister pluginRegister;
    private ArrayList<String> activatedDataSources = new ArrayList<>();
    private AsteriskSettingsField asteriskSettingsField;
    private DataSourceSettingsField dataSourceSettingsField;
    private EventBus bus;
    private VBox panelD;
    private SqlLiteConnection sqlLiteConnection;

    public OptionsStorage(Button accept, Button reject, VBox panelD, EventBus bus, SqlLiteConnection sqlLiteConnection) {
        this.asteriskSettingsField = new AsteriskSettingsField();
        this.pluginRegister = new PluginRegister();
        this.dataSourceSettingsField = new DataSourceSettingsField();
        this.bus = bus;
        bus.register(this);
        this.panelD = panelD;
        this.sqlLiteConnection = sqlLiteConnection;

        readSettingsFromDatabase();

        this.pluginRegister.explorePluginFolder(this.pluginFolder);

        setUpPlugins();

        panelD.getChildren().addAll(asteriskSettingsField, dataSourceSettingsField);
        panelD.getChildren().addAll(this.getPluginRegister().getAllPluginSettingsFields());

        accept.setOnAction(event -> accept());
        reject.setOnAction(event -> setTempVariables());
    }


    private void setUpPlugins() {
        this.pluginRegister.loadPlugins(activatedDataSources, pluginFolder);
        this.pluginRegister.activateAllPlugins(sqlLiteConnection);
        dataSourceSettingsField.setCheckBoxes(pluginRegister.getPluginsFound(), activatedDataSources, this.pluginRegister.getLoadedPluginNames());
    }

        /**
     * Used to store the temp. vars.
     */
    public void setTempVariables() {
        pluginRegister.discardAllPlugins();
        String[] options = {amiAddress, Integer.toString(amiServerPort), amiLogIn};
        this.asteriskSettingsField.setOptions(options);
        dataSourceSettingsField.setCheckBoxes(pluginRegister.getPluginsFound(),activatedDataSources, this.pluginRegister.getLoadedPluginNames());
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
    }

    private void refreshProgram() {
        if(asteriskSettingsField.hasChanged() || asteriskSettingsField.passwordChanged()) {
            String[] options = asteriskSettingsField.getOptions();
            bus.post(new StartConnectionEvent(options[0], Integer.parseInt(options[1]), options[2], options[3], false));
        } else if(dataSourceSettingsField.hasChanged()) {
            setUpPlugins();
            writeActivatedDataSourcesToDatabase();
            if(dataSourceSettingsField.isExpanded()) {
                dataSourceSettingsField.refresh();
                for (PluginSettingsField settingsField : this.getPluginRegister().getAllPluginSettingsFields()) {
                    if(!panelD.getChildren().contains(settingsField)) {
                        panelD.getChildren().add(settingsField);
                    }
                }
                List<AddressBookEntry> ld = getPluginRegister().getResultFromEveryPlugin("", 10);
                bus.post(new UpdateAddressFieldsEvent(ld));
            }
            Logger.getLogger(getClass().getName()).info("ÄNDERUNG");
        }

        for (AddressPlugin plugin : this.getPluginRegister().getAllActivePlugins()) {
            if (plugin.getPluginSettingsField().hasChanged()) {
                sqlLiteConnection.writePluginSettingsToDatabase(plugin.getName(), plugin.getOptions(), plugin.getDataFields());
            }
        }

    }

    /**
     * Sets the real variables to the temps. That will happen if user clicks on
     * accept
     */
    public void setVariables() {
        // compare the old with the new values -> if no change happened its not necessary to restart server connection
        String[] optionsCompare = {amiAddress, Integer.toString(amiServerPort), amiLogIn};
        String[] options = asteriskSettingsField.getOptionsTriggerHasChanged(optionsCompare);
        amiAddress = options[0];
        amiServerPort = Integer.valueOf(options[1]);
        amiLogIn = options[2];


        // compare the old with the new values -> if no change happened its not necessary to reload the plugins
        activatedDataSources = (ArrayList<String>) this.dataSourceSettingsField.getChecked(activatedDataSources);
        pluginFolder = this.dataSourceSettingsField.getPluginFolder(pluginFolder);
    }

    /**
     * Saves the settings in the sqlite database
     */
    private void writeSettingsToDatabase() {
        sqlLiteConnection.buildUpdateOrInsertStatementForSetting("amiAddress", amiAddress);
        sqlLiteConnection.buildUpdateOrInsertStatementForSetting("amiServerPort", String.valueOf(amiServerPort));
        sqlLiteConnection.buildUpdateOrInsertStatementForSetting("amiLogIn", amiLogIn);
        sqlLiteConnection.buildUpdateOrInsertStatementForSetting("pluginFolder", pluginFolder);
    }

    /**
     * deletes all activated entries from the database and writes the currently activated
     */
    private void writeActivatedDataSourcesToDatabase() {
        sqlLiteConnection.queryNoReturn("Delete from settings where description LIKE 'datasource_%%%%%%%%%%%%';");
        int i = 0;
        for (String s : activatedDataSources) {
            sqlLiteConnection.buildUpdateOrInsertStatementForSetting("datasource" + i, s);
            ++i;
        }
    }

    /**
     * reads the settings saved in the sqlite database
     */
    public void readSettingsFromDatabase() {

        try (Connection con = DriverManager.getConnection(sqlLiteConnection.getConnection()); Statement statement = con.createStatement()) {
            statement.setQueryTimeout(10);
            //Safely read in all Settings. If a setting isnt found a default value will be taken
            final String query = "select setting from settings where description = ?";
            readAmiFields(query, con);

            readDatabaseForSources(con);
            

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
            ptsm.setString(1, "amiPasswordHash");
            ResultSet amiAddressRS = ptsm.executeQuery();
            amiPasswordHash = !amiAddressRS.next() ? "" : amiAddressRS.getString(SETTING);
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

    @Subscribe
    public void searchNameForCdr(SearchDataSourcesForCdrEvent event) {
        Logger.getLogger(getClass().getName()).info("TTTTTTT");

        String name = getPluginRegister().getNameToNumber(event.getWho());
        if(name.equals("")) {
            bus.post(new NotFoundCdrNameInDataSourceEvent(event));
        } else {
            bus.post(new FoundCdrNameInDataSourceEvent(event, name));
        }
    }

    @Subscribe
    public void closeApplication(CloseApplicationSafelyEvent event) {
        bus.unregister(this);
    }

    @Subscribe
    public void loginSuccessfulNewHash(UserLoginStatusEvent event) {
        if (event.isLoggedIn()) {
            this.amiPasswordHash = event.getHashedPw();
            sqlLiteConnection.buildUpdateOrInsertStatementForSetting("amiPasswordHash", this.amiPasswordHash);
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

    public String getAmiPasswordHash() {
        return amiPasswordHash;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

   public List<String> getActivatedDataSources() {
        return this.activatedDataSources;
   }


    public PluginRegister getPluginRegister() {
        return pluginRegister;
    }

    public void setAsteriskSettingsField(AsteriskSettingsField asteriskSettingsField) {
        this.asteriskSettingsField = asteriskSettingsField;
        String[] options = {amiAddress, Integer.toString(amiServerPort), amiLogIn};
        this.asteriskSettingsField.setOptions(options);
    }

}
