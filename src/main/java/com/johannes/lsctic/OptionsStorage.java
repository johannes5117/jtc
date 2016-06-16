/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
    private String ownExtension;     // eigene Extension asterisk
    private boolean activated;       // Aktiv
    
    
    private String amiAdressTemp;        //AMI Server Adresse
    private int amiServerPortTemp;       //AMI Server Port
    private String amiLogInTemp;         //AMI Login
    private String amiPasswordTemp;      //AMI Password
    private String ldapAdressTemp;       //LDAP Server Adresse
    private int ldapServerPortTemp;      //LDAP Server Port
    private String ldapSearchBaseTemp;   //LDAP Suchbasis
    private String ldapBaseTemp;         //LDAP Basis
    private String ownExtensionTemp;     // eigene Extension asterisk
    private boolean activatedTemp;       // Aktiv
    public OptionsStorage(Button accept, Button reject) {
        accept.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                amiAdress = amiAdressTemp;
                amiServerPort = amiServerPortTemp;
                amiLogIn = amiLogInTemp;
                amiPassword = amiPasswordTemp;
                ldapAdress = ldapAdressTemp;
                ldapServerPort = ldapServerPortTemp;
                ldapSearchBase = ldapSearchBaseTemp;
                ldapBase = ldapBaseTemp;
                activated = activatedTemp;
                ownExtension = ownExtensionTemp;
                writeSettingsToDatabase();
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
                activatedTemp = activated;
                ownExtensionTemp = ownExtension;
            }
        });
    }
    
    public void writeSettingsToDatabase() {
                try {
                                    
                    Connection con = DriverManager.getConnection("jdbc:sqlite:"+"settingsAndData.db");

        Statement statement = con.createStatement();
        
        statement.setQueryTimeout(10);        
        statement.executeUpdate("drop table settings");
        statement.executeUpdate("create table settings (id integer, setting string, description string)");

        statement.executeUpdate("insert into settings values(0, '"+amiAdress+"', 'AMI Server Adresse')");
        statement.executeUpdate("insert into settings values(1, '"+amiServerPort+"','AMI Server Port')");
        statement.executeUpdate("insert into settings values(2, '"+amiLogIn+"','AMI Login')");
        statement.executeUpdate("insert into settings values(3, '"+amiPassword+"','AMI Password')");
        // LDAP Optionen
        statement.executeUpdate("insert into settings values(4, '"+ldapAdress+"', 'LDAP Server Adresse')");
        statement.executeUpdate("insert into settings values(5, '"+ldapServerPort+"','LDAP Server Port')");
        statement.executeUpdate("insert into settings values(6, '"+ldapSearchBase+"','LDAP Suchbasis')");
        statement.executeUpdate("insert into settings values(7, '"+ldapBase+"','LDAP Basis')");
        
        statement.executeUpdate("insert into settings values(8, '"+activated+"','Aktiv')");

            statement.executeUpdate("insert into settings values(9, '"+ownExtension+"','ownExtension')");
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex+ " in writeSEttingsToDatabase in OptionStorage");
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

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
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

    public void setActivatedTemp(boolean activatedTemp) {
        this.activatedTemp = activatedTemp;
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
    
}
