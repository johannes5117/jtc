/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.fields;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author johannesengler
 */
public class HistoryField extends VBox{
    private String who;
    private String when;
    private String howLong;
    private boolean outgoing;
    public HistoryField(String who, String when, String howLong, boolean outgoing){
        this.when=when;
        this.who=who;
        this.howLong=howLong;
        this.outgoing=outgoing;
        
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
        
       this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                 if(event.getClickCount()==2) {
            System.out.println(HistoryField.this.getWho()+" anrufen");
                  
                 }
                 HistoryField.this.requestFocus();
                 event.consume();
            
            }
        });
            
            Label a = new Label(who);
            a.setStyle(" -fx-font-size: 12px;  -fx-font-weight: bold;");
            Label b = null;
            if(outgoing) {
                b = new Label("Ausgehend");
            } else {
                b = new Label("Eingehend");
            }
        inner.getChildren().add(a);
        this.getChildren().add(inner);
        inner.getChildren().add(innerinner);
        inner.getChildren().add(b);
        this.focusedProperty().addListener(new ChangeListener<Boolean> () {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue){
                    HistoryField.this.setStyle("-fx-border-color: #0093ff; -fx-border-width: 1px;");
                } else {
                    HistoryField.this.setStyle("-fx-border-color: #FFFFFF; -fx-border-width: 1px;");
                }
 
                 
            }
        });
        this.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

    @Override
    public void handle(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            System.out.println(HistoryField.this.getWho()+" anrufen");
            event.consume(); // do nothing
        }
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
