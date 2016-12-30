package com.johannes.lsctic;

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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * @author johannesengler
 */
public final class OptionsStorage {

    private String amiAdress;        //AMI Server Adresse
    private int amiServerPort;       //AMI Server Port
    private String amiLogIn;         //AMI Login
    private String amiPassword;      //AMI Password
    private String ldapAdress;       //LDAP Server Adresse
    private int ldapServerPort;      //LDAP Server Port
    private String ldapSearchBase;   //LDAP Suchbasis
    private String ldapBase;         //LDAP Basis
    private int ldapSearchAmount;       //Amount of Entrys that will be loaded 
    private String ownExtension;     // eigene Extension asterisk
    private long time;               // TimeStamp
    private ArrayList<String[]> ldapFields = new ArrayList<>();  // LDAP Felder mit Namen

    private String amiAdressTemp;        //AMI Server Adresse
    private int amiServerPortTemp;       //AMI Server Port
    private String amiLogInTemp;         //AMI Login
    private String amiPasswordTemp;      //AMI Password
    private String ldapAdressTemp;       //LDAP Server Adresse
    private int ldapServerPortTemp;      //LDAP Server Port
    private String ldapSearchBaseTemp;   //LDAP Suchbasis
    private String ldapBaseTemp;         //LDAP Basis
    private int ldapSearchAmountTemp = 0; //Amount of Entrys that will be loaded 
    private String ownExtensionTemp;     // eigene Extension asterisk
    private long timeTemp;               // TimeStamp
    private final ArrayList<String[]> ldapFieldsTemp = new ArrayList<>();  // LDAP Felder mit Namen

    private static final String DATABASE_CONNECTION = "jdbc:sqlite:settingsAndData.db";
    private static final String SETTING = "setting";

