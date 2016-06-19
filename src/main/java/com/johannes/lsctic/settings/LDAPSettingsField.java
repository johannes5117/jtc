/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.settings;

import com.johannes.lsctic.OptionsStorage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author johannesengler
 */
public class LDAPSettingsField extends SettingsField {

    public LDAPSettingsField(OptionsStorage storage) {
        super("LDAP", storage);
    }
    @Override
    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        TextField f = new TextField();
        f.setPromptText("IP Adresse (Beispiel: server)");
        f.setText(getStorage().getLdapAdress());
        f.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getStorage().setLdapAdressTemp(newValue);
            }
        });
        final TextField f2 = new TextField();
        f2.setPromptText("Port (Beispiel: 389)");
        f2.setText(""+getStorage().getLdapServerPort());
        f2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    getStorage().setLdapServerPortTemp(Integer.valueOf(newValue));
                } catch (Exception e) {
                    f2.setPromptText("Der Port ist eine Zahl");
                }
            }
        });
        TextField f3 = new TextField();
        f3.setPromptText("Suchbasis (Beispiel: dc=server, dc=com)");
        f3.setText(getStorage().getLdapSearchBase());
        f3.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getStorage().setLdapSearchBaseTemp(newValue);
            }
        });
        TextField f5 = new TextField();
        f5.setText(getStorage().getLdapBase());
        f5.setPromptText("Basis (Beispiel: ou=people");
        f5.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getStorage().setLdapBaseTemp(newValue);
            }
        });
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
