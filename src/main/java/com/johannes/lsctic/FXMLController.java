package com.johannes.lsctic;

import java.awt.Panel;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private VBox panelA;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        panelA.setSpacing(3);
        internField i1 = new internField("Johannes Engler");
        internField i2 = new internField("Manuel Engler");
        internField i3 = new internField("Fred Engler");
        internField i4 = new internField("dannes Engler");
        i1.setBusyInUse();
        i2.setIdle();
        i3.setRinging();
        i4.setNotFoundUnavailable();
        ArrayList<internField> i = new ArrayList(); 
        i.add(i1);
        i.add(i2);
        i.add(i3);
        i.add(i4);
        panelA.getChildren().addAll(i);
        
        System.out.println(i.indexOf(new internField("Fred Engler")));
     

    }    
}
