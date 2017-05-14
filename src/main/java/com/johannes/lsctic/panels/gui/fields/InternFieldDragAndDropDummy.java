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
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;
import java.util.logging.Logger;

/**
 *
 * @author johannesengler
 */
public class InternFieldDragAndDropDummy extends HBox {

    public InternFieldDragAndDropDummy(String name,double width) {
        this.setOpacity(0.6);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(12, 12, 12, 12));
        this.getStyleClass().clear();
        this.getStyleClass().add("intern-box");
        this.setFocusTraversable(true);
        this.setWidth(width);

        Label label = new Label(name);
        label.setPrefWidth(width);
        label.getStyleClass().clear();
        label.getStyleClass().add("fields-label-notfound");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(label);
    }
}
