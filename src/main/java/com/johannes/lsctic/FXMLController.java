package com.johannes.lsctic;


import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private VBox panelA;
    @FXML
    private TextField paneATextIn;
    @FXML
    private TabPane tabPane;
    
    private Map<String, PhoneNumber> internNumbers;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        panelA.setSpacing(3);
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

     /*   tabPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {   NICHT LÖSCHEN ERSTER ANSATZ FÜR WEITERE BESCHLEUNIGUNG DES 

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
        
       
        ArrayList<internField> i = new ArrayList(); 
        for(Map.Entry<String,PhoneNumber> g : internNumbers.entrySet()){
            
            i.add(new internField(g.getKey(),g.getValue().getCount(),g.getValue().getPhoneNumber()));
        }
        updateAnzeige(i);
            
        paneATextIn.textProperty().addListener(new ChangeListener<String> () {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
              
                updateAnzeige(generiereReduziertesSet(internNumbers, newValue));
                
                 
            }
        });
        
      LDAPController l = new LDAPController("server", 389, "server", "people" );
      ArrayList<LDAPEntry> ld = l.getN("", 5);
      
      for(LDAPEntry ent : ld) {
          System.out.println("Vorname: "+ent.getVorname() +" ,Nachname: "+ ent.getNachname()+" ,Firma: "+ent.getFirma());
      }

    }    
    private void selectTab(int i) {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(i); //select by index starting with 0
        tabPane.setSelectionModel(selectionModel);
        selectionModel.clearSelection(); //clear your selection
    }
    private ArrayList<internField> generiereReduziertesSet(Map<String, PhoneNumber> internNumbers, String val) {
        ArrayList<internField> out = new ArrayList<>();
            for(Map.Entry<String,PhoneNumber> g : internNumbers.entrySet()){
            if(g.getKey().toLowerCase().contains(val.toLowerCase())){
             out.add(new internField(g.getKey(),g.getValue().getCount(),g.getValue().getPhoneNumber()));
            }
        }
        return out;
    }
    private void updateAnzeige(ArrayList<internField> i) {
         Collections.sort(i, new Comparator<internField>() {

        public int compare(internField o1, internField o2) {
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
     


 
    
   
}
