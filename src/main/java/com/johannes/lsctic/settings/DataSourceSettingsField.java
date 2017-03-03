/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.settings;

import com.johannes.lsctic.OptionsStorage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author johannes
 */
public class DataSourceSettingsField extends SettingsField{
    
    public DataSourceSettingsField(OptionsStorage storage) {
        super("Datenquelle", storage);
    }
     @Override
    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        CheckBox b = new CheckBox("LDAP");
        b.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                getStorage().getDataSourcesTemp().checkOption(b.getText(),newValue);
            }
        });
        CheckBox b2 = new CheckBox("MySql");

        v.getChildren().addAll(b, b2);
        this.getChildren().add(v);
        super.expand();
    }
    
    @Override
    public void collapse() {
        this.getChildren().remove(this.getChildren().size() - 1);
        super.collapse();
    }
}
