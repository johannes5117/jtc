/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.settings;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

/**
 *
 * @author johannesengler
 */
public class AsteriskSettingsField extends SettingsField {


    private ToolTipTextField ipTextField;
    private ToolTipTextField portTextField;
    private ToolTipTextField userTextField;
    private ToolTipTextField passwordTextField;
    private boolean changed;


    public AsteriskSettingsField() {
        super("Asterisk (AMI)");
        ipTextField = new ToolTipTextField("Address of the server");
        ipTextField.setPromptText("IP Adresse (Beispiel: server)");
        portTextField = new ToolTipTextField("Port on the server");
        portTextField.setPromptText("Port (Beispiel: 5038)");
        portTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("^[0-9]*$")){
                portTextField.setText("Der Port ist eine Zahl");
            }
        });
        userTextField = new ToolTipTextField("Your username");
        userTextField.setPromptText("AMI Benutzer (Beispiel: user)");
        passwordTextField = new ToolTipTextField("Your password");
        passwordTextField.setPromptText("AMI Passwort (Beispiel: vogel)");
        changed = false;
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

    public String[] getOptions(String[] optionsCompare) {
        String[] options = new String[4];
        options[0] = ipTextField.getText();
        String z =portTextField.getText();
        if(z.matches("^[0-9]*$")){
            options[1] = z;
        } else {
            options[1] = "5038";
        }
        options[2] = userTextField.getText();
        options[3] = passwordTextField.getText();

        for(int i = 0; i<options.length; i++) {
           if(!(options[i].equals(optionsCompare[i]))) {
               changed = true;
               break;
           }
        }

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

    public boolean hasChanged(){
        boolean out = changed;
        changed = false;
        return out;
    }
}