    public OptionsStorage(Button accept, Button reject) {
        accept.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                accept();
            }
        });
        reject.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                setTempVariables();
            }
        });

        readSettingsFromDatabase();
    }

    /**
     * Used to store the temp. vars.
     */
    public void setTempVariables() {
        amiAdressTemp = amiAdress;
        amiServerPortTemp = amiServerPort;
        amiLogInTemp = amiLogIn;
        amiPasswordTemp = amiPassword;
        ldapAdressTemp = ldapAdress;
        ldapServerPortTemp = ldapServerPort;
        ldapSearchBaseTemp = ldapSearchBase;
        ldapBaseTemp = ldapBase;
        ldapSearchAmountTemp = ldapSearchAmount;
        ownExtensionTemp = ownExtension;
        ldapFieldsTemp.clear();
        for (String[] p : ldapFields) {
            String[] g = {p[0], p[1]};
            ldapFieldsTemp.add(g);
        }
    }

    /**
     * User accepts the changes and wants to store the changes in database
     */
    public void accept() {
        setVariables();
        writeSettingsToDatabase();
    }

    /**
     * Sets the real variables to the temps. That will happen if user clicks on
     * accept
     */
    public void setVariables() {
        amiAdress = amiAdressTemp;
        amiServerPort = amiServerPortTemp;
        amiLogIn = amiLogInTemp;
        amiPassword = amiPasswordTemp;
        ldapAdress = ldapAdressTemp;
        ldapServerPort = ldapServerPortTemp;
        ldapSearchBase = ldapSearchBaseTemp;
        ldapBase = ldapBaseTemp;
        ldapSearchAmount = ldapSearchAmountTemp;
        ownExtension = ownExtensionTemp;
        ldapFields.clear();

        for (String[] p : ldapFieldsTemp) {
            String[] g = {p[0], p[1]};
            ldapFields.add(g);
        }
    }

    /**
     * Saves the settings in the sqlite database
     */
    private void writeSettingsToDatabase() {

        try (Connection con = DriverManager.getConnection(DATABASE_CONNECTION); Statement statement = con.createStatement()) {
            statement.setQueryTimeout(10);
            final String query = "UPDATE Settings SET setting = '";
            statement.executeUpdate(query + amiAdress + "' WHERE description = 'amiAdress'");
            statement.executeUpdate(query + amiServerPort + "' WHERE description = 'amiServerPort'");
            statement.executeUpdate(query + amiLogIn + "' WHERE description = 'amiLogIn'");
            statement.executeUpdate(query + amiPassword + "' WHERE description = 'amiPassword'");
            statement.executeUpdate(query + ldapAdress + "' WHERE description = 'ldapAdress'");
            statement.executeUpdate(query + ldapServerPort + "' WHERE description = 'ldapServerPort'");
            statement.executeUpdate(query + ldapSearchBase + "' WHERE description = 'ldapSearchBase'");
            statement.executeUpdate(query + ldapBase + "' WHERE description = 'ldapBase'");
            statement.executeUpdate(query + ldapSearchAmount + "' WHERE description = 'ldapSearchAmount'");

            int i = 0;
            statement.execute("Delete from Settings where description LIKE '%ldapField%'");
            int max;
            try (ResultSet res = statement.executeQuery("SELECT * FROM Settings ORDER BY id DESC LIMIT 1")) {
                max = res.getInt("id");
            }
            ++max;
            for (String[] s : ldapFields) {
                statement.execute("insert into settings values(" + max + ", '" + s[0] + ";" + s[1] + "', 'ldapField" + i + "')");
                ++max;
                ++i;
            }

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
            readLdapFields(query, con);
            try (PreparedStatement ptsm = con.prepareStatement(query)) {
                ptsm.setString(1, "ownExtension");
                ResultSet amiAdressRS = ptsm.executeQuery();
                ownExtension = !amiAdressRS.next() ? "201" : amiAdressRS.getString(SETTING);
            }
            try (PreparedStatement ptsm = con.prepareStatement(query)) {
                ptsm.setString(1, "time");
                ResultSet amiAdressRS = ptsm.executeQuery();
                time = Long.valueOf(!amiAdressRS.next() ? Long.toString(System.currentTimeMillis()) : amiAdressRS.getString(SETTING));
            }

            readinLdapFields();
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
            ptsm.setString(1, "amiAdress");
            ResultSet amiAdressRS = ptsm.executeQuery();
            amiAdress = !amiAdressRS.next() ? "localhost" : amiAdressRS.getString(SETTING);
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "amiServerPort");
            ResultSet amiAdressRS = ptsm.executeQuery();
            amiServerPort = Integer.valueOf(!amiAdressRS.next() ? "12350" : amiAdressRS.getString(SETTING));
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "amiLogIn");
            ResultSet amiAdressRS = ptsm.executeQuery();
            amiLogIn = !amiAdressRS.next() ? "admin" : amiAdressRS.getString(SETTING);
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "amiPassword");
            ResultSet amiAdressRS = ptsm.executeQuery();
            amiPassword = !amiAdressRS.next() ? "" : amiAdressRS.getString(SETTING);
        }
    }

    /**
     * Reads the LDAP settings from the sqlite database
     *
     * @param query
     * @param con
     * @throws SQLException
     */
    private void readLdapFields(String query, Connection con) throws SQLException {
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "ldapAdress");
            ResultSet amiAdressRS = ptsm.executeQuery();
            ldapAdress = !amiAdressRS.next() ? "192.168.178.68" : amiAdressRS.getString(SETTING);
            Logger.getLogger(getClass().getName()).info(ldapAdress);
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "ldapServerPort");
            ResultSet amiAdressRS = ptsm.executeQuery();
            ldapServerPort = Integer.valueOf(!amiAdressRS.next() ? "322" : amiAdressRS.getString(SETTING));
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "ldapSearchBase");
            ResultSet amiAdressRS = ptsm.executeQuery();
            ldapSearchBase = !amiAdressRS.next() ? "cn=ldapDocker23" : amiAdressRS.getString(SETTING);
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "ldapBase");
            ResultSet amiAdressRS = ptsm.executeQuery();
            ldapBase = !amiAdressRS.next() ? "ou=people23" : amiAdressRS.getString(SETTING);
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "ldapSearchAmount");
            ResultSet amiAdressRS = ptsm.executeQuery();
            ldapSearchAmount = Integer.valueOf(!amiAdressRS.next() ? "1023" : amiAdressRS.getString(SETTING));
        }
    }

    /**
     * Reads in the LDAP fields (information eg: mobile, name) the user wants to
     * get from the LDAP Server
     */
    private void readinLdapFields() {
        try (Connection con = DriverManager.getConnection(DATABASE_CONNECTION); Statement statement = con.createStatement()) {
            readinLdapFieldsInner(statement, con);
        } catch (SQLException ex) {
            Logger.getLogger(OptionsStorage.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    /**
     * sub Method of readinLdapFields to increase readability of the code
     *
     * @param statement
     * @param con
     * @throws SQLException
     */
    private void readinLdapFieldsInner(Statement statement, Connection con) throws SQLException {
        statement.setQueryTimeout(10);
        int i = 0;
        String quField = "ldapField";
        while (true) {
            PreparedStatement statement2 = con.prepareStatement("select setting from settings where description = ?");
            statement2.setString(1, quField + i);

            Logger.getLogger(getClass().getName()).log(Level.INFO, "{0}{1}", new Object[]{quField, i});

            try (ResultSet fieldRS = statement2.executeQuery()) {
                if (fieldRS.next()) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Gefunden");
                    String field = fieldRS.getString("setting");
                    ldapFields.add(field.split(";"));
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

    public String getAmiAdress() {
        return amiAdress;
    }

    public void setAmiAdress(String amiAdress) {
        this.amiAdress = amiAdress;
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

    public String getLdapAdress() {
        return ldapAdress;
    }

    public void setLdapAdress(String ldapAdress) {
        this.ldapAdress = ldapAdress;
    }

    public int getLdapServerPort() {
        return ldapServerPort;
    }

    public void setLdapServerPort(int ldapServerPort) {
        this.ldapServerPort = ldapServerPort;
    }

    public String getLdapSearchBase() {
        return ldapSearchBase;
    }

    public void setLdapSearchBase(String ldapSearchBase) {
        this.ldapSearchBase = ldapSearchBase;
    }

    public String getLdapBase() {
        return ldapBase;
    }

    public void setLdapBase(String ldapBase) {
        this.ldapBase = ldapBase;
    }

    public void setAmiAdressTemp(String amiAdressTemp) {
        this.amiAdressTemp = amiAdressTemp;
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

    public void setLdapAdressTemp(String ldapAdressTemp) {
        this.ldapAdressTemp = ldapAdressTemp;
    }

    public void setLdapServerPortTemp(int ldapServerPortTemp) {
        this.ldapServerPortTemp = ldapServerPortTemp;
    }

    public void setLdapSearchBaseTemp(String ldapSearchBaseTemp) {
        this.ldapSearchBaseTemp = ldapSearchBaseTemp;
    }

    public void setLdapBaseTemp(String ldapBaseTemp) {
        this.ldapBaseTemp = ldapBaseTemp;
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

    public int getLdapSearchAmount() {
        return ldapSearchAmount;
    }

    public void setLdapSearchAmount(int ldapSearchAmount) {
        this.ldapSearchAmount = ldapSearchAmount;
    }

    public void setLdapSearchAmountTemp(int ldapSearchAmountTemp) {
        this.ldapSearchAmountTemp = ldapSearchAmountTemp;
    }

    @Override
    public String toString() {
        return "OptionsStorage{" + "amiAdress=" + amiAdress + ", amiServerPort=" + amiServerPort + ", amiLogIn=" + amiLogIn + ", amiPassword=" + amiPassword + ", ldapAdress=" + ldapAdress + ", ldapServerPort=" + ldapServerPort + ", ldapSearchBase=" + ldapSearchBase + ", ldapBase=" + ldapBase + ", ownExtension=" + ownExtension + ", time=" + time + ", amiAdressTemp=" + amiAdressTemp + ", amiServerPortTemp=" + amiServerPortTemp + ", amiLogInTemp=" + amiLogInTemp + ", amiPasswordTemp=" + amiPasswordTemp + ", ldapAdressTemp=" + ldapAdressTemp + ", ldapServerPortTemp=" + ldapServerPortTemp + ", ldapSearchBaseTemp=" + ldapSearchBaseTemp + ", ldapBaseTemp=" + ldapBaseTemp + ", ownExtensionTemp=" + ownExtensionTemp + ", timeTemp=" + timeTemp + '}';
    }

    public List<String[]> getLdapFields() {
        return ldapFields;
    }

    public void setLdapFields(List<String[]> ldapFields) {
        this.ldapFields = (ArrayList<String[]>) ldapFields;
    }

    /**
     * removes temp ldap field. The user removes the fields and they are removed
     * from sqlite database when he hits save. Until that point they are only
     * temp. removed.
     *
     * @param cn
     * @param field
     */
    public void removeFromLdapFieldsTemp(String cn, String field) {

        Iterator<String[]> iter = ldapFieldsTemp.iterator();

        while (iter.hasNext()) {
            String[] kk = iter.next();
            if (kk[0].equals(cn) && kk[1].equals(field)) {
                iter.remove();
                Logger.getLogger(getClass().getName()).info("Remove");
            }
        }
        ldapFieldsTemp.forEach(g -> Logger.getLogger(getClass().getName()).info(g[0]));
    }

    /**
     * * adds temp ldap field. The user adds the fields and they are stored in
     * sqlite databse when he hits save. Until that point they are only temp.
     * saved.
     *
     * @param cn
     * @param field
     * @return
     */
    public boolean addToLdapFieldsTemp(String cn, String field) {
        String[] a = {cn, field};
        for (String[] b : ldapFieldsTemp) {

            if (a[0].equals(b[0]) || a[1].equals(b[1])) {
                Logger.getLogger(getClass().getName()).info("Schon vorhanden");
                return false;
            }

        }
        ldapFieldsTemp.add(a);
        ldapFieldsTemp.forEach(b -> Logger.getLogger(getClass().getName()).log(Level.INFO, "{0} {1}", new Object[]{b[0], b[1]}));
        return true;
    }

}
