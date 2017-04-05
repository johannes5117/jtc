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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author johannes
 */
public class DataSourceSettingsField extends SettingsField {

    public DataSourceSettingsField(OptionsStorage storage) {
        super("Datasource", storage);
    }

    @Override
    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        ArrayList<CheckBox> boxes = new ArrayList<>();
        ArrayList<String> activated = getStorage().getActivatedDataSources();

        Logger.getLogger(getClass().getName()).info(super.getStorage().getLoaderRegister().getPluginsFound().toString());

        for (String found : super.getStorage().getLoaderRegister().getPluginsFound()) {
            CheckBox b = new CheckBox(found);
            if(activated.contains(found)) {
                b.selectedProperty().setValue(true);
            } else {
                b.selectedProperty().setValue(false);
            }
            b.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    getStorage().deactivateDatasource(getParent().getAccessibleText());
                }
            });
            boxes.add(b);
        }
        v.getChildren().addAll(boxes);
        this.getChildren().add(v);
        super.expand();
    }

    @Override
    public void collapse() {
        this.getChildren().remove(this.getChildren().size() - 1);
        super.collapse();
    }
}
