package com.johannes.lsctic.panels.gui.plugins.MysqlPlugin;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.johannes.lsctic.panels.gui.settings.SettingsField;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johannesengler
 */
public class MysqlSettingsField extends SettingsField {
    private final ArrayList<HBox> mysqlFields;
    private MysqlLoader loader;
    public MysqlSettingsField(MysqlLoader loader) {
        super("MySql");
        this.loader = loader;
        mysqlFields = new ArrayList<>();
    }
    @Override
    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        TextField f = new TextField();
        f.setPromptText("IP Adresse (Beispiel: server)");
        f.setText(loader.getStorage().getServerAddress());
        f.textProperty().addListener((observable, oldValue, newValue) -> loader.getStorageTemp().setServerAddress(newValue));
        final TextField f2 = new TextField();
        f2.setPromptText("Port (Beispiel: 3306)");
        f2.setText(Integer.toString(loader.getStorage().getServerPort()));
        f2.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                loader.getStorageTemp().setServerPort(Integer.valueOf(newValue));
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE,null, e);
            }
        });
        TextField f3 = new TextField();
        f3.setPromptText("Datenbank (Beispiel: database)");
        f3.setText(loader.getStorage().getDatabase());
        f3.textProperty().addListener((observable, oldValue, newValue) -> loader.getStorageTemp().setDatabase(newValue));

      Separator s = new Separator();
      Label l = new Label("Mysql Felder");
      VBox.setMargin(s, new Insets(5,0, 0, 0));
      VBox.setMargin(l, new Insets(0, 0, 0, 5));
      int i = 0;
      v.getChildren().addAll(f,f2,f3,s,l);
        VBox vLdapFields = new VBox();
        vLdapFields.setSpacing(3);
      for(String[] g: loader.getStorage().getMysqlFields()) {
          mysqlFields.add(makeAdditionalField(g[0], g[1],vLdapFields,"X"));
          ++i;
      }

       // Button plus = new Button("Hizufügen");
      //  plus.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

       //     @Override
      //      public void handle(javafx.event.ActionEvent event) {
     //           ldapFields.add(makeAdditionalField("", "",vLdapFields));
        //    }
      //  });

      mysqlFields.add(makeAdditionalField("", "",vLdapFields,"+"));

      v.getChildren().addAll(vLdapFields);

        this.getChildren().add(v);




        super.expand();

    }


    @Override
    public void collapse() {
        this.getChildren().remove(this.getChildren().size()-1);
        super.collapse();
    }
    public HBox makeAdditionalField(String a, String b, VBox vLdapFields, String sign) {
       HBox box = new HBox();
       TextField t1 = new TextField(a);
       t1.setPromptText("Feld");
       Label l = new Label(":");
       TextField t2 = new TextField(b);
       t2.setPromptText("Anzeigename");
       Button but = new Button(sign);
       if("+".equals(sign)) {
           but.setStyle("-fx-font-size:13.5;");
       }
       but.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
           @Override
           public void handle(javafx.event.ActionEvent event) {
               if(("X").equals(but.getText())) {
               HBox b = (HBox) but.getParent();
               TextField t1  = (TextField) b.getChildren().get(0);
               TextField t2  = (TextField) b.getChildren().get(2);
                loader.getStorage().removeFromMysqlFields(t1.getText(), t2.getText());
               mysqlFields.remove(but.getParent());

               vLdapFields.getChildren().remove(but.getParent());
               }else {

                   boolean r = loader.getStorage().addToMysqlFields(t1.getText(), t2.getText());
                   if(r) {
                        makeAdditionalField("", "", vLdapFields,"+");
                        but.setText("X");
                          but.setStyle("-fx-font-size:13");
                   } else {
                       t1.setPromptText("bereits vorhanden");
                       t2.setPromptText("bereits vorhanden");
                       t1.setText("");
                       t2.setText("");
                   }
               }
           }
       });
       box.getChildren().addAll(t1,l, t2,but);
       vLdapFields.getChildren().add(box);

       return box;
    }
}
