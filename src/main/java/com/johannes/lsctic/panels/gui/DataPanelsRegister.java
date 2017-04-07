package com.johannes.lsctic.panels.gui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.johannes.lsctic.PhoneNumber;
import com.johannes.lsctic.SqlLiteConnection;
import com.johannes.lsctic.panels.gui.fields.*;
import com.johannes.lsctic.panels.gui.fields.internevents.AddInternEvent;
import com.johannes.lsctic.panels.gui.fields.internevents.NewInternField;
import com.johannes.lsctic.panels.gui.fields.internevents.RemoveInternAndUpdateEvent;
import com.johannes.lsctic.panels.gui.fields.AddCdrAndUpdateEvent;
import com.johannes.lsctic.panels.gui.fields.HistoryField;
import com.johannes.lsctic.panels.gui.fields.RemoveCdrAndUpdateEvent;
import com.johannes.lsctic.panels.gui.fields.SetStatusEvent;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.AboStatusExtensionEvent;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.DeAboStatusExtension;
import com.johannes.lsctic.panels.gui.plugins.AddressBookEntry;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by johannes on 06.04.2017.
 */
public class DataPanelsRegister {
    private HashMap<String, InternField> internFields;
    private Map<String, PhoneNumber> internNumbers;
    private ArrayList<HistoryField> historyFields;
    private SqlLiteConnection sqlLiteConnection;
    private String quickdialString;
    private VBox panelA;
    private VBox panelB;
    private VBox panelC;
    private VBox panelD;

    private EventBus eventBus;

    public DataPanelsRegister(EventBus bus, SqlLiteConnection sqlLiteConnection, VBox[] panels){

        this.panelA = panels[0];
        this.panelB = panels[1];
        this.panelC = panels[2];
        this.panelD = panels[3];

        this.eventBus = bus;
        this.eventBus.register(this);
        this.sqlLiteConnection = sqlLiteConnection;
        internNumbers = sqlLiteConnection.getInterns();
        panelA.setSpacing(3);
        panelB.setSpacing(3);
        panelC.setSpacing(3);

        internFields = new HashMap();
        internNumbers.entrySet().stream().forEach(g
                -> internFields.put(g.getKey(), new InternField(g.getValue().getName(), g.getValue().getCount(), g.getKey(), eventBus)));

        internFields.entrySet().stream().forEach(g -> eventBus.post(new AboStatusExtensionEvent(g.getValue().getNumber())));

        updateView(new ArrayList<>(internFields.values()));

        historyFields = new ArrayList();

        panelC.getChildren().addAll(historyFields);
        //internNumbers = sqlLiteConnection.getInterns();
       // internFields = new HashMap();
       // internNumbers.entrySet().stream().forEach(g
       //         -> internFields.put(g.getKey(), new InternField(g.getValue().getName(), g.getValue().getCount(), g.getKey())));
    }

    @Subscribe
    public void addInternAndUpdate(AddInternEvent event) {
        PhoneNumber p = event.getPhoneNumber();
        if (!internFields.containsKey(p.getPhoneNumber())) {
            sqlLiteConnection.queryNoReturn("Insert into internfields (number,name,callcount,favorit) values ('" + p.getPhoneNumber() + "','" + p.getName() + "'," + p.getCount() + ",0)");
            internNumbers.put(p.getPhoneNumber(), p);
            internFields.put(p.getPhoneNumber(), new InternField(p.getName(), p.getCount(), p.getPhoneNumber(), eventBus));
            eventBus.post(new AboStatusExtensionEvent(p.getPhoneNumber()));
            updateView(new ArrayList<>(internFields.values()));
        } else {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "There already exists a user with that phonenumber.");
        }
    }
    public ArrayList<InternField> generateReducedSet(String val) {
        ArrayList<InternField> out = new ArrayList<>();
        internFields.values().stream().filter((f) -> (f.getName().toLowerCase().contains(val.toLowerCase()))).forEachOrdered((f) -> out.add(f));
        return out;
    }

    @Subscribe
    public void addCdrAndUpdate(AddCdrAndUpdateEvent event) {
        HistoryField f = new HistoryField(event.getWho(), event.getWhen(),event.getHowLong(),event.isOutgoing(), event.getEventBus());
        historyFields.add(f);
        panelC.getChildren().clear();
        panelC.getChildren().addAll(historyFields);
    }

    @Subscribe
    public void removeCdrAndUpdate(RemoveCdrAndUpdateEvent event) {
        HistoryField f = event.getHistoryField();
        historyFields.remove(f);
        panelC.getChildren().clear();
        panelC.getChildren().addAll(historyFields);
    }

    @Subscribe
    public void setStatus(SetStatusEvent event) {
        internFields.get(event.getIntern()).setStatus(event.getStatus());
    }

    @Subscribe
    public void removeInternAndUpdate(RemoveInternAndUpdateEvent event) {
        sqlLiteConnection.queryNoReturn("Delete from internfields where number='" +event.getNumber() + "'");
        internFields.remove(event.getNumber());
        internNumbers.remove(event.getNumber());
        eventBus.post(new DeAboStatusExtension(event.getNumber()));
        updateView(new ArrayList<>(internFields.values()));
    }

    public void updateView(ArrayList<InternField> i) {
        //scrollPaneA.setVvalue(0);
        Collections.sort(i, (InternField o1, InternField o2) -> o2.getCount() - o1.getCount()); //UPDATE: would be nice to choose the sorting
        panelA.getChildren().clear();
        panelA.getChildren().addAll(i);
        panelA.getChildren().add(new NewInternField(eventBus));
    }

    public void updateAddressFields(ArrayList<AddressBookEntry> i) {
        panelB.getChildren().clear();
        ArrayList<AddressField> addressFields = new ArrayList<>();
        i.stream().forEach(ent -> addressFields.add(new AddressField(2, 123123, ent, eventBus)));
        panelB.getChildren().addAll(addressFields);
    }

}
