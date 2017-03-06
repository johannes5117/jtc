/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.address.loaders;

import com.johannes.lsctic.OptionsStorage;

/**
 *
 * @author johannes
 */
public class LoaderRegister {
   
    LdapLoader ldap = new LdapLoader();
    public LoaderRegister() {
        
    }
    
   /* public  AddressLoader getLoader(String text, OptionsStorage op) {
        switch(text){
            case("ldap"):
               return 
            case("mysql"):
                return new MySqlLoader(op);
        }
        return null;
    }*/

    public static void addNewLoader(String text) {
        
    }
}
