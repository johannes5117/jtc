/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.settings;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author johannesengler
 */
public class DeploymentSettingsField extends SettingsField{
    public DeploymentSettingsField() {
        super("Deployment");
    }
  
  
    
    @Override
    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        TextField f = new TextField();
        f.setPromptText("Lizenzcode");
        
        Button b = new Button();
        b.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Geht");
            }
        });
        b.setText("Deployment Datei");
        
        
        Button b2 = new Button();
        b2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Geht auch");
            }
        });
        b2.setText("Start");
        b.setMaxWidth(Double.MAX_VALUE);
        b2.setMaxWidth(Double.MAX_VALUE);
        v.getChildren().addAll(f,b,b2);
        this.getChildren().add(v);
        super.expand();
        
    }
    @Override
    public void collapse() {
        this.getChildren().remove(this.getChildren().size()-1);
        super.collapse();
    }
}
