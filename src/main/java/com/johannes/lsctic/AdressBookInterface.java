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
public interface AdressBookInterface {
    public ArrayList<AdressBookEntry> getN(String ein, int n);
}
