/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic;


import java.io.File;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;


/**
 *
 * @author johannesengler
 */
public class internField extends HBox{
    private StackPane p;
    private final String name;
    public internField(String name) {
        this.name =name;
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(12,12, 12, 12));
        this.setSpacing(3);
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 5px;");
       
        HBox inner = new HBox();
        inner.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(inner, Priority.ALWAYS);
        
        p = new StackPane();
        p.setStyle("-fx-background-color: #FF0000; -fx-background-radius: 6px; -fx-border-width: 6px;");
        p.setPadding(new Insets(1, 1, 1, 1));
        p.setAlignment(Pos.CENTER);
       
        
        File file = new File("src/main/resources/pics/phone2.jpg");
        Image image = new Image(file.toURI().toString());
        ImageView v = new ImageView(image);
        v.setFitHeight(15);
        v.setFitWidth(15);
        v.setOpacity(0.5);
        p.getChildren().add(v);
        
        Label a = new Label(name);
        a.setStyle(" -fx-font-size: 12px;  -fx-font-weight: bold;");
        
        this.getChildren().add(a);
        this.getChildren().add(inner);
        this.getChildren().add(p);
        
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
        final internField other = (internField) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

   
   
}
