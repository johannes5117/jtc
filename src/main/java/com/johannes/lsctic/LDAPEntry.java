/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;

/**
 *
 * @author johannesengler
 */
public class LDAPEntry {
    private String cn;
    private String vorname;
    private String nachname;
    private int telephoneNumber;
    private int mobileNumber;
    private String email;
    private String firma;
    private String wohnort;
    private int plz;

    public LDAPEntry(String cn, String vorname, String nachname, int telephoneNumber, int mobileNumber, String email, String firma, String wohnort) {
        this.cn = cn;
        this.vorname = vorname;
        this.nachname = nachname;
        this.telephoneNumber = telephoneNumber;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.firma = firma;
        this.wohnort = wohnort;
        this.plz = plz;
    }

    public String getCn() {
        return cn;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public int getTelephoneNumber() {
        return telephoneNumber;
    }

    public int getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getFirma() {
        return firma;
    }

    public String getWohnort() {
        return wohnort;
    }

    public int getPlz() {
        return plz;
    }
    
    
}
