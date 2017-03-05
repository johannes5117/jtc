/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;

import java.util.ArrayList;


/**
 *
 * @author johannes
 */
public class MySqlLoader implements AdressBookInterface {

    ArrayList<AdressBookEntry> en = new ArrayList<>();
    
    public MySqlLoader() {
        ArrayList<String> infos = new ArrayList<>();
        infos.add("Test");
        infos.add("Test2");
        DataSource s = new DataSource();
        s.setMysql(true);
        en.add(new AdressBookEntry(infos, "Testname",s));
    }
    
    // TODO implement Funciton
    @Override
    public ArrayList<AdressBookEntry> getN(String ein, int n) {
        return en;
    }
    
}
