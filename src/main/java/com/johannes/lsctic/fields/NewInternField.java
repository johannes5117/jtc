/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.fields;

import com.johannes.lsctic.FXMLController;
import com.johannes.lsctic.PhoneNumber;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 *
 * @author johannesengler
 */
public class NewInternField extends HBox{
    private ImageView v;
    public NewInternField(FXMLController aThis) {
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(8,8, 8, 8));
        this.setSpacing(8);
        this.setStyle(" -fx-border-color: #FFFFFF; -fx-border-width: 1px;");
        this.setFocusTraversable(true);
        TextField name = new TextField();
        name.setPromptText("Name");
        this.getChildren().add(name);
        TextField extension = new TextField();
        extension.setPromptText("Interne Nummer");
        this.getChildren().add(extension);
        HBox imgBox = new HBox();
        imgBox.setPadding(new Insets(7,1,7,0));
        imgBox.setAlignment(Pos.TOP_CENTER);
        v  = new ImageView(new Image("/pics/plus.png"));
        v.setFitHeight(21);
        v.setFitWidth(21);
        v.setOpacity(0.5);
        imgBox.getChildren().add(v);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(imgBox);
        v.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            String number = extension.getText();
            PhoneNumber p = new PhoneNumber(true, number, name.getText(), 0);
           aThis.addInternAndUpdate(p);
           event.consume();
        });
        v.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event) -> {
           v.setOpacity(0.8);
           event.consume();
        });
        v.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent event) -> {
           v.setOpacity(0.5);
           event.consume();
        });
    }


   
    

   
}
