package com.johannes.lsctic.panels.gui.plugins;


import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Created by johannes on 24.04.17.
 */
public class MinusFieldButton extends VBox {

    public MinusFieldButton() {
        Image image = new Image("/pics/substract.png");
        this.setStyle("-fx-background-color: #FC74A7; -fx-border-radius: 5px;");
        ImageView v = new ImageView(image);
        v.setFitHeight(15);
        v.setFitWidth(15);
        this.getChildren().add(v);
        this.setAlignment(Pos.CENTER);
    }

}
