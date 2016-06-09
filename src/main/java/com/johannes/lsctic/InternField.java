/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;


import java.io.File;
import java.util.Objects;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private StackPane p;
    private int state;
    private String name;
    private int count;
    private int number;
    public InternField(String name, int count, int number) {
        this.name =name;
        this.count = count;
        this.number = number;
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(12,12, 12, 12));
        this.setSpacing(3);
        this.setStyle(" -fx-border-color: #FFFFFF; -fx-border-width: 1px;");
        this.setFocusTraversable(true);
     

        HBox inner = new HBox();
        inner.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(inner, Priority.ALWAYS);
        
        p = new StackPane();
        p.setStyle("-fx-background-color: #FF0000; -fx-background-radius: 6px; -fx-border-width: 6px;");
        p.setPadding(new Insets(1, 1, 1, 1));
        p.setAlignment(Pos.CENTER);
        p.setPrefSize(15, 15);
        
       state = -1;
       this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                 if(event.getClickCount()==2) {
            System.out.println(InternField.this.getName()+" anrufen");
                  
                 }
                 InternField.this.requestFocus();
                 event.consume();
            
            }
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
        this.focusedProperty().addListener(new ChangeListener<Boolean> () {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue){
                    InternField.this.setStyle("-fx-border-color: #0093ff; -fx-border-width: 1px;");
                } else {
                    InternField.this.setStyle("-fx-border-color: #FFFFFF; -fx-border-width: 1px;");
                }
 
                 
            }
        });
        this.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

    @Override
    public void handle(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            System.out.println(InternField.this.getName()+" anrufen");
            event.consume(); // do nothing
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

   
   
}
