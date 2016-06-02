package com.johannes.lsctic;


import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private VBox panelA;
    @FXML
    private TextField paneATextIn;
    private Map<String, PhoneNumber> internNumbers;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        panelA.setSpacing(3);
        internNumbers = new TreeMap<String, PhoneNumber>();
        internNumbers.put("Johannes Engler", new PhoneNumber(true, 0157, "Johannes Engler", 12));
        internNumbers.put("Michael Engler", new PhoneNumber(true, 0157, "Michael Engler", 2));
        internNumbers.put("Fabian Engler", new PhoneNumber(true, 0157, "Fabian Engler", 1));
        internNumbers.put("Fabian Englert", new PhoneNumber(true, 0157, "Fabian Englert", 5));
        
       
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

    }    
    private ArrayList<internField> generiereReduziertesSet(Map<String, PhoneNumber> internNumbers, String val) {
        ArrayList<internField> out = new ArrayList<>();
            for(Map.Entry<String,PhoneNumber> g : internNumbers.entrySet()){
            if(g.getKey().contains(val)){
             out.add(new internField(g.getKey(),g.getValue().getCount(),g.getValue().getPhoneNumber()));
            }
        }
        return out;
    }
    private void updateAnzeige(ArrayList<internField> i) {
         Collections.sort(i, new Comparator<internField>() {

        public int compare(internField o1, internField o2) {
            //return o2.getCount()- o1.getCount(); Sortiert nach Count
            int i = 0;
            while(o1.getName().charAt(i) == o2.getName().charAt(i)) {
                ++i;
                if(i>=o1.getName().length() || i>=o2.getName().length()) {
                return 0;
            }
            }
            
            return o1.getName().charAt(i) - o2.getName().charAt(i);
        }
            });
        panelA.getChildren().clear();
        panelA.getChildren().addAll(i);
    }
   
}
