package com.johannes.lsctic.panels.gui.plugins.MysqlPlugin;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johannes.lsctic.panels.gui.plugins.TransparentImageButton;
import com.johannes.lsctic.panels.gui.plugins.PluginDataField;
import com.johannes.lsctic.panels.gui.plugins.PluginSettingsField;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
    private int lastSetting = 0;

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
        l.setStyle("-fx-text-fill: #c3c3c3");
        VBox.setMargin(s, new Insets(5, 0, 0, 0));
        VBox.setMargin(l, new Insets(0, 0, 0, 5));
        int i = 0;
        v.getChildren().addAll(f, f2, f3, s, l);
        VBox vLdapFields = new VBox();
        vLdapFields.setSpacing(3);
        for (PluginDataField g : loader.getStorageTemp().getMysqlFields()) {
            mysqlFields.add(makeAdditionalField(g.getFieldname(), g.getFieldvalue(), vLdapFields,false,i));
            ++i;
        }

        mysqlFields.add(makeAdditionalField("", "", vLdapFields,true,i));

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

    public HBox makeAdditionalField(String a, String b, VBox vLdapFields, boolean add, int num) {
        HBox box = new HBox();
        box.setSpacing(5);
        TextField t1 = new TextField(a);
        t1.setPromptText("Feld");

        TextField t2 = new TextField(b);
        t2.setPromptText("Anzeigename");
        TransparentImageButton but;
        if(add) {
             but = new TransparentImageButton("/pics/add.png");
        } else {
             but = new TransparentImageButton("/pics/remove.png");
        }

        if(!add) {
            but.setOnMouseClicked(event -> {
                HBox b1 = (HBox) but.getParent();
                TextField t11 = (TextField) b1.getChildren().get(0);
                TextField t21 = (TextField) b1.getChildren().get(2);
                loader.getStorageTemp().removeFromMysqlFields(t11.getText(), t21.getText());
                mysqlFields.remove(but.getParent());
                vLdapFields.getChildren().remove(but.getParent());
                hasChanged = true;
            });

        }  else {
            but.setOnMouseClicked(event -> {
                boolean r = loader.getStorageTemp().addToMysqlFields(t1.getText(), t2.getText());
                if (r) {
                    this.makeAdditionalField("", "", vLdapFields, true, num +1);
                } else {
                    t1.setPromptText("already showed");
                    t2.setPromptText("already showed");
                    t1.setText("");
                    t2.setText("");
                }
            });
        }

        String initialResource = "/pics/right-arrow.png";
        if(num == loader.getStorage().getMobile()) {
            initialResource = "/pics/smartphone-call.png";
        } else if(num == loader.getStorage().getTelephone()) {
            initialResource = "/pics/telephone-of-old-design.png";
        }

        TransparentImageButton v = new TransparentImageButton(initialResource,add);
        if(!add) {
            lastSetting = 0;
            v.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                if (loader.getStorageTemp().getMobile() < 0 && lastSetting == 0) {
                    v.setImage(new Image("/pics/smartphone-call.png"));
                    TransparentImageButton view = (TransparentImageButton) event.getSource();
                    loader.getStorageTemp().setMobile(mysqlFields.indexOf(view.getParent()));
                    if (mysqlFields.indexOf(view.getParent()) == loader.getStorageTemp().getTelephone()) {
                        loader.getStorageTemp().unsetTelephone();
                    }
                    lastSetting = 1;
                } else if (loader.getStorageTemp().getTelephone() < 0 && lastSetting == 1) {
                    v.setImage(new Image("/pics/telephone-of-old-design.png"));
                    TransparentImageButton view = (TransparentImageButton) event.getSource();
                    loader.getStorageTemp().setTelephone(mysqlFields.indexOf(view.getParent()));
                    if (mysqlFields.indexOf(view.getParent()) == loader.getStorageTemp().getMobile()) {
                        loader.getStorageTemp().unsetMobile();
                    }
                    lastSetting = 2;
                } else {
                    v.setImage(new Image("/pics/right-arrow.png"));
                    TransparentImageButton view = (TransparentImageButton) event.getSource();
                    if (mysqlFields.indexOf(view.getParent()) == loader.getStorageTemp().getTelephone()) {
                        loader.getStorageTemp().unsetTelephone();
                    }
                    if (mysqlFields.indexOf(view.getParent()) == loader.getStorageTemp().getMobile()) {
                        loader.getStorageTemp().unsetMobile();
                    }
                    if (loader.getStorageTemp().getTelephone() < 0 && loader.getStorageTemp().getMobile() > 0) {
                        lastSetting = 1;
                    } else {
                        lastSetting = 0;
                    }

                }
                event.consume();
            });
        }

        box.setAlignment(Pos.CENTER);

        box.getChildren().addAll(t1, t2, but, v);
        vLdapFields.getChildren().add(box);
        hasChanged = true;
        return box;
    }


}
