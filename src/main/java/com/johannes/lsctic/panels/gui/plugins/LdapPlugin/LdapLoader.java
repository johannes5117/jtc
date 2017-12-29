/*
 * Copyright (c) 2017. Johannes Engler
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.plugins.LdapPlugin;

import com.google.common.eventbus.EventBus;
import com.johannes.lsctic.panels.gui.fields.callrecordevents.FoundCdrNameInDataSourceEvent;
import com.johannes.lsctic.panels.gui.fields.callrecordevents.NotFoundCdrNameInDataSourceEvent;
import com.johannes.lsctic.panels.gui.fields.callrecordevents.SearchDataSourcesForCdrEvent;
import com.johannes.lsctic.panels.gui.plugins.*;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johannesengler
 */
public class LdapLoader implements AddressLoader {
    private static final String SETTING = "setting";

    private LdapLoaderStorage storage;
    private LdapLoaderStorage storageTemp;

    private DataSource source;
    private EventBus eventBus;




    public LdapLoader(DataSource source) {
        this.source = source;
        this.storage = new LdapLoaderStorage();
        this.storageTemp = new LdapLoaderStorage(storage);
    }
    
    @Override
    public ArrayList<AddressBookEntry> getResults(String ein, int n) {
        this.storage.initLdap();

        ArrayList<AddressBookEntry> aus = new ArrayList<>();
        SearchControls sc = new SearchControls();
        String[] attributeFilter = new String[storage.getLdapFields().size()];

        int i = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("(|");
        for (PluginDataField s : storage.getLdapFields()) {
            attributeFilter[i] = s.getFieldname();
            builder.append("(");
            builder.append(s.getFieldname());
            builder.append("=");
            // Search for occurence of sequence user typed (ex: without search fpr Mei wouldnt find user "Harald Meier")
            if (ein.length()>0) {
                builder.append("*");
            }
            builder.append(ein);
            builder.append("*)");
            ++i;
        }
        builder.append(")");
        String filter = builder.toString();
        sc.setReturningAttributes(attributeFilter);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //String filter = "(|(sn="+ein+"*)(sn="+ein+"*)(cn="+ein+"*)(o="+ein+"*))";
        NamingEnumeration results = null;

        DirContext dctx = null;
        try {
            dctx = new InitialDirContext(storage.getEnv());
        } catch (NamingException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        try {
            results = dctx.search(storage.getBase(), filter, sc);
        } catch (NamingException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        try {
            i = 0;
            while (results!=null && results.hasMore() && i < n) {
                SearchResult sr = (SearchResult) results.next();
                Attributes attrs = sr.getAttributes();

                ArrayList<String> data = new ArrayList<>();

                for (PluginDataField field : storage.getLdapFields()) {
                    // catch if a ldap field will be not available
                    try {
                        Attribute attr = (Attribute) attrs.get(field.getFieldname());
                        data.add((String) attr.get());
                    } catch (Exception e) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null,e);
                    }
                }
                aus.add(new AddressBookEntry(data, data.get(0),source));
                ++i;
            }
        } catch (NamingException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        try {
            dctx.close();
        } catch (NamingException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return aus;
    }


    public void resolveNameForNumber(SearchDataSourcesForCdrEvent event, AtomicInteger terminated, AtomicBoolean found) {
        Logger.getLogger(getClass().getName()).info("Searches For: " + event.getWho());
        // TODO: observe if this is a safe way to search only for numbers
        ArrayList<AddressBookEntry> results = getResults(event.getWho(), 1);

        if(!results.isEmpty()) {
            found.set(true);
            Logger.getLogger(getClass().getName()).info("Found: "+results.get(0).getName());
            eventBus.post(new FoundCdrNameInDataSourceEvent(event, results.get(0).getName()));
        }

        if(!found.get()) {
            eventBus.post(new NotFoundCdrNameInDataSourceEvent(event));
        }
        terminated.decrementAndGet();
    }


    @Override
    public void saved() {
        this.storage = new LdapLoaderStorage(this.storageTemp);
        // Update also the datasource -> without that Addressfields wouldnt update by accept
        this.getDataSource().setAvailableFields(this.storage.getLdapFields());
    }

    @Override
    public void discarded() {
        this.storageTemp = new LdapLoaderStorage(this.storage);
    }

    public LdapLoaderStorage getStorageTemp() {
        return storageTemp;
    }

    public LdapLoaderStorage getStorage() {
        return storage;
    }

    @Override
    public DataSource getDataSource() {
        return source;
    }

    @Override
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

}
