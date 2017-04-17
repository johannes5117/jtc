package com.johannes.lsctic;

import com.google.common.eventbus.EventBus;
import com.johannes.lsctic.amiapi.netty.ServerConnectionHandler;
import com.johannes.lsctic.panels.gui.DataPanelsRegister;
import com.johannes.lsctic.panels.gui.fields.otherevents.StartConnectionEvent;
import com.johannes.lsctic.panels.gui.fields.otherevents.UpdateAddressFieldsEvent;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.CallEvent;
import com.johannes.lsctic.panels.gui.plugins.AddressBookEntry;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class FXMLController implements Initializable {

    @FXML
    private VBox panelA;
    @FXML
    private VBox panelB;
    @FXML
    private VBox panelC;
    @FXML
    private VBox panelD;
    @FXML
    private Button optionAccept;
    @FXML
    private Button optionReject;
    @FXML
    private TextField paneATextIn;
    @FXML
    private TextField paneBTextIn;
    @FXML
    private TabPane tabPane;

    private String quickfireString;
    private DataPanelsRegister dataPanelsRegister;
    private Stage stage;
    private EventBus eventBus;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
        /*stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                paneATextIn.requestFocus();
                paneATextIn.setFocusTraversable(true);
            } else {
                stage.setIconified(true);
                stage.toBack();
            }

        });*/
    }

    public void startApp(EventBus eventBus) {
        this.eventBus = eventBus;

        // Sqlite connection must be established before creating the optionsstorage, because he loads data from sqlite
        SqlLiteConnection sqlLiteConnection = new SqlLiteConnection("settingsAndData.db");

        // creates optionstorage which loads data from sqlite and triggers plugin loading
        OptionsStorage storage = new OptionsStorage(optionAccept, optionReject, panelD, eventBus, sqlLiteConnection);

        // set ownextension
        String ownExtension = storage.getOwnExtension();

        new ServerConnectionHandler(eventBus, ownExtension);
        eventBus.post(new StartConnectionEvent(storage.getAmiAddress(), storage.getAmiServerPort(), storage.getAmiLogIn(), storage.getAmiPasswordHash(), true));

        VBox[] panels = {panelA, panelB, panelC, panelD};
        dataPanelsRegister = new DataPanelsRegister(eventBus, sqlLiteConnection, panels);

        //Initally show 10 first entries in the Addressbook View
        List<AddressBookEntry> ld = storage.getPluginRegister().getResultFromEveryPlugin("", 10);
        dataPanelsRegister.updateAddressFields(new UpdateAddressFieldsEvent(ld));

        //Add listener for number enterd in search field of paneA which will be used as quickdial field for phonenumbers
        paneATextIn.addEventFilter(KeyEvent.KEY_PRESSED, (javafx.scene.input.KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (quickfireString.matches("^[0-9]*$")) {
                    eventBus.post(new CallEvent(quickfireString));
                }
                event.consume();
            }
        });


        // Tooltip that will be used to indicate options for the user input in the search field
        Tooltip customTooltip = new Tooltip();

        //listener to search the intern on new entry
        paneATextIn.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            quickfireString = newValue;
            if (quickfireString.matches("^[0-9]*$") && quickfireString.length() > 0) {
                customTooltip.hide();
                customTooltip.setText("Nummer erkannt. Enter zum wählen");
                paneATextIn.setTooltip(customTooltip);
                customTooltip.setAutoHide(true);
                Point2D p = paneATextIn.localToScene(0.0, 0.0);
                customTooltip.show(paneATextIn, p.getX()
                        + paneATextIn.getScene().getX() + paneATextIn.getScene().getWindow().getX(), p.getY()
                        + paneATextIn.getScene().getY() + paneATextIn.getScene().getWindow().getY() + paneATextIn.getHeight());

            } else {
                customTooltip.hide();
            }
            dataPanelsRegister.updateView(dataPanelsRegister.generateReducedSet(newValue));
        });

        //listener to search in the Address sources
        paneBTextIn.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            List<AddressBookEntry> ld1 = storage.getPluginRegister().getResultFromEveryPlugin(newValue, 10);
            dataPanelsRegister.updateAddressFields(new UpdateAddressFieldsEvent(ld1));
        });
    }
}
