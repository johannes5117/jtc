/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.settings;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * @author johannes
 */
public class DataSourceSettingsField extends SettingsField {

    private ArrayList<CheckBox> checkBoxes;

    public DataSourceSettingsField() {
        super("Datasource");
        checkBoxes = new ArrayList<>();
    }

    public void setCheckBoxes(ArrayList<String> foundList, ArrayList<String> activatedList) {
        checkBoxes.clear();
        for (String found : foundList) {
            CheckBox b = new CheckBox(found);
            if(activatedList.contains(found)) {
                b.selectedProperty().setValue(true);
            } else {
                b.selectedProperty().setValue(false);
            }
            checkBoxes.add(b);
        }
    }

    public ArrayList<String> getChecked() {
        ArrayList<String> checked = new ArrayList<>();
        for(CheckBox checkBox: checkBoxes){
            if(checkBox.selectedProperty().getValue()) {
                checked.add(checkBox.getText());
            }
        }
        return checked;
    }

    @Override
    public void expand() {
        VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        v.getChildren().addAll(checkBoxes);
        this.getChildren().add(v);
        super.expand();
    }

    @Override
    public void collapse() {
        this.getChildren().remove(this.getChildren().size() - 1);
        super.collapse();
    }


}
