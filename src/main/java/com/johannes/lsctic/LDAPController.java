/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;

import java.util.ArrayList;
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
public class LDAPController {

    private Hashtable env;
    private String ldapUrl;
    private DirContext dctx;
    private String base;

    public LDAPController(String serverIp, int port, String dc, String dc2, String ou) {
        env = new Hashtable();

        String sp = "com.sun.jndi.ldap.LdapCtxFactory";
        env.put(Context.INITIAL_CONTEXT_FACTORY, sp);

        ldapUrl = "ldap://" + serverIp + ":" + port + "/dc=" + dc + " , dc=" + dc2;

        env.put(Context.PROVIDER_URL, ldapUrl);


        base = "ou=" + ou;
    }

    public LDAPController(String serverIp, int port, String dc, String ou) {
        env = new Hashtable();

        String sp = "com.sun.jndi.ldap.LdapCtxFactory";
        env.put(Context.INITIAL_CONTEXT_FACTORY, sp);

        ldapUrl = "ldap://" + serverIp + ":" + port + "/dc=" + dc;

        env.put(Context.PROVIDER_URL, ldapUrl);

        
        base = "ou=" + ou;
    }

    
    public ArrayList<LDAPEntry> getN(String ein, int n) {
        ArrayList<LDAPEntry> aus = new ArrayList<>();
        SearchControls sc = new SearchControls();
        String[] attributeFilter = {"cn", "mail", "sn", "givenName", "l","mobile","telephoneNumber","o" };
        sc.setReturningAttributes(attributeFilter);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String filter = "(|(sn="+ein+"*)(sn="+ein+"*)(cn="+ein+"*)(o="+ein+"*))";

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
            int i = 0;
            while (results.hasMore() && i<n) {
                SearchResult sr = (SearchResult) results.next();
                Attributes attrs = sr.getAttributes();
                
                Attribute attr = (Attribute) attrs.get("cn");
                String cn = (String) attr.get();
                attr = (Attribute) attrs.get("sn");
                String sn = (String) attr.get();
                attr = (Attribute) attrs.get("givenName");
                String givenName = (String) attr.get();
                attr = (Attribute) attrs.get("l");
                String l = (String) attr.get();
                attr = (Attribute) attrs.get("mail");
                String email = (String) attr.get();
                attr = (Attribute) attrs.get("mobile");
                int mobile = Integer.parseInt((String)attr.get());
                attr = (Attribute) attrs.get("telephoneNumber");
                int telephone = Integer.parseInt((String)attr.get());
                attr = (Attribute) attrs.get("o");
                String o = (String) attr.get();
        
                aus.add(new LDAPEntry(cn, givenName, sn, telephone, mobile, email, o, l));
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
