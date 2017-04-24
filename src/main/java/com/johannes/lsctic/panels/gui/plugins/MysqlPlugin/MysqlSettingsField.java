package com.johannes.lsctic.panels.gui.plugins.MysqlPlugin;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johannes.lsctic.panels.gui.plugins.MinusFieldButton;
import com.johannes.lsctic.panels.gui.plugins.PluginDataField;
import com.johannes.lsctic.panels.gui.plugins.PluginSettingsField;
import com.johannes.lsctic.panels.gui.settings.SettingsFieldButton;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author johannesengler
 */
public class MysqlSettingsField extends PluginSettingsField {
    private final ArrayList<HBox> mysqlFields;
    private final String pluginName;
    private MysqlLoader loader;
    private boolean hasChanged = false;

    public MysqlSettingsField(MysqlLoader loader, String settingsFieldName, String pluginName) {
        super(pluginName);
        this.loader = loader;
        mysqlFields = new ArrayList<>();
        this.pluginName = pluginName;
    }

    @Override
    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        TextField f = new TextField();
        f.setPromptText("IP Adresse (Beispiel: server)");
        f.setText(loader.getStorageTemp().getServerAddress());
        f.textProperty().addListener((observable, oldValue, newValue) -> {
            loader.getStorageTemp().setServerAddress(newValue);
            hasChanged = true;
        });
        final TextField f2 = new TextField();
        f2.setPromptText("Port (Beispiel: 3306)");
        f2.setText(Integer.toString(loader.getStorageTemp().getServerPort()));
        f2.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loader.getStorageTemp().setServerPort(Integer.valueOf(newValue));
                hasChanged = true;
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        });
        TextField f3 = new TextField();
        f3.setPromptText("Datenbank (Beispiel: database)");
        f3.setText(loader.getStorageTemp().getDatabase());
        f3.textProperty().addListener((observable, oldValue, newValue) -> {
            loader.getStorageTemp().setDatabase(newValue);
            hasChanged = true;
        });

        Separator s = new Separator();
        Label l = new Label("Mysql Felder");
        VBox.setMargin(s, new Insets(5, 0, 0, 0));
        VBox.setMargin(l, new Insets(0, 0, 0, 5));
        int i = 0;
        v.getChildren().addAll(f, f2, f3, s, l);
        VBox vLdapFields = new VBox();
        vLdapFields.setSpacing(3);
        for (PluginDataField g : loader.getStorageTemp().getMysqlFields()) {
            mysqlFields.add(makeAdditionalField(g.getFieldname(), g.getFieldvalue(), vLdapFields, "X"));
            ++i;
        }

        // Button plus = new Button("Hizufügen");
        //  plus.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

        //     @Override
        //      public void handle(javafx.event.ActionEvent event) {
        //           ldapFields.add(makeAdditionalField("", "",vLdapFields));
        //    }
        //  });

        mysqlFields.add(makeAdditionalField("", "", vLdapFields, "+"));

        v.getChildren().addAll(vLdapFields);
        this.getChildren().add(v);
        super.expand();
    }


    @Override
    public void collapse() {
        this.getChildren().remove(this.getChildren().size() - 1);
        super.collapse();
    }

    @Override
    public boolean hasChanged() {
        boolean out = hasChanged;
        hasChanged = false;
        return out;
    }

    public HBox makeAdditionalField(String a, String b, VBox vLdapFields, String sign) {
        HBox box = new HBox();
        box.setSpacing(5);
        TextField t1 = new TextField(a);
        t1.setPromptText("Feld");

        TextField t2 = new TextField(b);
        t2.setPromptText("Anzeigename");
        MinusFieldButton but = new MinusFieldButton();
        if ("+".equals(sign)) {
            but.setStyle("-fx-font-size:13.5;");
        }
        but.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                HBox b1 = (HBox) but.getParent();
                TextField t11 = (TextField) b1.getChildren().get(0);
                TextField t21 = (TextField) b1.getChildren().get(2);
                loader.getStorageTemp().removeFromMysqlFields(t11.getText(), t21.getText());
                mysqlFields.remove(but.getParent());
                vLdapFields.getChildren().remove(but.getParent());
                hasChanged = true;
            }
        });
     /*   but.setOnAction(event -> {
            if (("X").equals(but.getText())) {

            } else {
                boolean r = loader.getStorageTemp().addToMysqlFields(t1.getText(), t2.getText(),0);
                if (r) {
                    makeAdditionalField("", "", vLdapFields, "+");
                   // but.setText("X");
                    but.setStyle("-fx-font-size:13");
                } else {
                    t1.setPromptText("bereits vorhanden");
                    t2.setPromptText("bereits vorhanden");
                    t1.setText("");
                    t2.setText("");
                }
            }
        });*/
        Image image = new Image("/pics/plus.png");
        ImageView v = new ImageView(image);
        v.setFitHeight(15);
        v.setFitWidth(15);
        v.setOpacity(0.8);
        v.setStyle("-fx-border-radius:3px");

        v.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

            //TODO Function to determine which field is mobile or phone

            if (loader.getStorageTemp().getMobile() < 0) {
                v.setImage(new Image("/pics/mobile.png"));
                ImageView view = (ImageView) event.getSource();
                loader.getStorageTemp().setMobile(mysqlFields.indexOf(view.getParent()));
            } else if (loader.getStorageTemp().getTelephone() < 0) {
                v.setImage(new Image("/pics/phone.png"));
                ImageView view = (ImageView) event.getSource();
                loader.getStorageTemp().setTelephone(mysqlFields.indexOf(view.getParent()));
            } else {
                v.setImage(new Image("/pics/plus.png"));
            }
            event.consume();
        });
        v.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            ImageView v15 = (ImageView) event.getSource();
            v15.setOpacity(1);
            event.consume();
        });
        v.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            ImageView v14 = (ImageView) event.getSource();
            v14.setOpacity(0.8);
            event.consume();
        });

        box.setAlignment(Pos.CENTER);

        box.getChildren().addAll(t1, t2, but, v);
        vLdapFields.getChildren().add(box);
        hasChanged = true;
        return box;
    }


}
