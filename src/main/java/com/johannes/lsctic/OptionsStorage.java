/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 *
 * @author johannesengler
 */
public class OptionsStorage {

    private String amiAdress = "";        //AMI Server Adresse
    private int amiServerPort = 0;       //AMI Server Port
    private String amiLogIn = "";         //AMI Login
    private String amiPassword ="";      //AMI Password
    private String ldapAdress = "";       //LDAP Server Adresse
    private int ldapServerPort = 0;      //LDAP Server Port
    private String ldapSearchBase = "";   //LDAP Suchbasis
    private String ldapBase = "";         //LDAP Basis
    private String ownExtension = "";     // eigene Extension asterisk
    private boolean activated =false;       // Aktiv
    private long time = 0;               // TimeStamp
    private ArrayList<String[]> ldapFields = new ArrayList<>();  // LDAP Felder mit Namen

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
                activatedTemp = activated;
                ownExtensionTemp = ownExtension;
                ldapFieldsTemp = ldapFields;
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
                activated = activatedTemp;
                ownExtension = ownExtensionTemp;
                ldapFields = ldapFieldsTemp;
                writeSettingsToDatabase();
    }

    public void writeSettingsToDatabase() {
        try {

            Connection con = DriverManager.getConnection("jdbc:sqlite:" + "settingsAndData.db");

            Statement statement = con.createStatement();
            statement.setQueryTimeout(10);
            statement.executeUpdate("UPDATE Settings SET Setting = '"+amiAdress+"' WHERE description = 'amiAdress'");
            statement.executeUpdate("UPDATE Settings SET Setting = '"+amiServerPort+"' WHERE description = 'amiServerPort'");
            statement.executeUpdate("UPDATE Settings SET Setting = '"+amiLogIn+"' WHERE description = 'amiLogIn'");
            statement.executeUpdate("UPDATE Settings SET Setting = '"+amiPassword+"' WHERE description = 'amiPassword'");
            statement.executeUpdate("UPDATE Settings SET Setting = '"+ldapAdress+"' WHERE description = 'ldapAdress'");
            statement.executeUpdate("UPDATE Settings SET Setting = '"+ldapServerPort+"' WHERE description = 'ldapServerPort'");
            statement.executeUpdate("UPDATE Settings SET Setting = '"+ldapSearchBase+"' WHERE description = 'ldapSearchBase'");
            statement.executeUpdate("UPDATE Settings SET Setting = '"+ldapBase+"' WHERE description = 'ldapBase'");
         //   statement.executeUpdate("UPDATE Settings SET Setting = '"+ownExtension+"' WHERE description = 'ownExtension'");
         //   statement.executeUpdate("UPDATE Settings SET Setting = '"+activated+"' WHERE description = 'activated'");
         //   statement.executeUpdate("UPDATE Settings SET Setting = '"+time+"' WHERE description = 'time'");
            int i = 0;
            for(String[] s : ldapFields) {
                statement.executeUpdate("UPDATE Settings SET Setting = '"+s[0]+";"+s[1]+"' WHERE description = 'ldapField"+i+"'");
            }

            con.close();
        } catch (SQLException ex) {
            System.out.println(ex + " in writeSEttingsToDatabase in OptionStorage");
        }
    }

    public void readSettingsFromDatabase() {
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:" + "settingsAndData.db");
            Statement statement = con.createStatement();
            statement.setQueryTimeout(10);

            amiAdress = statement.executeQuery("select setting from settings where description = 'amiAdress'").getString("setting");
            amiServerPort = Integer.valueOf(statement.executeQuery("select setting from settings where description = 'amiServerPort'").getString("setting"));
            amiLogIn = statement.executeQuery("select setting from settings where description = 'amiLogIn'").getString("setting");
            amiPassword = statement.executeQuery("select setting from settings where description = 'amiPassword'").getString("setting");
            ldapAdress = statement.executeQuery("select setting from settings where description = 'ldapAdress'").getString("setting");
            ldapServerPort = Integer.valueOf(statement.executeQuery("select setting from settings where description = 'ldapServerPort'").getString("setting"));
            ldapSearchBase = statement.executeQuery("select setting from settings where description = 'ldapSearchBase'").getString("setting");
            ldapBase = statement.executeQuery("select setting from settings where description = 'ldapBase'").getString("setting");
            ownExtension = statement.executeQuery("select setting from settings where description = 'ownExtension'").getString("setting");
            activated = Boolean.valueOf(statement.executeQuery("select setting from settings where description = 'activated'").getString("setting"));
            time = Long.valueOf(statement.executeQuery("select setting from settings where description = 'time'").getString("setting"));
      
            String field = null;
            int i = 0;
            field = statement.executeQuery("select setting from settings where description = 'ldapField"+i+"'").getString("setting");
           
            ldapFields.add(field.split(";"));
            System.out.println(ldapFields.get(i)[0]+" und "+ldapFields.get(i)[1]);
            ++i;
            while(field != null) {
                try {
                field = statement.executeQuery("select setting from settings where description = 'ldapField"+i+"'").getString("setting");
                ldapFields.add(field.split(";"));
                System.out.println(ldapFields.get(i)[0]+" und "+ldapFields.get(i)[1]);
                ++i;
                }catch(SQLException e){
                    System.out.println("nicht gefunden");
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
            ownExtensionTemp = ownExtension;
            activatedTemp = activated;
            timeTemp = time;
            
            statement.close();
            con.close();

            /* Funktioniert ist aber nicht schön
            ResultSet rs = statement.executeQuery("select * from settings");
            while (rs.next()) {
                String description = rs.getString("description");
                    if(description.equals("AMI Server Adresse")) {
                        System.out.println("Ja");
                        amiAdress = rs.getString("setting");
                    } else if(description.equals("AMI Server Port")){
                        System.out.println("Ja 2");
                        amiServerPort = Integer.valueOf(rs.getString("setting"));
                    } else if(description.equals("AMI Login")) {
                        System.out.println("Ja 3");
                        amiLogIn = rs.getString("setting");
                    } else if(description.equals("AMI Password")){
                        System.out.println("Ja 4");
                        amiPassword = rs.getString("setting");
                    } else if(description.equals("LDAP Server Adresse")){
                        System.out.println("Ja 5");     
                        ldapAdress = rs.getString("setting");
                    } else if(description.equals("LDAP Server Port")){
                        System.out.println("Ja 6");
                        ldapServerPort = Integer.valueOf(rs.getString("setting"));
                    } else if(description.equals("LDAP Suchbasis")){
                        System.out.println("Ja 7");
                        ldapSearchBase = rs.getString("setting");
                    } else if(description.equals("LDAP Basis")){
                        System.out.println("Ja 8");          
                        ldapBase = rs.getString("setting");
                    } else if(description.equals("Aktiv")){
                        System.out.println("Ja 9");
                        activated = Boolean.valueOf(rs.getString("setting"));
                    } else if(description.equals("Beginn")){
                        System.out.println("Ja 10");
                        time = Long.valueOf(rs.getString("setting"));
                    } else if(description.equals("ownExtension")){
                        System.out.println("Ja 11");
                        ownExtension = rs.getString("setting");
                    }  
                   
            }
             */

        } 
        catch (SQLException ex) {
            System.out.println(ex + " in readSettingsFromDatabase in OptionStorage");
            System.out.println(ex.getMessage());
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTimeTemp(long timeTemp) {
        this.timeTemp = timeTemp;
    }

    @Override
    public String toString() {
        return "OptionsStorage{" + "amiAdress=" + amiAdress + ", amiServerPort=" + amiServerPort + ", amiLogIn=" + amiLogIn + ", amiPassword=" + amiPassword + ", ldapAdress=" + ldapAdress + ", ldapServerPort=" + ldapServerPort + ", ldapSearchBase=" + ldapSearchBase + ", ldapBase=" + ldapBase + ", ownExtension=" + ownExtension + ", activated=" + activated + ", time=" + time + ", amiAdressTemp=" + amiAdressTemp + ", amiServerPortTemp=" + amiServerPortTemp + ", amiLogInTemp=" + amiLogInTemp + ", amiPasswordTemp=" + amiPasswordTemp + ", ldapAdressTemp=" + ldapAdressTemp + ", ldapServerPortTemp=" + ldapServerPortTemp + ", ldapSearchBaseTemp=" + ldapSearchBaseTemp + ", ldapBaseTemp=" + ldapBaseTemp + ", ownExtensionTemp=" + ownExtensionTemp + ", activatedTemp=" + activatedTemp + ", timeTemp=" + timeTemp + '}';
    }

    public ArrayList<String[]> getLdapFields() {
        return ldapFields;
    }

    public void setLdapFields(ArrayList<String[]> ldapFields) {
        this.ldapFields = ldapFields;
    }


    public void removeFromLdapFieldsTemp(String cn, String field) {
        String[] a = {cn, field};
         for(String[] kk : ldapFieldsTemp) {
            System.out.println(kk.toString());
        }
        ldapFieldsTemp.remove(a);
         for(String[] kk : ldapFieldsTemp) {
            System.out.println(kk.toString());
        }
    }
    public boolean addToLdapFieldsTemp(String cn, String field) {
        String[] a = {cn, field};
        for(String[] kk : ldapFieldsTemp) {
            System.out.println(kk.toString());
        }
        if(ldapFieldsTemp.indexOf(a)!=-1) {
            return false;
        } else {
            ldapFieldsTemp.add(a);
            for(String[] kk : ldapFieldsTemp) {
            System.out.println(kk.toString());
        }
            return true;
        }
            
    }
    
}
