/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.settings;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;

/**
 *
 * @author johannesengler
 */
public class AsteriskSettingsField extends SettingsField {

    public AsteriskSettingsField() {
        super("Asterisk (AMI)");
    }
  
  
    
    @Override
    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        TextField f = new TextField();
        f.setPromptText("IP Adresse (Beispiel: server)");
        
        TextField f1 = new TextField();
        f1.setPromptText("Port (Beispiel: 5038)");
        
        TextField f2 = new TextField();
        f2.setPromptText("AMI Benutzer (Beispiel: user)");
        
        TextField f3 = new TextField();
        f3.setPromptText("AMI Passwort (Beispiel: vogel)");
        
        v.getChildren().addAll(f,f1,f2,f3);
        this.getChildren().add(v);
        super.expand();
        
    }
    @Override
    public void collapse() {
        this.getChildren().remove(this.getChildren().size()-1);
        super.collapse();
    }
}
