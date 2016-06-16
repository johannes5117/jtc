/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.settings;

import com.johannes.lsctic.OptionsStorage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author johannesengler
 */
public abstract class SettingsField extends VBox {
     private ImageView vUpDown;
    private boolean expanded;
    private OptionsStorage storage;
    public SettingsField(String name, OptionsStorage storage) {
        this.storage = storage;
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(12,12, 12, 12));
        this.setSpacing(3);
        this.setStyle(" -fx-border-color: #FFFFFF; -fx-border-width: 1px;");
        this.setFocusTraversable(true);
        
        HBox inner = new HBox();
        inner.setSpacing(5);
        inner.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(inner, Priority.ALWAYS);
        
        HBox innerinner = new HBox();
        innerinner.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(innerinner, Priority.ALWAYS);
        Label a = new Label(name);
        a.setStyle(" -fx-font-size: 12px;  -fx-font-weight: bold;");
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
              
                 SettingsField.this.requestFocus();
                 event.consume();
            
            }
        });
         this.focusedProperty().addListener(new ChangeListener<Boolean> () {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue){
                    SettingsField.this.setStyle("-fx-border-color: #0093ff; -fx-border-width: 1px;");
                } else {
                    SettingsField.this.setStyle("-fx-border-color: #FFFFFF; -fx-border-width: 1px;");
                }
 
                 
            }
        });
        inner.getChildren().add(a);
        this.getChildren().add(inner);
        inner.getChildren().add(innerinner);
        Image image = new Image("/pics/down.png");
        vUpDown = new ImageView(image);
        vUpDown.setFitHeight(15);
        vUpDown.setFitWidth(15);
        vUpDown.setOpacity(0.2);
                vUpDown.setStyle("-fx-border-radius:3px");

        inner.getChildren().add(vUpDown);

        vUpDown.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(expanded){
                    Image image = new Image("/pics/down.png");
                    SettingsField.this.requestFocus();
                    vUpDown.setImage(image);
                    collapse();
                } else {
                    Image image = new Image("/pics/up.png");
                    SettingsField.this.requestFocus();
                    vUpDown.setImage(image);
                    expand();
                }
                
                event.consume();
            }
        });
         vUpDown.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                                ImageView v = (ImageView) event.getSource();

                v.setOpacity(1);
                event.consume();
            }
        });
        vUpDown.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                ImageView v = (ImageView) event.getSource();
                v.setOpacity(0.2);
                event.consume();
            }
        });
    
}
     public void expand() {
       expanded = true;
     }
    public  void collapse(){
        expanded = false;
    }
    public OptionsStorage getStorage(){
        return storage;
    }
}
