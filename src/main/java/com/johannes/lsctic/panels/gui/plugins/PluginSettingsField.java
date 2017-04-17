/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.plugins;

import com.johannes.lsctic.panels.gui.settings.SettingsField;
import javafx.scene.image.ImageView;

/**
 * @author johannesengler
 */
public abstract class PluginSettingsField extends SettingsField {
    private ImageView vUpDown;
    private boolean expanded;
    private String name;


    public PluginSettingsField(String name) {
        super(name);
        this.name = name;
    }


    public String getName() {
        return this.name;
    }

}
