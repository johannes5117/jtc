/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;

import java.util.Random;

/**
 *
 * @author johannesengler
 */
public class LicenseVerification {
    private String license;
    public LicenseVerification(String license) {
        this.license = license;
        
      
        
        
    }
    public int getLicenseCount() {
        String aus = "";
        String licenseshort = license.substring(0, 13);
        for(int i =0;i<=licenseshort.length(); i = i +3) {
            aus = aus + licenseshort.charAt(i);
        }
        return Integer.valueOf(aus);
    }
    public boolean checkValid() {
      
        String licenseshort = license.substring(0, 13);
        String licenseshort2 = license.substring(13, license.length());
        StringBuffer aus = new StringBuffer(licenseshort);
        
        for(int i =0;i<=aus.length(); i = i +3) {
            aus.deleteCharAt(i);
            --i;
        }
        return istPrimzahl(Integer.valueOf(aus.toString())-12)& istPrimzahl(Integer.valueOf(licenseshort2)-14);       
        
    }
    
     private boolean istPrimzahl(int p){
      boolean istPrim = true;
      if (p < 2) return false;          
      for (int i=2; i <= Math.sqrt(p); i++){
         if (p%i == 0){
            istPrim = false;
            break;
         }
      }             
      return istPrim;
   }
    
    private int randInt(int min, int max) {

    Random rand = new Random();

    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
}
    
}
