/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.fields;

import com.google.common.eventbus.EventBus;
import com.johannes.lsctic.panels.gui.fields.callrecordevents.RemoveCdrAndUpdateEvent;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.CallEvent;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author johannesengler
 */
public class HistoryField extends VBox {

    private final String who;
    private final String labelText;
    private final String when;
    private final String howLong;
    private final boolean outgoing;
    private final EventBus eventBus;
    private String name;

    public HistoryField(String who, String when, String howLong, boolean outgoing, EventBus eventBus) {
        this.when = when;
        this.who = who;
        this.howLong = howLong + "min";
        this.outgoing = outgoing;
        this.eventBus = eventBus;
        this.labelText = who + " (not found)";
        buildField();
    }
    public HistoryField(String name, String who, String when, String howLong, boolean outgoing, EventBus eventBus) {
        this.when = when;
        this.name = name;
        this.who = who;
        this.howLong = howLong + "min";
        this.outgoing = outgoing;
        this.eventBus = eventBus;
        this.labelText = who + " ("+name+")";
        buildField();
    }


    public void buildField() {
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(12, 12, 12, 12));
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

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                this.eventBus.post(new CallEvent(this.getWho()));
            }
            HistoryField.this.requestFocus();
            event.consume();

        });

        Label a = new Label(labelText);
        a.getStyleClass().clear();
        a.getStyleClass().add("fields-label");
        Label b = null;
        if (outgoing) {
            b = new Label("Outgoing");
        } else {
            b = new Label("Incoming");
        }
        inner.getChildren().add(a);
        this.getChildren().add(inner);
        inner.getChildren().add(innerinner);
        inner.getChildren().add(b);
        this.focusedProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue) {
                this.setStyle("-fx-border-color: #0093ff; -fx-border-width: 1px;");
            } else {
                this.setStyle("-fx-border-color: #FFFFFF; -fx-border-width: 1px;");
            }

        });
        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.eventBus.post(new CallEvent(HistoryField.this.getWho()));
                event.consume(); // do nothing
            }
        });

        HBox innerLine2 = new HBox();
        innerLine2.setSpacing(5);
        innerLine2.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(innerLine2, Priority.ALWAYS);

        HBox innerinnerLine2 = new HBox();
        innerinnerLine2.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(innerinnerLine2, Priority.ALWAYS);

        Label aLine2 = new Label(when);
        Label bLine2 = new Label(howLong);
        innerLine2.getChildren().add(aLine2);
        this.getChildren().add(innerLine2);
        innerLine2.getChildren().add(innerinnerLine2);
        innerLine2.getChildren().add(bLine2);

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem del = new MenuItem("Löschen");
        MenuItem call = new MenuItem("Anrufen");
        contextMenu.getItems().addAll(del, call);

        del.setOnAction(event -> {
            this.eventBus.post(new RemoveCdrAndUpdateEvent(HistoryField.this));
            contextMenu.hide();
        });
        call.setOnAction(event -> {
            this.eventBus.post(new CallEvent(HistoryField.this.getWho()));
            contextMenu.hide();

        });

        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(HistoryField.this, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });

    }

    public String getWho() {
        return who;
    }

    public String getWhen() {
        return when;
    }

    public String getHowLong() {
        return howLong;
    }

    public boolean isAusEin() {
        return outgoing;
    }

}

