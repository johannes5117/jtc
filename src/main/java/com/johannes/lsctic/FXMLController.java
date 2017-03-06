package com.johannes.lsctic;

import com.johannes.lsctic.address.loaders.MySqlLoader;
import com.johannes.lsctic.address.AddressBookEntry;
import com.johannes.lsctic.amiapi.ServerConnectionHandler;
import com.johannes.lsctic.fields.InternField;
import com.johannes.lsctic.fields.HistoryField;
import com.johannes.lsctic.fields.AddressField;
import com.johannes.lsctic.fields.NewInternField;
import com.johannes.lsctic.settings.AsteriskSettingsField;
import com.johannes.lsctic.settings.DataSourceSettingsField;
import com.johannes.lsctic.settings.DeploymentSettingsField;
import com.johannes.lsctic.settings.LDAPSettingsField;
import com.johannes.lsctic.settings.MysqlSettingsField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
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

    private int ownExtension;
    private OptionsStorage storage;
    private HashMap<String, InternField> internFields;
    private Map<String, PhoneNumber> internNumbers;
    private SqlLiteConnection sqlCon;
    private String quickdialString;
    private ServerConnectionHandler somo;
    private ArrayList<HistoryField> hFields;
    private Stage stage;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        // Muss vor dem erstellen des Optionsstorage sein, da ggf. die Datenbank nicht existiert
        sqlCon = new SqlLiteConnection("settingsAndData.db", "dataLocal.db");
        // Optionsstorage erstellen und Daten aus Settingsdatabase laden
        storage = new OptionsStorage(optionAccept, optionReject);
        Tooltip customTooltip = new Tooltip();
        panelA.setSpacing(3);
        panelB.setSpacing(3);
        panelC.setSpacing(3);

        ownExtension = 201;
        internNumbers = sqlCon.getInterns();

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
            somo = new ServerConnectionHandler(internFields, this);
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

        internFields.entrySet().stream().forEach(g -> somo.aboStatusExtension(g.getValue().getNumber()));
        somo.aboCdrExtension(String.valueOf(ownExtension));

        updateAnzeige(new ArrayList<>(internFields.values()));

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
            updateAnzeige(generiereReduziertesSet(internFields, newValue));
        });
    /*    storage.setLdapSearchAmount(10);
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Search Amount: {0}", String.valueOf(storage.getLdapSearchAmount()));
        LDAPController l = new LDAPController(storage);
        ArrayList<LDAPEntry> ld = l.getN("", storage.getLdapSearchAmount());
        updateAddressFields(ld);
        paneBTextIn.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            ArrayList<LDAPEntry> ld1 = l.getN(newValue, storage.getLdapSearchAmount());
            updateLdapFields(ld1);
        });*/
   
        MySqlLoader l = new MySqlLoader();
        updateAddressFields(l.getN("", 9));

        hFields = new ArrayList();

        panelC.getChildren().addAll(hFields);

        panelD.getChildren().addAll(new AsteriskSettingsField(storage), new DeploymentSettingsField(storage), new DataSourceSettingsField(storage), new LDAPSettingsField(storage), new MysqlSettingsField(storage));

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

    private ArrayList<InternField> generiereReduziertesSet(HashMap<String, InternField> internFields, String val) {
        ArrayList<InternField> out = new ArrayList<>();
        internFields.values().stream().filter((f) -> (f.getName().toLowerCase().contains(val.toLowerCase()))).forEachOrdered((f) -> out.add(f));
        return out;
    }

    public void addInternAndUpdate(PhoneNumber p) {
        if (!internFields.containsKey(p.getPhoneNumber())) {
            sqlCon.queryNoReturn("Insert into internfields (number,name,callcount,favorit) values ('" + p.getPhoneNumber() + "','" + p.getName() + "'," + p.getCount() + ",0)");
            internNumbers.put(p.getPhoneNumber(), p);
            internFields.put(p.getPhoneNumber(), new InternField(p.getName(), p.getCount(), p.getPhoneNumber(), this));
            somo.aboStatusExtension(p.getPhoneNumber());
            updateAnzeige(new ArrayList<>(internFields.values()));
        } else {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "There already exists a user with that phonenumber.");
        }
    }

    public void addCdrAndUpdate(HistoryField f) {
        hFields.add(f);
        panelC.getChildren().clear();
        panelC.getChildren().addAll(hFields);
    }
    
    public void removeCdrAndUpdate(HistoryField f) {
        hFields.remove(f);
         panelC.getChildren().clear();
        panelC.getChildren().addAll(hFields);
    }

    public void removeInternAndUpdate(InternField f) {
        sqlCon.queryNoReturn("Delete from internfields where number=" + f.getNumber() + "");
        internFields.remove(f.getNumber(), f);
        internNumbers.remove(f.getNumber());
        somo.deAboStatusExtension(f.getNumber());
        updateAnzeige(new ArrayList<>(internFields.values()));
    }

    private void updateAnzeige(ArrayList<InternField> i) {
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

    public int getOwnExtension() {
        return ownExtension;
    }

    public ServerConnectionHandler getSomo() {
        return somo;
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
