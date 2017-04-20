/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.fields;

import com.google.common.eventbus.EventBus;
import com.johannes.lsctic.panels.gui.fields.internevents.RemoveInternAndUpdateEvent;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.CallEvent;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

import java.util.Objects;

/**
 *
 * @author johannesengler
 */
public class InternField extends HBox {

    private final String name;
    private final int count;
    private final String number;
    private final EventBus eventBus;
    private int state;
    private Label a;

    public InternField(String name, int count, String number, EventBus eventBus) {
        this.name = name;
        this.count = count;
        this.number = number;
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(12, 12, 12, 12));
        this.getStyleClass().clear();
        this.getStyleClass().add("intern-box");
        this.setFocusTraversable(true);
        this.eventBus = eventBus;
        HBox inner = new HBox();
        inner.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(inner, Priority.ALWAYS);

        state = -1;
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getClickCount() == 2 & event.getButton() == MouseButton.PRIMARY) {
                this.eventBus.post(new CallEvent(InternField.this.getNumber()));
            }
            InternField.this.requestFocus();

            event.consume();
        });


        this.addEventFilter(KeyEvent.KEY_PRESSED, (javafx.scene.input.KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.eventBus.post(new CallEvent(InternField.this.getNumber()));
                event.consume(); // do nothing
            }
        });
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem del = new MenuItem("Delete");
        MenuItem call = new MenuItem("Call");
        MenuItem num = new MenuItem("Number: "+this.getNumber());
        contextMenu.getItems().addAll(del, call, num);

        del.setOnAction(event -> {
            this.eventBus.post(new RemoveInternAndUpdateEvent(this.getNumber()));
            contextMenu.hide();

        });
        call.setOnAction(event -> {
            this.eventBus.post(new CallEvent(InternField.this.getNumber()));
            contextMenu.hide();

        });
        
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(InternField.this, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });

        this.a = new Label(name);
        a.getStyleClass().clear();
        a.getStyleClass().add("fields-label-notfound");
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(a);

    }

    public void changeStatus(int status) {
        this.state = status;
        refresh();
    }

    public void refresh() {
        if (state == 1 || state == 2) {
            setBusyInUse();
        } else if (state == 8) {
            setRinging();
        } else if (state == 0) {
            setIdle();
        } else {
            setNotFoundUnavailable();
        }
    }

    public void setBusyInUse() {
        a.getStyleClass().clear();
        a.getStyleClass().add("fields-label-blocked");
    }

    public void setIdle() {
        a.getStyleClass().clear();
        a.getStyleClass().add("fields-label-active");
    }

    public void setNotFoundUnavailable() {

        a.getStyleClass().removeAll();
        a.getStyleClass().add("fields-label-notfound");
    }

    public void setRinging() {
        a.getStyleClass().clear();
        a.getStyleClass().add("fields-label-calling");
    }

    public void setVisisble(boolean value) {
        this.setVisible(value);

    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InternField other = (InternField) obj;
        return Objects.equals(this.name, other.name);
    }

    public int getCount() {
        return count;
    }

    public String getNumber() {
        return number;
    }

    public void setStatus(int status) {
        switch (status) {
            case 0:
                setIdle();
                break;
            case 1:
            case 2:
                setBusyInUse();
                break;
            case -1:
            case 4:
            case 5:
            default:
                setNotFoundUnavailable();
                break;
            case 8:
                setRinging();
                break;
        }
    }

}
