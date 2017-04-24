/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.fields;

import com.google.common.eventbus.EventBus;
import com.johannes.lsctic.PhoneNumber;
import com.johannes.lsctic.panels.gui.fields.internevents.AddInternEvent;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.CallEvent;
import com.johannes.lsctic.panels.gui.settings.SettingsFieldButton;
import com.sun.org.apache.bcel.internal.generic.NEW;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author johannesengler
 */
public class NewInternField extends HBox{

    private Button button;
    public NewInternField(EventBus eventBus) {
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(5,3, 5, 1));
        this.setSpacing(4);
        this.setAlignment(Pos.CENTER);
        HBox.setHgrow(this, Priority.ALWAYS);
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            NewInternField.this.requestFocus();
            event.consume();
        });

        TextField name = new TextField();
        name.setPromptText("Name");

        TextField extension = new TextField();
        extension.setPromptText("Intern number");

        button = new SettingsFieldButton("Add");
        button.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            String number = extension.getText();
            PhoneNumber p = new PhoneNumber(true, number, name.getText(), 0);
            eventBus.post(new AddInternEvent(p));
           event.consume();
        });

        this.getChildren().addAll(name,extension,button);

    }


   
    

   
}
