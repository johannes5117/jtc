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
    private String phoneNumber;
    private String name;
    private int count;
    private int position;

    public PhoneNumber(boolean intern, String phoneNumber, String name, int count, int position) {
        this.intern = intern;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.count = count;
        this.position = position;
    }

    public boolean isIntern() {
        return intern;
    }

    public String getPhoneNumber() {
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

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
