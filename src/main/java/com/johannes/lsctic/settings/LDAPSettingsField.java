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
public class LDAPSettingsField extends SettingsField {

    public LDAPSettingsField() {
        super("LDAP");
    }
    @Override
    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        TextField f = new TextField();
        f.setPromptText("IP Adresse (Beispiel: server)");
        
        TextField f2 = new TextField();
        f2.setPromptText("Port (Beispiel: 389)");
        
        TextField f3 = new TextField();
        f3.setPromptText("Suchbasis (Beispiel: dc=server, dc=com)");
        
        TextField f5 = new TextField();
        f5.setPromptText("Basis (Beispiel: ou=people");
        
        v.getChildren().addAll(f,f2,f3,f5);
        this.getChildren().add(v);
        super.expand();
        
    }
    @Override
    public void collapse() {
        this.getChildren().remove(this.getChildren().size()-1);
        super.collapse();
    }
}
