/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 *
 * @author johannesengler
 */
public class OptionsStorage {

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
    private boolean activated;       // Aktiv
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
    private boolean activatedTemp;       // Aktiv
    private long timeTemp;               // TimeStamp
    private ArrayList<String[]> ldapFieldsTemp = new ArrayList<>();  // LDAP Felder mit Namen

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
                amiAdressTemp = amiAdress;
                amiServerPortTemp = amiServerPort;
                amiLogInTemp = amiLogIn;
                amiPasswordTemp = amiPassword;
                ldapAdressTemp = ldapAdress;
                ldapServerPortTemp = ldapServerPort;
                ldapSearchBaseTemp = ldapSearchBase;
                ldapBaseTemp = ldapBase;
                ldapSearchAmountTemp = ldapSearchAmount;
                activatedTemp = activated;
                ownExtensionTemp = ownExtension;
                ldapFieldsTemp.clear();
                for (String[] p : ldapFields) {
                    String[] g = {p[0], p[1]};
                    ldapFieldsTemp.add(g);
                }
            }
        });

        readSettingsFromDatabase();
    }

    public void accept() {
        amiAdress = amiAdressTemp;
        amiServerPort = amiServerPortTemp;
        amiLogIn = amiLogInTemp;
        amiPassword = amiPasswordTemp;
        ldapAdress = ldapAdressTemp;
        ldapServerPort = ldapServerPortTemp;
        ldapSearchBase = ldapSearchBaseTemp;
        ldapBase = ldapBaseTemp;
        ldapSearchAmount = ldapSearchAmountTemp;
        activated = activatedTemp;
        ownExtension = ownExtensionTemp;
        ldapFields.clear();

        for (String[] p : ldapFieldsTemp) {
            String[] g = {p[0], p[1]};
            ldapFields.add(g);
        }
        writeSettingsToDatabase();
    }

    public void writeSettingsToDatabase() {
        try {

            try (Connection con = DriverManager.getConnection("jdbc:sqlite:" + "settingsAndData.db")) {
                Statement statement = con.createStatement();
                statement.setQueryTimeout(10);
                statement.executeUpdate("UPDATE Settings SET setting = '" + amiAdress + "' WHERE description = 'amiAdress'");
                statement.executeUpdate("UPDATE Settings SET Setting = '" + amiServerPort + "' WHERE description = 'amiServerPort'");
                statement.executeUpdate("UPDATE Settings SET Setting = '" + amiLogIn + "' WHERE description = 'amiLogIn'");
                statement.executeUpdate("UPDATE Settings SET Setting = '" + amiPassword + "' WHERE description = 'amiPassword'");
                statement.executeUpdate("UPDATE Settings SET Setting = '" + ldapAdress + "' WHERE description = 'ldapAdress'");
                statement.executeUpdate("UPDATE Settings SET Setting = '" + ldapServerPort + "' WHERE description = 'ldapServerPort'");
                statement.executeUpdate("UPDATE Settings SET Setting = '" + ldapSearchBase + "' WHERE description = 'ldapSearchBase'");
                statement.executeUpdate("UPDATE Settings SET Setting = '" + ldapBase + "' WHERE description = 'ldapBase'");
                statement.executeUpdate("UPDATE Settings SET Setting = '" + ldapSearchAmount + "' WHERE description = 'ldapSearchAmount'");
                
                //   statement.executeUpdate("UPDATE Settings SET Setting = '"+ownExtension+"' WHERE description = 'ownExtension'");
                //   statement.executeUpdate("UPDATE Settings SET Setting = '"+activated+"' WHERE description = 'activated'");
                //   statement.executeUpdate("UPDATE Settings SET Setting = '"+time+"' WHERE description = 'time'");
                int i = 0;
                statement.execute("Delete from Settings where description LIKE '%ldapField%'");
                int max = statement.executeQuery("SELECT * FROM Settings ORDER BY id DESC LIMIT 1").getInt("id");
                ++max;
                for (String[] s : ldapFields) {
                    statement.execute("insert into settings values(" + max + ", '" + s[0] + ";" + s[1] + "', 'ldapField" + i + "')");
                    ++max;
                    ++i;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex + " in writeSEttingsToDatabase in OptionStorage");
        }
    }

    public void readSettingsFromDatabase() {
        try {
            try (Connection con = DriverManager.getConnection("jdbc:sqlite:" + "settingsAndData.db"); Statement statement = con.createStatement()) {
                statement.setQueryTimeout(10);
                
                //Safely read in all Settings. If a setting isnt found a default value will be taken
                ResultSet amiAdressRS = statement.executeQuery("select setting from settings where description = 'amiAdress'");
                amiAdress = ((amiAdressRS.next() == false) ? "localhost" : amiAdressRS.getString("setting"));
                
                ResultSet amiServerPortRS = statement.executeQuery("select setting from settings where description = 'amiServerPort'");
                amiServerPort = Integer.valueOf(((amiServerPortRS.next() == false) ? "12350" : amiServerPortRS.getString("setting")));
                
                ResultSet amiLogInRS = statement.executeQuery("select setting from settings where description = 'amiLogIn'");
                amiLogIn = ((amiLogInRS.next() == false) ? "admin" : amiLogInRS.getString("setting"));
                
                ResultSet amiPasswordRS = statement.executeQuery("select setting from settings where description = 'amiPassword'");
                amiPassword = ((amiPasswordRS.next() == false) ? "" : amiPasswordRS.getString("setting"));
                
                ResultSet ldapAdressRS = statement.executeQuery("select setting from settings where description = 'ldapAdress'");
                ldapAdress = ((ldapAdressRS.next() == false) ? "192.168.178.66" : ldapAdressRS.getString("setting"));
                
                ResultSet ldapServerPortRS = statement.executeQuery("select setting from settings where description = 'ldapServerPort'");
                ldapServerPort = Integer.valueOf(((ldapServerPortRS.next() == false) ? "389" : ldapServerPortRS.getString("setting")));
                
                ResultSet ldapSearchBaseRS = statement.executeQuery("select setting from settings where description = 'ldapSearchBase'");
                ldapSearchBase = ((ldapSearchBaseRS.next() == false) ? "cn=ldapDocker" : ldapSearchBaseRS.getString("setting"));
                
                ResultSet ldapBaseRS = statement.executeQuery("select setting from settings where description = 'ldapBase'");
                ldapBase = ((ldapBaseRS.next() == false) ? "ou=people" : ldapBaseRS.getString("setting"));
                
                ResultSet ldapSearchAmountRS = statement.executeQuery("select setting from settings where description = 'ldapSearchAmount'");
                ldapSearchAmount = Integer.valueOf(((ldapSearchAmountRS.next() == false) ? "10" : ldapSearchAmountRS.getString("setting")));
                
                ResultSet ownExtensionRS = statement.executeQuery("select setting from settings where description = 'ownExtension'");
                ownExtension = ((ownExtensionRS.next() == false) ? "201" : ownExtensionRS.getString("setting"));
                
                ResultSet timeRS = statement.executeQuery("select setting from settings where description = 'time'");
                time = Long.valueOf(((timeRS.next() == false) ? System.currentTimeMillis() + "" : timeRS.getString("setting")));
                
            
                int i = 1;
                String quField = "ldapField=";
                String query = "select setting from settings where description = ?";
                
                while(true) {
                    PreparedStatement statement2 = con.prepareStatement(query);
                    statement2.setString(1, quField + i);
                
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "{0}{1}", new Object[]{quField, i});
                
                    ResultSet fieldRS = statement2.executeQuery();
                    if(fieldRS.next()==true){
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "Gefunden");
                                
                        String field = fieldRS.getString("setting");
                       ldapFields.add(field.split(";"));
                         ++i;
                    } else {
                                                Logger.getLogger(getClass().getName()).log(Level.INFO, "Break");

                        break;
                    }
                }
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
                timeTemp = time;
                
                for (String[] p : ldapFields) {
                    String[] g = {p[0], p[1]};
                    ldapFieldsTemp.add(g);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.toString());
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
        return "OptionsStorage{" + "amiAdress=" + amiAdress + ", amiServerPort=" + amiServerPort + ", amiLogIn=" + amiLogIn + ", amiPassword=" + amiPassword + ", ldapAdress=" + ldapAdress + ", ldapServerPort=" + ldapServerPort + ", ldapSearchBase=" + ldapSearchBase + ", ldapBase=" + ldapBase + ", ownExtension=" + ownExtension + ", time=" + time + ", amiAdressTemp=" + amiAdressTemp + ", amiServerPortTemp=" + amiServerPortTemp + ", amiLogInTemp=" + amiLogInTemp + ", amiPasswordTemp=" + amiPasswordTemp + ", ldapAdressTemp=" + ldapAdressTemp + ", ldapServerPortTemp=" + ldapServerPortTemp + ", ldapSearchBaseTemp=" + ldapSearchBaseTemp + ", ldapBaseTemp=" + ldapBaseTemp + ", ownExtensionTemp=" + ownExtensionTemp + ", activatedTemp=" + activatedTemp + ", timeTemp=" + timeTemp + '}';
    }

    public ArrayList<String[]> getLdapFields() {
        return ldapFields;
    }

    public void setLdapFields(ArrayList<String[]> ldapFields) {
        this.ldapFields = ldapFields;
    }

    public void removeFromLdapFieldsTemp(String cn, String field) {

        Iterator<String[]> iter = ldapFieldsTemp.iterator();

        while (iter.hasNext()) {
            String[] kk = iter.next();
            if (kk[0].equals(cn) & kk[1].equals(field)) {
                iter.remove();
                System.out.println("Remove");
            }
        }
        for (String[] g : ldapFieldsTemp) {
            System.out.println(g[0]);
        }

    }

    public boolean addToLdapFieldsTemp(String cn, String field) {
        System.out.println("HA");
        String[] a = {cn, field};

        for (String[] b : ldapFieldsTemp) {

            if (a[0].equals(b[0]) | a[1].equals(b[1])) {
                System.out.println("Schon vorhanden");
                return false;
            }

        }

        ldapFieldsTemp.add(a);
        for (String[] b : ldapFieldsTemp) {

            System.out.println(b[0] + " " + b[1]);

        }
        return true;
    }

}
