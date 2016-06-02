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
public class PhoneNumber {
    private boolean intern;
    private int phoneNumber;
    private String name;
    private int count;

    public PhoneNumber(boolean intern, int phoneNumber, String name, int count) {
        this.intern = intern;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.count = count;
    }

    public boolean isIntern() {
        return intern;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setIntern(boolean intern) {
        this.intern = intern;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public void plusCount() {
        this.count++;
    }
}
