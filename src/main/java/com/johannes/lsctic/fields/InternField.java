/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.fields;

import com.johannes.lsctic.FXMLController;
import java.util.Objects;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

/**
 *
 * @author johannesengler
 */
public class InternField extends HBox {

    private final StackPane p;
    private int state;
    private final String name;
    private final int count;
    private final String number;
    private final FXMLController con;

    public InternField(String name, int count, String number, FXMLController con) {
        this.name = name;
        this.count = count;
        this.number = number;
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(12, 12, 12, 12));
        this.setSpacing(3);
        this.setStyle(" -fx-border-color: #FFFFFF; -fx-border-width: 1px;");
        this.setFocusTraversable(true);
        this.con = con;
        HBox inner = new HBox();
        inner.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(inner, Priority.ALWAYS);

        p = new StackPane();
        p.setStyle("-fx-background-color: #FF0000; -fx-background-radius: 7px; -fx-border-width: 7px;");
        p.setPadding(new Insets(1, 1, 1, 1));
        p.setAlignment(Pos.CENTER);
        p.setPrefSize(14, 14);

        state = -1;
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {

            if (event.getClickCount() == 2 & event.getButton() == MouseButton.PRIMARY) {
                System.out.println(InternField.this.getName() + " anrufen");

            }
            InternField.this.requestFocus();
            event.consume();
        });

        this.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                InternField.this.setStyle("-fx-border-color: #0093ff; -fx-border-width: 1px;");
            } else {
                InternField.this.setStyle("-fx-border-color: #FFFFFF; -fx-border-width: 1px;");
            }
        });
        this.addEventFilter(KeyEvent.KEY_PRESSED, (javafx.scene.input.KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {

           
                    System.out.println(InternField.this.getName() + " anrufen mit dem Anton aus Tirol");
                    InternField.this.call(InternField.this.getNumber()+"");
                    event.consume(); // do nothing
               
            }
        });
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem del = new MenuItem("Löschen");
        MenuItem call = new MenuItem("Anrufen");
        MenuItem num = new MenuItem("Nummer: "+this.getNumber());
        contextMenu.getItems().addAll(del, call, num);

        del.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Lösche");
                con.removeInternAndUpdate(InternField.this);
                                    contextMenu.hide();

            }
        });
        call.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                InternField.this.call(InternField.this.getNumber()+"");
                contextMenu.hide();

            }
        });
        
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(InternField.this, event.getScreenX(), event.getScreenY());
                } else {
                    contextMenu.hide();
                }
            }
        });

        // p.getChildren().add(v);
        Label a = new Label(name);
        a.setStyle(" -fx-font-size: 12px;  -fx-font-weight: bold;");

        this.getChildren().add(a);
        this.getChildren().add(inner);
        this.getChildren().add(p);

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
        p.setStyle("-fx-background-color: #FF0000; -fx-background-radius: 6px; -fx-border-width: 6px;");
    }

    public void setIdle() {
        p.setStyle("-fx-background-color: #00FF00; -fx-background-radius: 6px; -fx-border-width: 6px;");
    }

    public void setNotFoundUnavailable() {
        p.setStyle("-fx-background-color: #0000FF; -fx-background-radius: 6px; -fx-border-width: 6px;");
    }

    public void setRinging() {
        p.setStyle("-fx-background-color: #eeff00; -fx-background-radius: 6px; -fx-border-width: 6px;");
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
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    public int getCount() {
        return count;
    }

    public String getNumber() {
        return number;
    }

    public void setStatus(int status) {
        switch (status) {
            case -1:
                setNotFoundUnavailable();
                break;
            case 0:
                setIdle();
                break;
            case 1:
                setBusyInUse();
                break;
            case 2:
                setBusyInUse();
                break;
            case 4:
                setNotFoundUnavailable();
                break;
            case 8:
                setRinging();
                break;
        }
    }

    private void call(String number) {
        con.getSomo().sendBack("003"+con.getOwnExtension()+":"+number);
   }

}
