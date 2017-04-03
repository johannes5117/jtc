package com.johannes.lsctic;

import com.johannes.lsctic.address.AddressBookEntry;
import com.johannes.lsctic.address.LoaderRegister;
import com.johannes.lsctic.amiapi.ServerConnectionHandler;
import com.johannes.lsctic.fields.InternField;
import com.johannes.lsctic.fields.HistoryField;
import com.johannes.lsctic.fields.AddressField;
import com.johannes.lsctic.fields.NewInternField;
import com.johannes.lsctic.settings.*;
//import com.johannes.lsctic.settings.LDAPSettingsField;
//import com.johannes.lsctic.settings.MysqlSettingsField;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FXMLController implements Initializable {

    @FXML
    private Label label;
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
    @FXML
    private ScrollPane scrollPaneA;

    private String ownExtension;
    private OptionsStorage storage;
    private HashMap<String, InternField> internFields;
    private Map<String, PhoneNumber> internNumbers;
    private SqlLiteConnection sqlLiteConnection;
    private String quickdialString;
    private ServerConnectionHandler serverConnectionHandler;
    private ArrayList<HistoryField> historyFields;
    private Stage stage;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        // Sqlite connection must be established before creating the optionsstorage, because he loads data from sqlite
        sqlLiteConnection = new SqlLiteConnection("settingsAndData.db", "dataLocal.db");


        // creates optionstorage which loads data from sqlite and triggers plugin loading
        storage = new OptionsStorage(optionAccept, optionReject);




        // Tooltip that will be used to indicate options for the user input in the search field
        Tooltip customTooltip = new Tooltip();

        panelA.setSpacing(3);
        panelB.setSpacing(3);
        panelC.setSpacing(3);


        ownExtension = storage.getOwnExtension();
        internNumbers = sqlLiteConnection.getInterns();

        /*   tabPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {   NICHT LÖSCHEN ERSTER ANSATZ FÜR WEITERE BESCHLEUNIGUNG DES ARBEITENS
        

         @Override
         public void handle(javafx.scene.input.KeyEvent event) {
         if (event.getCode() == KeyCode.I && event.isControlDown()) {
         selectTab(0);
         System.out.println(0);
         event.consume(); 
         } if(event.getCode() == KeyCode.L && event.isControlDown()) {
         selectTab(1);
         System.out.println(1);

         event.consume();
         }  if(event.getCode() == KeyCode.A && event.isControlDown()) {
         selectTab(2);
         System.out.println(2);

         event.consume();
         }  if (event.getCode() == KeyCode.O && event.isControlDown()) {
         selectTab(3);
         System.out.println(3);

         event.consume();
         }
         }
         });*/
        internFields = new HashMap();
        internNumbers.entrySet().stream().forEach(g
                -> internFields.put(g.getKey(), new InternField(g.getValue().getName(), g.getValue().getCount(), g.getKey(), this)));

        try {
            serverConnectionHandler = new ServerConnectionHandler(internFields, this);
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

        internFields.entrySet().stream().forEach(g -> serverConnectionHandler.aboStatusExtension(g.getValue().getNumber()));
        serverConnectionHandler.aboCdrExtension(String.valueOf(ownExtension));

        updateView(new ArrayList<>(internFields.values()));

        paneATextIn.addEventFilter(KeyEvent.KEY_PRESSED, (javafx.scene.input.KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    long l = Long.parseLong(quickdialString);
                    Logger.getLogger(getClass().getName()).log(Level.INFO, null, "Dial: " + l);
                } catch (NumberFormatException e) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, null, e);
                }

                event.consume();
            }
        });

        paneATextIn.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                quickdialString = newValue;
                long quickdial = Long.parseLong(newValue);
                customTooltip.hide();
                customTooltip.setText("Nummer erkannt. Enter zum wählen");
                paneATextIn.setTooltip(customTooltip);
                customTooltip.setAutoHide(true);
                Point2D p = paneATextIn.localToScene(0.0, 0.0);
                customTooltip.show(paneATextIn, p.getX()
                        + paneATextIn.getScene().getX() + paneATextIn.getScene().getWindow().getX(), p.getY()
                        + paneATextIn.getScene().getY() + paneATextIn.getScene().getWindow().getY() + paneATextIn.getHeight());

            } catch (NumberFormatException e) {
                customTooltip.hide();
                Logger.getLogger(getClass().getName()).log(Level.INFO, null, e);
            }
            updateView(generateReducedSet(internFields, newValue));
        });


        List<AddressBookEntry> ld = storage.getLoaderRegister().getResultFromEveryPlugin("", 10);
        updateAddressFields((ArrayList<AddressBookEntry>) ld);
     /*   paneBTextIn.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            ArrayList<LDAPEntry> ld1 = l.getResults(newValue, storage.getLdapSearchAmount());
            updateLdapFields(ld1);
        });*/


        historyFields = new ArrayList();

        panelC.getChildren().addAll(historyFields);

        //Load the standard (which are needed anyway) setting boxes
        panelD.getChildren().addAll(new AsteriskSettingsField(storage), new DeploymentSettingsField(storage), new DataSourceSettingsField(storage));
        panelD.getChildren().addAll(storage.getLoaderRegister().getAllSettingsfields());
        //Load the setting boxes of the plugins
        //TODO: Plugins settings box

        for(String[] as : storage.getDataSourcesTemp().getFields().getFields("mysql")) {
            Logger.getLogger(getClass().getName()).info(Arrays.toString(as));
        }

    }

    private void selectTab(int i) {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(i); //select by index starting with 0
        tabPane.setSelectionModel(selectionModel);
        selectionModel.clearSelection(); //clear your selection
    }

    private ArrayList<InternField> generateReducedSet(HashMap<String, InternField> internFields, String val) {
        ArrayList<InternField> out = new ArrayList<>();
        internFields.values().stream().filter((f) -> (f.getName().toLowerCase().contains(val.toLowerCase()))).forEachOrdered((f) -> out.add(f));
        return out;
    }

    public void addInternAndUpdate(PhoneNumber p) {
        if (!internFields.containsKey(p.getPhoneNumber())) {
            sqlLiteConnection.queryNoReturn("Insert into internfields (number,name,callcount,favorit) values ('" + p.getPhoneNumber() + "','" + p.getName() + "'," + p.getCount() + ",0)");
            internNumbers.put(p.getPhoneNumber(), p);
            internFields.put(p.getPhoneNumber(), new InternField(p.getName(), p.getCount(), p.getPhoneNumber(), this));
            serverConnectionHandler.aboStatusExtension(p.getPhoneNumber());
            updateView(new ArrayList<>(internFields.values()));
        } else {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "There already exists a user with that phonenumber.");
        }
    }

    public void addCdrAndUpdate(HistoryField f) {
        historyFields.add(f);
        panelC.getChildren().clear();
        panelC.getChildren().addAll(historyFields);
    }
    
    public void removeCdrAndUpdate(HistoryField f) {
        historyFields.remove(f);
         panelC.getChildren().clear();
        panelC.getChildren().addAll(historyFields);
    }

    public void removeInternAndUpdate(InternField f) {
        sqlLiteConnection.queryNoReturn("Delete from internfields where number=" + f.getNumber() + "");
        internFields.remove(f.getNumber(), f);
        internNumbers.remove(f.getNumber());
        serverConnectionHandler.deAboStatusExtension(f.getNumber());
        updateView(new ArrayList<>(internFields.values()));
    }

    private void updateView(ArrayList<InternField> i) {
        scrollPaneA.setVvalue(0);
        Collections.sort(i, (InternField o1, InternField o2) -> o2.getCount() - o1.getCount()); //UPDATE: would be nice to choose the sorting 
        panelA.getChildren().clear();
        panelA.getChildren().addAll(i);
        panelA.getChildren().add(new NewInternField(this));
    }

    private void updateAddressFields(ArrayList<AddressBookEntry> i) {
        panelB.getChildren().clear();
        ArrayList<AddressField> addressFields = new ArrayList<>();
        i.stream().forEach(ent -> addressFields.add(new AddressField(ent.get(0), 2, 123123, ent, storage)));
        panelB.getChildren().addAll(addressFields);
    }

    public String getOwnExtension() {
        return ownExtension;
    }

    public ServerConnectionHandler getServerConnectionHandler() {
        return serverConnectionHandler;
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                     paneATextIn.requestFocus();
                     paneATextIn.setFocusTraversable(true);
                } else {
                    stage.setIconified(true);
                    stage.toBack();
                }
               
            }
        });
    }
}
