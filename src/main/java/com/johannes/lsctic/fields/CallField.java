/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.fields;

import java.util.Objects;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 *
 * @author johannesengler
 */
public class CallField extends HBox{
    private int number;
    public CallField(int number) {
        this.number = number;
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(12,12, 12, 12));
        this.setSpacing(3);
        this.setStyle(" -fx-border-color: #FFFFFF; -fx-border-width: 1px;");
        this.setFocusTraversable(true);
     

        HBox inner = new HBox();
        inner.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(inner, Priority.ALWAYS);
        
      
        
       this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                 if(event.getClickCount()==2) {
            System.out.println(CallField.this.getNumber()+" anrufen");
                  
                 }
                 CallField.this.requestFocus();
                 event.consume();
            
            }
        });
       
     
        this.focusedProperty().addListener(new ChangeListener<Boolean> () {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue){
                    CallField.this.setStyle("-fx-border-color: #0093ff; -fx-border-width: 1px;");
                } else {
                    CallField.this.setStyle("-fx-border-color: #FFFFFF; -fx-border-width: 1px;");
                }
 
                 
            }
        });
        this.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

    @Override
    public void handle(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            System.out.println(CallField.this.getNumber()+" anrufen");
            
            event.consume(); // do nothing
        }
    }
        });
        
        

       // p.getChildren().add(v);
        
        Label a = new Label(""+number);
        a.setStyle(" -fx-font-size: 15px;  -fx-font-weight: bold;");
        
        this.getChildren().add(a);
        this.getChildren().add(inner);
        Label b = new Label("ANRUFEN");
        b.setStyle(" -fx-font-size: 12px;");
        this.getChildren().add(b);
        
    }
    
  

 

    public int getNumber() {
        return number;
    }
}
