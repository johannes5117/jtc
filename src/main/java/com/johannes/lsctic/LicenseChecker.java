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
public class LicenseChecker {
    private OptionsStorage os;

    private long testTimeInMilSecs;
    public LicenseChecker(OptionsStorage os){
        this.os = os;
        long testTimeInDays = 90;
        testTimeInMilSecs = testTimeCalc(testTimeInDays);
    }
    public boolean checkIsValid(){
        if(os.isActivated()) {
            return true;
        }
        else if(os.getTime()+ testTimeInMilSecs < System.currentTimeMillis() ) {
            return true;
        }
        return false;
    }
   

    private long testTimeCalc(long testTimeInDays) {
        return testTimeInDays*24*60*60*1000;
    }
}
