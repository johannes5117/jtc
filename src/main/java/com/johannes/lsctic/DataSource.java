/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;

/**
 *
 * @author johannes
 */
public class DataSource {
    private boolean ldap = false;
    private boolean mysql = false;
    private boolean textFile = false;
    
     public boolean isLdap() {
        return ldap;
    }

    public void setLdap(boolean ldap) {
        this.ldap = ldap;
    }

    public boolean isMysql() {
        return mysql;
    }

    public void setMysql(boolean mysql) {
        this.mysql = mysql;
    }

    public boolean isTextFile() {
        return textFile;
    }

    public void setTextFile(boolean textFile) {
        this.textFile = textFile;
    }
    
    
}
