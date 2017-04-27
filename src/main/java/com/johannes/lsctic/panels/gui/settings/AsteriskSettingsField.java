/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.settings;

import com.google.common.eventbus.EventBus;
import com.johannes.lsctic.messagestage.PasswordChange;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
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
    private Button changePassword;
    private boolean changed;
    private EventBus bus = null;


    public AsteriskSettingsField(EventBus bus) {
        super("Asterisk (AMI)");
        this.bus = bus;
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
        changePassword = new Button("Change password");
        changePassword.getStyleClass().add("button-ui");
        changePassword.setOnMouseClicked(event -> {
            PasswordChange passwordChange = new PasswordChange(bus, userTextField.getText());
            passwordChange.start();

        });
        changed = false;
    }



    @Override
    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        VBox spaceButton = new VBox();
        spaceButton.setAlignment(Pos.CENTER);
        spaceButton.setPadding(new Insets(5,5,0,5));
        spaceButton.getChildren().addAll(changePassword);
        v.getChildren().addAll(ipTextField, portTextField, userTextField, passwordTextField, spaceButton);
        this.getChildren().add(v);
        super.expand();
    }

    public String[] getOptionsTriggerHasChanged(String[] optionsCompare) {
        String[] options = new String[3];
        options[0] = ipTextField.getText();
        String z = portTextField.getText();
        if (z.matches("^[0-9]*$")) {
            options[1] = z;
        } else {
            options[1] = "5038";
        }
        options[2] = userTextField.getText();

        for (int i = 0; i < options.length; i++) {
            if (!(options[i].equals(optionsCompare[i]))) {
                changed = true;
                break;
            }
        }

        return options;
    }

    public String[] getOptions() {
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
        passwordTextField.setText("");
        return options;
    }

    public void setOptions(String[] options) {
        ipTextField.setText(options[0]);
        portTextField.setText(options[1]);
        userTextField.setText(options[2]);
    }

    public boolean passwordChanged() {
        if(passwordTextField.getText().length()>0) {
            return true;
        } else {
            return false;
        }
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
