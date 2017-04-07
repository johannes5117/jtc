/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.fields;

import com.google.common.eventbus.EventBus;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.CallEvent;
import com.johannes.lsctic.panels.gui.plugins.AddressBookEntry;
import com.johannes.lsctic.panels.gui.plugins.DataSource;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;

/**
 * @author johannesengler
 */
public class AddressField extends VBox {
    private String name;
    private int number;
    private final AddressBookEntry addressBookEntry;
    private ImageView vUpDown;
    private boolean expanded;
    private final ArrayList<String> fieldNames;
    private final EventBus eventBus;
    private final String BORDER_RADIUS = "-fx-border-radius:3px";

    //Todo: Implement count function

    public AddressField(int count, int number, AddressBookEntry addressBookEntry, EventBus eventBus) {
        this.name = addressBookEntry.getName();
        this.number = number;
        this.eventBus = eventBus;
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(12, 12, 12, 12));
        this.setSpacing(3);
        this.setStyle(" -fx-border-color: #FFFFFF; -fx-border-width: 1px;");
        this.setFocusTraversable(true);
        this.expanded = false;
        this.addressBookEntry = addressBookEntry;
        DataSource source = addressBookEntry.getSource();
        this.fieldNames = new ArrayList<>();
        for (String s : source.getAvailableFields()) {
            fieldNames.add(s);
        }

        HBox inner = new HBox();
        inner.setSpacing(5);
        inner.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(inner, Priority.ALWAYS);

        HBox innerinner = new HBox();
        innerinner.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(innerinner, Priority.ALWAYS);

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                handleCollapseExpand();
            }
            AddressField.this.requestFocus();
            event.consume();

        });

        Label a = new Label(name);
        a.setStyle(" -fx-font-size: 12px;  -fx-font-weight: bold;");

        inner.getChildren().add(a);
        this.getChildren().add(inner);
        inner.getChildren().add(innerinner);
        this.focusedProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue) {
                AddressField.this.setStyle("-fx-border-color: #0093ff; -fx-border-width: 1px;");
            } else {
                AddressField.this.setStyle("-fx-border-color: #FFFFFF; -fx-border-width: 1px;");
            }


        });
        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.eventBus.post(new CallEvent(this.getName()));
                event.consume(); // do nothing
            }
        });

        if (fieldNames.contains("Telefon")) {
            Image image = new Image("/pics/phone.png");
            ImageView v = new ImageView(image);
            v.setFitHeight(15);
            v.setFitWidth(15);
            v.setOpacity(0.2);
            v.setStyle(BORDER_RADIUS);

            inner.getChildren().add(v);
            v.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                this.eventBus.post(new CallEvent(this.getName()));
                event.consume();
            });
            v.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                ImageView v15 = (ImageView) event.getSource();
                v15.setOpacity(1);
                event.consume();
            });
            v.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                ImageView v14 = (ImageView) event.getSource();
                v14.setOpacity(0.2);
                event.consume();
            });
        }
        if (fieldNames.contains("Mobil")) {

            Image image = new Image("/pics/mobile.png");
            ImageView v = new ImageView(image);

            v.setFitHeight(15);
            v.setFitWidth(15);
            v.setStyle(BORDER_RADIUS);
            v.setOpacity(0.2);
            inner.getChildren().add(v);

            v.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                this.eventBus.post(new CallEvent(this.getName()));
                event.consume();
            });
            v.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                ImageView v12 = (ImageView) event.getSource();

                v12.setOpacity(1);
                event.consume();
            });
            v.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                ImageView v1 = (ImageView) event.getSource();
                v1.setOpacity(0.2);
                event.consume();
            });
        }
        Image image = new Image("/pics/down.png");
        vUpDown = new ImageView(image);
        vUpDown.setFitHeight(15);
        vUpDown.setFitWidth(15);
        vUpDown.setOpacity(0.2);
        vUpDown.setStyle(BORDER_RADIUS);

        inner.getChildren().add(vUpDown);

        vUpDown.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            handleCollapseExpand();
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

    private void handleCollapseExpand() {
        if (expanded) {
            Image image1 = new Image("/pics/down.png");
            AddressField.this.requestFocus();
            vUpDown.setImage(image1);
            collapse();
        } else {
            Image image1 = new Image("/pics/up.png");
            AddressField.this.requestFocus();
            vUpDown.setImage(image1);
            expand();
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        int i = 0;
        for (String fieldName : fieldNames) {
            HBox h = new HBox();
            h.setSpacing(0);
            Label field = new Label(fieldName + ": ");
            field.setWrapText(true);
            h.getChildren().add(field);
            HBox space = new HBox();
            space.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(space, Priority.ALWAYS);
            h.getChildren().add(space);
            Label value = new Label(addressBookEntry.get(i));
            value.setWrapText(true);
            value.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                Label l = (Label) event.getSource();
                StringSelection stringSelection = new StringSelection(l.getText());
                java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
                event.consume();
            });
            value.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                Label l = (Label) event.getSource();
                l.setStyle("-fx-font-weight: bold;");
                event.consume();
            });
            value.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                Label l = (Label) event.getSource();
                l.setStyle("-fx-font-weight: normal;");
                event.consume();
            });
            h.getChildren().add(value);
            v.getChildren().add(h);
            ++i;
        }

        this.getChildren().add(v);
        expanded = true;
    }

    public void collapse() {
        this.getChildren().remove(this.getChildren().size() - 1);
        expanded = false;
    }

}
