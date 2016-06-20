package com.johannes.lsctic;


import com.johannes.lsctic.settings.AsteriskSettingsField;
import com.johannes.lsctic.settings.DeploymentSettingsField;
import com.johannes.lsctic.settings.LDAPSettingsField;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


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
    private TabPane tabPane;
    @FXML
    private ScrollPane scrollPaneA;
    
    
    private Map<String, PhoneNumber> internNumbers;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Muss vor dem erstellen des Optionsstorage sein, da ggf. die Datenbank nicht existiert
         SqlLiteConnection s = new SqlLiteConnection("settingsAndData.db", "dataLocal.db");
        // Optionsstorage erstellen und Daten aus Settingsdatabase laden
        OptionsStorage storage = new OptionsStorage(optionAccept, optionReject);
        // Die Lizenz überprüfen, wenn nicht lizensiert beenden.
    //  startUpLicenseCheck(storage);
        
        panelA.setSpacing(3);
        panelB.setSpacing(3);
        panelC.setSpacing(3);
        
        
        internNumbers = new TreeMap<String, PhoneNumber>();
        internNumbers.put("Johannes Engler", new PhoneNumber(true, 0157, "Johannes Engler", 12));
        internNumbers.put("Michael Engler", new PhoneNumber(true, 0157, "Michael Engler", 2));
        internNumbers.put("Fabian Engler", new PhoneNumber(true, 0157, "Fabian Engler", 1));
        internNumbers.put("Fabian Englert", new PhoneNumber(true, 0157, "Fabian Englert", 5));
        internNumbers.put("Manuel Englert", new PhoneNumber(true, 0157, "Manuel Englert", 9));
        internNumbers.put("Stefan Englert", new PhoneNumber(true, 0157, "Stefan Englert", 5));
        internNumbers.put("Felix Englert", new PhoneNumber(true, 0157, "Felix Englert", 5));
        internNumbers.put("Marcel Englert", new PhoneNumber(true, 0157, "Marcel Englert", 5));
        internNumbers.put("Dirk Englert", new PhoneNumber(true, 0157, "Dirk Englert", 5));
        internNumbers.put("Olliver Englert", new PhoneNumber(true, 0157, "Olliver Englert", 15));
        internNumbers.put("Victor Englert", new PhoneNumber(true, 0157, "Victor Englert", 5));
        internNumbers.put("Marco Englert", new PhoneNumber(true, 0157, "Marco Englert", 5));
        internNumbers.put("Gregor Englert", new PhoneNumber(true, 0157, "Gregor Englert", 5));
        
        internNumbers.put("JoZannes Engler", new PhoneNumber(true, 0157, "JohZnnes Engler", 12));
        internNumbers.put("MiZhael Engler", new PhoneNumber(true, 0157, "MichZel Engler", 2));
        internNumbers.put("FaZian Engler", new PhoneNumber(true, 0157, "FabiaZ Engler", 1));
        internNumbers.put("FaZian Englert", new PhoneNumber(true, 0157, "FabiZn Englert", 5));
        internNumbers.put("MaZuel Englert", new PhoneNumber(true, 0157, "ManuZl Englert", 9));
        internNumbers.put("StZfan Englert", new PhoneNumber(true, 0157, "StefZn Englert", 5));
        internNumbers.put("FeZix Englert", new PhoneNumber(true, 0157, "FelixZEnglert", 5));
        internNumbers.put("MaZcel Englert", new PhoneNumber(true, 0157, "MarcZl Englert", 5));
        internNumbers.put("DiZk Englert", new PhoneNumber(true, 0157, "Dirk EZglert", 5));
        internNumbers.put("OlZiver Englert", new PhoneNumber(true, 0157, "OllZver Englert", 15));
        internNumbers.put("ViZtor Englert", new PhoneNumber(true, 0157, "VictZr Englert", 5));
        internNumbers.put("MaZco Englert", new PhoneNumber(true, 0157, "MarcoZEnglert", 5));

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
        
        
        ArrayList<InternField> i = new ArrayList(); 
        for(Map.Entry<String,PhoneNumber> g : internNumbers.entrySet()){
            
            i.add(new InternField(g.getKey(),g.getValue().getCount(),g.getValue().getPhoneNumber()));
        }
        updateAnzeige(i);
            
        paneATextIn.textProperty().addListener(new ChangeListener<String> () {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
              
                updateAnzeige(generiereReduziertesSet(internNumbers, newValue));
                
                 
            }
        });
      

      LDAPController l = new LDAPController("server", 389, "server", "people", storage);
      ArrayList<LDAPEntry> ld = l.getN("", 5);
      
       panelB.getChildren().clear();
      ArrayList<LDAPField> ldapFields = new ArrayList<>();
      for(LDAPEntry ent : ld) { 
        ldapFields.add(new LDAPField(ent.get(0), 2, 123123,ent,storage));
      }
     panelB.getChildren().addAll(ldapFields);
     
     ArrayList<HistoryField> hFields = new ArrayList();
     hFields.add(new HistoryField("Johannes Engler", "Gestern", "10 min", true));
     hFields.add(new HistoryField("Johannes Engler", "12.12.16 15:30", "10 min", true));
     hFields.add(new HistoryField("Johannes Engler", "Gestern", "10 min", true));
     hFields.add(new HistoryField("Johannes Engler", "Gestern", "10 min", false));
     hFields.add(new HistoryField("Johannes Engler", "Gestern", "10 min", true));
     hFields.add(new HistoryField("Johannes Engler", "Gestern", "10 min", true));
     hFields.add(new HistoryField("Johannes Engler", "Gestern", "10 min", true));
     hFields.add(new HistoryField("Johannes Engler", "Gestern", "10 min", false));
     hFields.add(new HistoryField("Johannes Engler", "Gestern", "10 min", true));
     hFields.add(new HistoryField("Johannes Engler", "Gestern", "10 min", true));
     hFields.add(new HistoryField("Johannes Engler", "Gestern", "10 min", true));

     panelC.getChildren().addAll(hFields);
     
    
     panelD.getChildren().addAll(new AsteriskSettingsField(storage), new LDAPSettingsField(storage), new DeploymentSettingsField(storage));

    }    
    private void selectTab(int i) {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(i); //select by index starting with 0
        tabPane.setSelectionModel(selectionModel);
        selectionModel.clearSelection(); //clear your selection
    }
    private ArrayList<InternField> generiereReduziertesSet(Map<String, PhoneNumber> internNumbers, String val) {
        ArrayList<InternField> out = new ArrayList<>();
            for(Map.Entry<String,PhoneNumber> g : internNumbers.entrySet()){
            if(g.getKey().toLowerCase().contains(val.toLowerCase())){
             out.add(new InternField(g.getKey(),g.getValue().getCount(),g.getValue().getPhoneNumber()));
            }
        }
        return out;
    }
    private void updateAnzeige(ArrayList<InternField> i) {
                scrollPaneA.setVvalue(0);

         Collections.sort(i, new Comparator<InternField>() {

        public int compare(InternField o1, InternField o2) {
            return o2.getCount()- o1.getCount(); //Sortiert nach Count
          /*  int i = 0;
            while(o1.getName().charAt(i) == o2.getName().charAt(i)) {
                ++i;
                if(i>=o1.getName().length() || i>=o2.getName().length()) {
                return 0;
            }
            }
            
            return o1.getName().charAt(i) - o2.getName().charAt(i);*/ // Nach namen sortieren
        }
            });
        panelA.getChildren().clear();
        panelA.getChildren().addAll(i);
    }
    public void startUpLicenseCheck(OptionsStorage storage){
        LicenseChecker lcheck = new LicenseChecker(storage);
        
        if(lcheck.checkIsValid()) {
            Stage dialogStage = new Stage();
            dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent event) {
                    System.exit(0);
                }
            });
            dialogStage.setAlwaysOnTop(true);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            final Button b = new Button("Beenden");
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.exit(0);
                }
            });
            dialogStage.setScene(new Scene(VBoxBuilder.create().
            children(new Text("Ihre Testlizenz ist leider abgelaufen. Bitte besuchen sie www.cti.eu um das Produkt zu lizenzieren"),b).
             alignment(Pos.CENTER).padding(new Insets(10)).build()));
             dialogStage.show();
             
             Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
        int seconds = 21;
    int i = 0;
    @Override
    public void run()
    {
       i++;

       if(i % seconds == 0) {
           System.exit(0);
    }
       else{
           Platform.runLater(() -> {
               b.setText("Beenden ("+(20-i)+")");
            });    
       }}
            
           
        
    }, 1000, 1000);
        }
    }
     


 
    
   
}
