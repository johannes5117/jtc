/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.settings;

import com.johannes.lsctic.OptionsStorage;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;

/**
 *
 * @author johannesengler
 */
public class AsteriskSettingsField extends SettingsField {

    public AsteriskSettingsField(OptionsStorage storage) {
        super("Asterisk (AMI)", storage);
    }

    @Override
    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        TextField f = new TextField();
        f.setPromptText("IP Adresse (Beispiel: server)");
        f.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getStorage().setAmiAdressTemp(newValue);
            }
        });
        final TextField f1 = new TextField();
        f1.setPromptText("Port (Beispiel: 5038)");
        f1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    getStorage().setAmiServerPortTemp(Integer.valueOf(newValue));
                } catch (Exception e) {
                    f1.setPromptText("Der Port ist eine Zahl");
                }
            }
        });
        TextField f2 = new TextField();
        f2.setPromptText("AMI Benutzer (Beispiel: user)");
        f2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getStorage().setAmiLogInTemp(newValue);
            }
        });
        TextField f3 = new TextField();
        f3.setPromptText("AMI Passwort (Beispiel: vogel)");
        f.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getStorage().setAmiPasswordTemp(newValue);
            }
        });
        v.getChildren().addAll(f, f1, f2, f3);
        this.getChildren().add(v);
        super.expand();

    }

    @Override
    public void collapse() {
        this.getChildren().remove(this.getChildren().size() - 1);
        super.collapse();
    }
}
