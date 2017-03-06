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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
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
public class LdapLoader implements AddressLoaderInterface{

    private Hashtable env;
    private String ldapUrl;
    private DirContext dctx;
    private String base;
    private OptionsStorage storage;

    public LdapLoader(OptionsStorage storage) {
        env = new Hashtable();

        String sp = "com.sun.jndi.ldap.LdapCtxFactory";
        env.put(Context.INITIAL_CONTEXT_FACTORY, sp);

        ldapUrl = "ldap://" + storage.getLdapAddress() + ":" + storage.getLdapServerPort() + "/" + storage.getLdapSearchBase();

        env.put(Context.PROVIDER_URL, ldapUrl);

        this.storage = storage;

        base = storage.getLdapBase();
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

    public ArrayList<AddressBookEntry> getN(String ein, int n) {
        ArrayList<AddressBookEntry> aus = new ArrayList<>();
        SearchControls sc = new SearchControls();
        String[] attributeFilter = new String[storage.getLdapFields().size()];
        Logger.getLogger(getClass().getName()).log(Level.INFO, Arrays.toString(attributeFilter));

        int i = 0;
        String filter = "(|";
        for (String[] s : storage.getLdapFields()) {
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

                for (String[] field : storage.getLdapFields()) {
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
}
