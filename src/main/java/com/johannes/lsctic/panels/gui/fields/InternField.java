/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.fields;

import com.google.common.eventbus.EventBus;
import com.johannes.lsctic.panels.gui.fields.internevents.RemoveInternAndUpdateEvent;
import com.johannes.lsctic.panels.gui.fields.internevents.ReorderDroppedEvent;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.CallEvent;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;
import java.util.logging.Logger;

/**
 *
 * @author johannesengler
 */
public class InternField extends HBox {

    private final String name;
    private int count;
    private int position;
    private final String number;
    private final EventBus eventBus;
    private int state;
    private boolean fromBelow;
    private boolean wasDragged;
    private Label a;
    private Popup popup;

    public InternField(String name, int count,int position, String number, EventBus eventBus) {
        this.name = name;
        this.count = count;
        this.number = number;
        this.position = position;
        this.wasDragged = false;
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(12, 12, 12, 12));
        this.getStyleClass().clear();
        this.getStyleClass().add("intern-box");
        this.setFocusTraversable(true);
        this.eventBus = eventBus;

        state = -1;
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getClickCount() == 2 & event.getButton() == MouseButton.PRIMARY) {
                this.eventBus.post(new CallEvent(InternField.this.getNumber(), true));
            }
            InternField.this.requestFocus();

            event.consume();
        });


        this.addEventFilter(KeyEvent.KEY_PRESSED, (javafx.scene.input.KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.eventBus.post(new CallEvent(InternField.this.getNumber(),true));
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
            this.eventBus.post(new CallEvent(InternField.this.getNumber(),true));
            contextMenu.hide();

        });
        
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Logger.getLogger(getClass().getName()).info(String.valueOf(this.position));
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

        this.setOnDragDetected((event -> {
            this.startFullDrag();
            this.wasDragged = true;
            popup = new Popup();
            popup.show(this.getScene().getWindow());
            popup.getContent().addAll(new InternFieldDragAndDropDummy(name,this.getParent().getBoundsInLocal().getWidth()-this.getPadding().getRight()-this.getPadding().getLeft()));
            Point2D p = this.localToScreen(0.0, 0.0);
            popup.setX(p.getX());
            popup.setY(event.getScreenY()-this.getHeight()/2);
        }));

        this.setOnMouseDragged((event -> {
                if(popup!=null)
                    if(popup.isShowing()) {
                        popup.setY(event.getScreenY() - this.getHeight() / 2);
                    }
        }));

        this.setOnMouseReleased((event -> {
            if(popup!=null) {
                popup.hide();
            }
        }));

        this.setOnMouseDragReleased(event -> {
            //Calculate where the dragged item belongs to
            double sceneY = event.getSceneY();
            Bounds boundsInScene = this.getParent().localToScene(this.getParent().getBoundsInLocal());
            double parent = boundsInScene.getMinY();
            VBox box = (VBox) this.getParent();
            double spacing = box.getSpacing();
            double height = this.getHeight();
            int posCalc = (int) ((sceneY-parent) / (height/2+spacing));
            double divisor = 2;
            int resolvedPosition = (int) Math.ceil((posCalc/divisor));
            Logger.getLogger(getClass().getName()).info("Berechnete position: "+posCalc/divisor);
            Logger.getLogger(getClass().getName()).info("Berechnete position: "+resolvedPosition);
            eventBus.post(new ReorderDroppedEvent(resolvedPosition));
            event.consume();
        });
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
        a.getStyleClass().clear();
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

    public void incCount() {this.count = this.count +1;}

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
                setNotFoundUnavailable();
                break;
            case 8:
                setRinging();
                break;
        }
    }

    public boolean isWasDragged() {
        return wasDragged;
    }

    public void setWasDragged(boolean wasDragged) {
        this.wasDragged = wasDragged;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void hidePopup() {
        this.popup.hide();
    }
}
