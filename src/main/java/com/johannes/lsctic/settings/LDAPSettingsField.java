/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.settings;

import com.johannes.lsctic.OptionsStorage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author johannesengler
 */
public class LDAPSettingsField extends SettingsField {
    private ArrayList<HBox> ldapFields;
    public LDAPSettingsField(OptionsStorage storage) {
        super("LDAP", storage);      
        ldapFields = new ArrayList<>();
    }
    @Override
    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        TextField f = new TextField();
        f.setPromptText("IP Adresse (Beispiel: server)");
        f.setText(getStorage().getLdapAdress());
        f.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getStorage().setLdapAdressTemp(newValue);
            }
        });
        final TextField f2 = new TextField();
        f2.setPromptText("Port (Beispiel: 389)");
        f2.setText(""+getStorage().getLdapServerPort());
        f2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    getStorage().setLdapServerPortTemp(Integer.valueOf(newValue));
                } catch (Exception e) {
                    f2.setPromptText("Der Port ist eine Zahl");
                }
            }
        });
        TextField f3 = new TextField();
        f3.setPromptText("Suchbasis (Beispiel: dc=server, dc=com)");
        f3.setText(getStorage().getLdapSearchBase());
        f3.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getStorage().setLdapSearchBaseTemp(newValue);
            }
        });
        TextField f5 = new TextField();
        f5.setText(getStorage().getLdapBase());
        f5.setPromptText("Basis (Beispiel: ou=people");
        f5.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getStorage().setLdapBaseTemp(newValue);
            }
        });
      
      Separator s = new Separator();
      Label l = new Label("LDAP Felder");
      VBox.setMargin(s, new Insets(5,0, 0, 0));
      VBox.setMargin(l, new Insets(0, 0, 0, 5));
      int i = 0;
      v.getChildren().addAll(f,f2,f3,f5,s,l);
        VBox vLdapFields = new VBox();
        vLdapFields.setSpacing(3);
      for(String[] g: getStorage().getLdapFields()) {
          ldapFields.add(makeAdditionalField(g[0], g[1],vLdapFields));
          ++i;
      }
        
        Button plus = new Button("Hizufügen");
        plus.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

            @Override
            public void handle(javafx.event.ActionEvent event) {
                ldapFields.add(makeAdditionalField("", "",vLdapFields));
            }
        });
        
        v.getChildren().addAll(vLdapFields,plus);
        this.getChildren().add(v);
        
        
        
        
        super.expand();
        
    }
    @Override
    public void collapse() {
        this.getChildren().remove(this.getChildren().size()-1);
        super.collapse();
    }
    public HBox makeAdditionalField(String a, String b, VBox vLdapFields) {
       HBox box = new HBox();
       TextField t1 = new TextField(a);
       t1.setPromptText("Feld");
       Label l = new Label(":");
       TextField t2 = new TextField(b);
       t2.setPromptText("Anzeigename");
       Button but = new Button("X");
       but.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
           @Override
           public void handle(javafx.event.ActionEvent event) {
               HBox b = (HBox) but.getParent();
               TextField t1  = (TextField) b.getChildren().get(0);              
               TextField t2  = (TextField) b.getChildren().get(2);
                getStorage().removeFromLdapFieldsTemp(t1.getText(), t2.getText());
               ldapFields.remove(but.getParent());
               
               vLdapFields.getChildren().remove(but.getParent());
           }
       });
       box.getChildren().addAll(t1,l, t2,but);
       vLdapFields.getChildren().add(box);

       return box;
    }
}
