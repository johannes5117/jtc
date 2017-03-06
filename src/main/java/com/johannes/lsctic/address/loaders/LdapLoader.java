/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.address.loaders;

import com.johannes.lsctic.FXMLController;
import com.johannes.lsctic.OptionsStorage;
import com.johannes.lsctic.address.AddressBookEntry;
import com.johannes.lsctic.address.AddressBookEntry;
import com.johannes.lsctic.address.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 *
 * @author johannesengler
 */
public class LdapLoader extends AddressLoader{
    private static final String SETTING = "setting";

    
    private String ldapAddress;       //LDAP Server Address
    private int ldapServerPort;      //LDAP Server Port
    private String ldapSearchBase;   //LDAP Suchbasis
    private String ldapBase;         //LDAP Basis
    private int ldapSearchAmount;       //Amount of Entrys that will be loaded 
    private ArrayList<String[]> ldapFields = new ArrayList<>();  // LDAP Felder mit Namen

    
    private Hashtable env;
    private String ldapUrl;
    private DirContext dctx;
    private String base;
    private OptionsStorage storage;

    public LdapLoader(OptionsStorage storage) {
        env = new Hashtable();

        String sp = "com.sun.jndi.ldap.LdapCtxFactory";
        env.put(Context.INITIAL_CONTEXT_FACTORY, sp);

        ldapUrl = "ldap://" + ldapAddress + ":" + ldapServerPort + "/" + ldapSearchBase;

        env.put(Context.PROVIDER_URL, ldapUrl);

        this.storage = storage;

        base = ldapBase;
    }

    public LdapLoader(String serverIp, int port, String dc, String ou, OptionsStorage storage) {
        env = new Hashtable();

        String sp = "com.sun.jndi.ldap.LdapCtxFactory";
        env.put(Context.INITIAL_CONTEXT_FACTORY, sp);

        ldapUrl = "ldap://" + serverIp + ":" + port + "/dc=" + dc;

        env.put(Context.PROVIDER_URL, ldapUrl);

        this.storage = storage;
        base = "ou=" + ou;
    }
    @Override
    public ArrayList<AddressBookEntry> getN(String ein, int n) {
        ArrayList<AddressBookEntry> aus = new ArrayList<>();
        SearchControls sc = new SearchControls();
        String[] attributeFilter = new String[ldapFields.size()];
        Logger.getLogger(getClass().getName()).log(Level.INFO, Arrays.toString(attributeFilter));

        int i = 0;
        String filter = "(|";
        for (String[] s : ldapFields) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, Arrays.toString(s));
            attributeFilter[i] = s[0];
            filter = filter + "(" + s[0] + "=" + ein + "*)";
            ++i;
        }
        filter = filter + ")";
        sc.setReturningAttributes(attributeFilter);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //String filter = "(|(sn="+ein+"*)(sn="+ein+"*)(cn="+ein+"*)(o="+ein+"*))";
        NamingEnumeration results = null;

        dctx = null;
        try {
            dctx = new InitialDirContext(env);
        } catch (NamingException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            results = dctx.search(base, filter, sc);
        } catch (NamingException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            i = 0;
            while (results.hasMore() && i < n) {
                SearchResult sr = (SearchResult) results.next();
                Attributes attrs = sr.getAttributes();

                ArrayList<String> data = new ArrayList<>();

                for (String[] field : ldapFields) {
                    // catch if a ldap field will be not available
                    try {
                        Attribute attr = (Attribute) attrs.get(field[0]);
                        data.add((String) attr.get());
                    } catch (Exception e) {
                        data.add("!Nicht gefunden!");
                    }
                }
                DataSource s = new DataSource();
                s.setDataSource("ldap");
                aus.add(new AddressBookEntry(data, data.get(0),s));
                ++i;
            }
        } catch (NamingException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            dctx.close();
        } catch (NamingException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aus;
    }
    
    public void writeSettings(Connection con, Statement statement){
     
            try {
                statement.setQueryTimeout(10);
                final String query = "UPDATE Settings SET setting = '";
                statement.executeUpdate(query + ldapAddress + "' WHERE description = 'ldapAddress'");
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
                
            }   catch (SQLException ex) {
            Logger.getLogger(LdapLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
             
    }
      
    /**
     * removes temp ldap field. The user removes the fields and they are removed
     * from sqlite database when he hits save. Until that point they are only
     * temp. removed.
     *
     * @param cn
     * @param field
     */
    public void removeFromLdapFields(String cn, String field) {

        Iterator<String[]> iter = ldapFields.iterator();

        while (iter.hasNext()) {
            String[] kk = iter.next();
            if (kk[0].equals(cn) && kk[1].equals(field)) {
                iter.remove();
                Logger.getLogger(getClass().getName()).info("Remove");
            }
        }
        ldapFields.forEach(g -> Logger.getLogger(getClass().getName()).info(g[0]));
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
    public boolean addToLdapFields(String cn, String field) {
        String[] a = {cn, field};
        for (String[] b : ldapFields) {

            if (a[0].equals(b[0]) || a[1].equals(b[1])) {
                Logger.getLogger(getClass().getName()).info("Schon vorhanden");
                return false;
            }

        }
        ldapFields.add(a);
        ldapFields.forEach(b -> Logger.getLogger(getClass().getName()).log(Level.INFO, "{0} {1}", new Object[]{b[0], b[1]}));
        return true;
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
            ptsm.setString(1, "ldapAddress");
            ResultSet amiAddressRS = ptsm.executeQuery();
            ldapAddress = !amiAddressRS.next() ? "localhost" : amiAddressRS.getString(SETTING);
            Logger.getLogger(getClass().getName()).info(ldapAddress);
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "ldapServerPort");
            ResultSet amiAddressRS = ptsm.executeQuery();
            ldapServerPort = Integer.valueOf(!amiAddressRS.next() ? "322" : amiAddressRS.getString(SETTING));
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "ldapSearchBase");
            ResultSet amiAddressRS=  ptsm.executeQuery();
            ldapSearchBase = !amiAddressRS.next() ? "cn=ldapDocker23" : amiAddressRS.getString(SETTING);
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "ldapBase");
            ResultSet amiAddressRS = ptsm.executeQuery();
            ldapBase = !amiAddressRS.next() ? "ou=people23" : amiAddressRS.getString(SETTING);
        }
        try (PreparedStatement ptsm = con.prepareStatement(query)) {
            ptsm.setString(1, "ldapSearchAmount");
            ResultSet amiAddressRS = ptsm.executeQuery();
            ldapSearchAmount = Integer.valueOf(!amiAddressRS.next() ? "1023" : amiAddressRS.getString(SETTING));
        }
    }

    public String getLdapAddress() {
        return ldapAddress;
    }

    public void setLdapAddress(String ldapAddress) {
        this.ldapAddress = ldapAddress;
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

    public int getLdapSearchAmount() {
        return ldapSearchAmount;
    }

    public void setLdapSearchAmount(int ldapSearchAmount) {
        this.ldapSearchAmount = ldapSearchAmount;
    }

    public String getLdapUrl() {
        return ldapUrl;
    }

    public void setLdapUrl(String ldapUrl) {
        this.ldapUrl = ldapUrl;
    }
    
    
}
