package com.johannes.lsctic.panels.gui.settings;

import javafx.scene.control.Button;
/**
 * Created by johannes on 23.04.17.
 */
public class SettingsFieldButton extends Button {

    public SettingsFieldButton(String text) {
        super(text);
        this.getStyleClass().clear();
        this.getStyleClass().add("button-ui");
    }
}
