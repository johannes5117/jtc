/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.fields;

import com.johannes.lsctic.LDAPEntry;
import com.johannes.lsctic.OptionsStorage;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author johannesengler
 */
public class LDAPField extends VBox{
    private StackPane p;
    private String name;
    private int count;
    private int number;
    private LDAPEntry ldapEntry;
    private ImageView vUpDown;
    private boolean expanded;
    private OptionsStorage storage;
    private ArrayList<String> fieldNames ;
        public LDAPField(String name, int count, int number , LDAPEntry ldapEntry, OptionsStorage storage) {

        this.name =name;
        this.count = count;
        this.number = number;
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(12,12, 12, 12));
        this.setSpacing(3);
        this.setStyle(" -fx-border-color: #FFFFFF; -fx-border-width: 1px;");
        this.setFocusTraversable(true);
        this.expanded = false;
        this.ldapEntry = ldapEntry;
        this.storage = storage;
        this.fieldNames = new ArrayList<>();
        int i = 0;
        for(String[] s : storage.getLdapFields()) {
            fieldNames.add(s[1]);
            ++i;
        }

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
            System.out.println(LDAPField.this.getName()+" anrufen");
                  
                 }
                 LDAPField.this.requestFocus();
                 event.consume();
            
            }
        });
            
            Label a = new Label(name);
            a.setStyle(" -fx-font-size: 12px;  -fx-font-weight: bold;");
        
        inner.getChildren().add(a);
        this.getChildren().add(inner);
        inner.getChildren().add(innerinner);
        this.focusedProperty().addListener(new ChangeListener<Boolean> () {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue){
                    LDAPField.this.setStyle("-fx-border-color: #0093ff; -fx-border-width: 1px;");
                } else {
                    LDAPField.this.setStyle("-fx-border-color: #FFFFFF; -fx-border-width: 1px;");
                }
 
                 
            }
        });
        this.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

    @Override
    public void handle(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            System.out.println(LDAPField.this.getName()+" anrufen");
            event.consume(); // do nothing
        }
    }
        });
        
        if(fieldNames.contains("Telefon")) {
        Image image = new Image("/pics/phone.png");
        ImageView v = new ImageView(image);
        v.setFitHeight(15);
        v.setFitWidth(15);
        v.setOpacity(0.2);
                v.setStyle("-fx-border-radius:3px");

        inner.getChildren().add(v);
        v.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                ImageView v = (ImageView) event.getSource();
                 System.out.println(LDAPField.this.getName());
                event.consume();
            }
        });
         v.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                                ImageView v = (ImageView) event.getSource();

                v.setOpacity(1);
                event.consume();
            }
        });
        v.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                ImageView v = (ImageView) event.getSource();
                v.setOpacity(0.2);
                event.consume();
            }
        });
        }
          if(fieldNames.contains("Mobil")) {
        
        Image image = new Image("/pics/mobile.png");
        ImageView v = new ImageView(image);
        
        v.setFitHeight(15);
        v.setFitWidth(15);
        v.setStyle("-fx-border-radius:3px");
        v.setOpacity(0.2);
        inner.getChildren().add(v);

        v.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                ImageView v = (ImageView) event.getSource();
                System.out.println(LDAPField.this.getName());

                event.consume();
            }
        });
         v.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                                ImageView v = (ImageView) event.getSource();

                v.setOpacity(1);
                event.consume();
            }
        });
        v.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                ImageView v = (ImageView) event.getSource();
                v.setOpacity(0.2);
                event.consume();
            }
        });
        }
        Image image = new Image("/pics/down.png");
        vUpDown = new ImageView(image);
        vUpDown.setFitHeight(15);
        vUpDown.setFitWidth(15);
        vUpDown.setOpacity(0.2);
                vUpDown.setStyle("-fx-border-radius:3px");

        inner.getChildren().add(vUpDown);

        vUpDown.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(expanded){
                    Image image = new Image("/pics/down.png");
                    LDAPField.this.requestFocus();
                    vUpDown.setImage(image);
                    collapse();
                } else {
                    Image image = new Image("/pics/up.png");
                    LDAPField.this.requestFocus();
                    vUpDown.setImage(image);
                    expand();
                }
                
                event.consume();
            }
        });
         vUpDown.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                                ImageView v = (ImageView) event.getSource();

                v.setOpacity(1);
                event.consume();
            }
        });
        vUpDown.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                ImageView v = (ImageView) event.getSource();
                v.setOpacity(0.2);
                event.consume();
            }
        });
        
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
        for(String name : fieldNames) {
            HBox h = new HBox();
            h.setSpacing(0);
            Label field = new Label(name+ ": ");
            field.setWrapText(true);
            h.getChildren().add(field);
            HBox space = new HBox();
            space.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(space, Priority.ALWAYS);
            h.getChildren().add(space);
            Label value = new Label(ldapEntry.get(i));
            value.setWrapText(true);
            value.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                Label l = (Label) event.getSource();
                StringSelection stringSelection = new StringSelection(l.getText());
                java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
                event.consume();
            }
        });
         value.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                Label l = (Label) event.getSource();
                l.setStyle("-fx-font-weight: bold;");
                event.consume();
            }
        });
        value.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                Label l = (Label) event.getSource();
                l.setStyle("-fx-font-weight: normal;");
                event.consume();
            }
        });
            h.getChildren().add(value);
            v.getChildren().add(h);
            ++i;
        }
        
        this.getChildren().add(v);
        expanded = true;
    }
    public void collapse() {
        this.getChildren().remove(this.getChildren().size()-1);
        expanded = false;
    }
   
}
