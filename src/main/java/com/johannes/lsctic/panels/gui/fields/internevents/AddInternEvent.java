/*
 * Copyright (c) 2017. Johannes Engler
 */

package com.johannes.lsctic.panels.gui.fields.internevents;

import com.johannes.lsctic.PhoneNumber;

/**
 * Created by johannes on 07.04.2017.
 */
public class AddInternEvent {
    private PhoneNumber phoneNumber;

    public AddInternEvent(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneNumber getPhoneNumber(){
        return phoneNumber;
    }
}
