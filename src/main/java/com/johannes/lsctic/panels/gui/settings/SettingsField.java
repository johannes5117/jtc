/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.settings;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * @author johannesengler
 */
public class SettingsField extends VBox {
    private ImageView vUpDown;
    private boolean expanded;
    private String name;



    public SettingsField(String name) {
        this.name = name;
        createBox();
    }

    private void createBox() {
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(12, 12, 12, 12));
        this.setSpacing(3);
        this.setStyle(" -fx-border-color: #FFFFFF; -fx-border-width: 1px;");
        this.setFocusTraversable(true);

        HBox inner = new HBox();
        inner.setSpacing(5);
        inner.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(inner, Priority.ALWAYS);

        HBox innerChild = new HBox();
        innerChild.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(innerChild, Priority.ALWAYS);
        Label a = new Label(name);
        a.setStyle(" -fx-font-size: 12px;  -fx-font-weight: bold;");
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

            SettingsField.this.requestFocus();
            event.consume();

        });
        this.focusedProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue) {
                SettingsField.this.setStyle("-fx-border-color: #0093ff; -fx-border-width: 1px;");
            } else {
                SettingsField.this.setStyle("-fx-border-color: #FFFFFF; -fx-border-width: 1px;");
            }


        });
        inner.getChildren().add(a);
        this.getChildren().add(inner);
        inner.getChildren().add(innerChild);
        Image image = new Image("/pics/down.png");
        vUpDown = new ImageView(image);
        vUpDown.setFitHeight(15);
        vUpDown.setFitWidth(15);
        vUpDown.setOpacity(0.2);
        vUpDown.setStyle("-fx-border-radius:3px");

        inner.getChildren().add(vUpDown);

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            clickAction(true);
            event.consume();
        });

        vUpDown.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            clickAction(false);
            event.consume();
        });
        vUpDown.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            ImageView v = (ImageView) event.getSource();
            v.setOpacity(1);
            event.consume();
        });
        vUpDown.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            ImageView v = (ImageView) event.getSource();
            v.setOpacity(0.2);
            event.consume();
        });
    }

    public void clickAction(boolean fromField) {
        if (expanded) {
            if(!fromField) {
                Image image1 = new Image("/pics/down.png");
                SettingsField.this.requestFocus();
                vUpDown.setImage(image1);
                collapse();
            }
        } else {
            Image image1 = new Image("/pics/up.png");
            SettingsField.this.requestFocus();
            vUpDown.setImage(image1);
            expand();
        }
    }

    public void expand() {
        expanded = true;
    }

    public void collapse() {
        expanded = false;
    }
}