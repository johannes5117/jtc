/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.fields;


import com.johannes.lsctic.amiapi.Amiapi;
import com.johannes.lsctic.amiapi.ServerConnectionHandler;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
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
public class InternField extends HBox {
    private final StackPane p;
    private int state;
    private final String name;
    private final int count;
    private final int number;
    private Amiapi api;
    public InternField(String name, int count, int number, ServerConnectionHandler somo) {
        this.name =name;
        this.count = count;
        this.number = number;
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(12,12, 12, 12));
        this.setSpacing(3);
        this.setStyle(" -fx-border-color: #FFFFFF; -fx-border-width: 1px;");
        this.setFocusTraversable(true);
        this.api = api;
     

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
           if(event.getClickCount()==2) {
               System.out.println(InternField.this.getName()+" anrufen");
               
           }
           InternField.this.requestFocus();
           event.consume();
        });
       
      /*  File file = new File("src/main/resources/pics/phone2.jpg");
        Image image = new Image(file.toURI().toString());
        ImageView v = new ImageView(image);
        v.setFitHeight(15);
        v.setFitWidth(15);
        v.setOpacity(0.5);
        v.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                ImageView v = (ImageView) event.getSource();
                internField i = (internField)v.getParent().getParent();
                System.out.println(i.getName());

                event.consume();
            }
        });
         v.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                ImageView v = (ImageView) event.getSource();
                StackPane p = (StackPane) v.getParent();
                p.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 6px; -fx-border-width: 6px;");                    
                event.consume();
            }
        });
        v.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                ImageView v = (ImageView) event.getSource();
                internField i = (internField)v.getParent().getParent();
                i.refresh();
                event.consume();
            }
        });*/
        this.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if(newValue){
                InternField.this.setStyle("-fx-border-color: #0093ff; -fx-border-width: 1px;");
            } else {
                InternField.this.setStyle("-fx-border-color: #FFFFFF; -fx-border-width: 1px;");
            }
        });
        this.addEventFilter(KeyEvent.KEY_PRESSED, (javafx.scene.input.KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                
                try {
                    System.out.println(InternField.this.getName()+" anrufen mit dem Anton aus Tirol");
                    api.dial("201", "202");
                    event.consume(); // do nothing
                } catch (IOException ex) {
                    System.out.println("Fehler");
                    Logger.getLogger(InternField.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void changeStatus(int status){
        this.state = status;
        refresh();
    }
    public void refresh() {
        if(state == 1 || state == 2) {
            setBusyInUse();
        } else if(state == 8) {
            setRinging();
        } else if(state == 0) {
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

    public int getNumber() {
        return number;
    }

    public void setStatus(int status) {
        switch (status){
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

   
   
}
