/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.deployment;

/**
 *
 * @author johannesengler
 */
public class Intern {
    private String name;
    private int extension;

    public Intern(String name, int extension) {
        this.name = name;
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public int getExtension() {
        return extension;
    }
    
}
