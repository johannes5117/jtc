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


    private TextField ipTextField;
    private TextField portTextField;
    private TextField userTextField;
    private TextField passwordTextField;


    public AsteriskSettingsField() {
        super("Asterisk (AMI)");
        ipTextField = new TextField();
        ipTextField.setPromptText("IP Adresse (Beispiel: server)");
        portTextField = new TextField();
        portTextField.setPromptText("Port (Beispiel: 5038)");
        portTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Integer.valueOf(newValue);
            } catch (Exception e) {
                portTextField.setPromptText("Der Port ist eine Zahl");
            }
        });
        userTextField = new TextField();
        userTextField.setPromptText("AMI Benutzer (Beispiel: user)");
        passwordTextField = new TextField();
        passwordTextField.setPromptText("AMI Passwort (Beispiel: vogel)");
    }



    @Override
    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        v.getChildren().addAll(ipTextField, portTextField, userTextField, passwordTextField);
        this.getChildren().add(v);
        super.expand();
    }

    public String[] getOptions() {
        String[] options = new String[4];
        options[0] = ipTextField.getText();
        try {
            String z =portTextField.getText();
            Integer.valueOf(z);
            options[1] = z;
        } catch (Exception e) {
            options[1] = "5038";
        }
        options[2] = userTextField.getText();
        options[3] = passwordTextField.getText();
        return options;
    }

    public void setOptions(String[] options) {
        ipTextField.setText(options[0]);
        portTextField.setText(options[1]);
        userTextField.setText(options[2]);
        passwordTextField.setText(options[3]);
    }

    @Override
    public void collapse() {
        this.getChildren().remove(this.getChildren().size() - 1);
        super.collapse();
    }
}
