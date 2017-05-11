package com.johannes.lsctic.panels.gui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.johannes.lsctic.PhoneNumber;
import com.johannes.lsctic.SqlLiteConnection;
import com.johannes.lsctic.messagestage.ErrorMessage;
import com.johannes.lsctic.panels.gui.fields.AddressField;
import com.johannes.lsctic.panels.gui.fields.HistoryField;
import com.johannes.lsctic.panels.gui.fields.InternField;
import com.johannes.lsctic.panels.gui.fields.callrecordevents.*;
import com.johannes.lsctic.panels.gui.fields.internevents.AddInternEvent;
import com.johannes.lsctic.panels.gui.fields.NewInternField;
import com.johannes.lsctic.panels.gui.fields.internevents.RemoveInternAndUpdateEvent;
import com.johannes.lsctic.panels.gui.fields.otherevents.*;
import com.johannes.lsctic.panels.gui.fields.serverconnectionhandlerevents.*;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by johannes on 06.04.2017.
 */
public class DataPanelsRegister {
    private HashMap<String, InternField> internFields;
    private Map<String, PhoneNumber> internNumbers;
    private List<HistoryField> historyFields;
    private SqlLiteConnection sqlLiteConnection;
    private VBox panelA;
    private VBox panelB;
    private VBox panelC;
    private Button buttonNext;
    private Button buttonLast;
    private int hFieldPerSite = 10;
    private int historyfieldCount = 0;
    private int maxHistoryFieldcount = 10;
    private boolean sortByCallCount = true;
    // Safes the last query for refreshs on the list
    private String searchPaneAValue = "";
    private String searchPaneCValue = "";

    private EventBus eventBus;

    public DataPanelsRegister(EventBus bus, SqlLiteConnection sqlLiteConnection, VBox[] panels, Button[] buttons) {

        this.panelA = panels[0];
        this.panelB = panels[1];
        this.panelC = panels[2];

        this.buttonLast = buttons[0];
        this.buttonNext = buttons[1];

        this.eventBus = bus;
        this.eventBus.register(this);
        this.sqlLiteConnection = sqlLiteConnection;
        internNumbers = sqlLiteConnection.getInterns();
        panelA.setSpacing(1);
        panelB.setSpacing(1);
        panelC.setSpacing(1);

        internFields = new HashMap();
        internNumbers.entrySet().stream().forEach(g
                -> internFields.put(g.getKey(), new InternField(g.getValue().getName(), g.getValue().getCount(), g.getKey(), eventBus)));

        updateView(new ArrayList<>(internFields.values()));
        historyFields = new ArrayList<>();
        panelC.getChildren().addAll(historyFields);

        buttonNext.setOnMouseClicked(event -> {
                ++historyfieldCount;
                this.buttonLast.setDisable(false);
                historyFields.clear();
                this.eventBus.post(new OrderCDRsEvent(historyfieldCount * hFieldPerSite,hFieldPerSite));
                // if for the next are not enough fields -> Disable button
                if ((historyfieldCount+1) * hFieldPerSite > maxHistoryFieldcount) {
                    this.buttonNext.setDisable(true);
                }
        });

        buttonLast.setOnMouseClicked(event -> {
            --historyfieldCount;
            historyFields.clear();
            this.buttonNext.setDisable(false);
            this.eventBus.post(new OrderCDRsEvent(historyfieldCount * hFieldPerSite,hFieldPerSite));
            // if user is on page 0 (start page, newest) -> Disable button
            if (historyfieldCount == 0) {
                this.buttonLast.setDisable(true);
            }
        });
    }

    @Subscribe
    public void logInSuccessful(UserLoginStatusEvent event) {
        historyFields.clear();
        panelC.getChildren().clear();
        if (event.isLoggedIn()) {
            internFields.entrySet().stream().forEach(g -> eventBus.post(new AboStatusExtensionEvent(g.getValue().getNumber())));
        }
        eventBus.post(new AskForCdrCountEvent());
        eventBus.post(new OrderCDRsEvent(historyfieldCount*hFieldPerSite,hFieldPerSite));
    }

    @Subscribe
    public void addInternAndUpdate(AddInternEvent event) {
        PhoneNumber p = event.getPhoneNumber();
        for (InternField field : internFields.values()) {
            if (field.getName().equals(p.getName())) {
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "There already exists a user with that phonenumber.");
                new ErrorMessage("There already exists a user with the same name");
                return;
            }
        }
        if (!internFields.containsKey(p.getPhoneNumber())) {
            sqlLiteConnection.queryNoReturn("Insert into internfields (id, number,name,callcount,position) " +
                    "values (((Select max(id) from internfields)+1),'" + p.getPhoneNumber() + "','" + p.getName() + "'," + p.getCount() + "," + p.getPosition() + ")");
            internNumbers.put(p.getPhoneNumber(), p);
            internFields.put(p.getPhoneNumber(), new InternField(p.getName(), p.getCount(), p.getPhoneNumber(), eventBus));
            eventBus.post(new AboStatusExtensionEvent(p.getPhoneNumber()));
            updateView(generateReducedSetFromList(searchPaneAValue,new ArrayList<>(internFields.values())));
        } else {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "There already exists a user with that phonenumber.");
            new ErrorMessage("There already exists a user with the same phone number");
        }
    }

    public List<InternField> generateReducedInternSet(String val) {
        this.searchPaneAValue = val;
        ArrayList<InternField> out = new ArrayList<>();
        internFields.values().stream().filter(f -> f.getName().toLowerCase().contains(val.toLowerCase())).forEachOrdered(f -> out.add(f));
        return out;
    }

    public List<InternField> generateReducedSetFromList(String val, ArrayList<InternField> internFieldsLocal) {
        ArrayList<InternField> out = new ArrayList<>();
        internFieldsLocal.stream().filter(f -> f.getName().toLowerCase().contains(val.toLowerCase())).forEachOrdered(f -> out.add(f));
        return out;
    }

    @Subscribe
    public void addCdrAndUpdate(AddCdrAndUpdateEvent event) {
        if (internFields.containsKey(event.getWho())) {
            InternField internField = internFields.get(event.getWho());
            String name = internField.getName();
            HistoryField f = new HistoryField(name, event.getWho(), event.getWhen(), event.getHowLong(), event.isOutgoing(), event.getTimeStamp(), eventBus);
            historyFields.add(0, f);
            if(historyFields.size()>=hFieldPerSite) {
                historyFields = historyFields.subList(0, hFieldPerSite);
            }
            panelC.getChildren().clear();
            panelC.getChildren().addAll(historyFields);
        } else {
            eventBus.post(new SearchDataSourcesForCdrEvent(event));
        }
    }

    @Subscribe
    public void addCdrUpdateWithNameFromDataSource(FoundCdrNameInDataSourceEvent event) {
        HistoryField f = new HistoryField(event.getName(), event.getWho(), event.getWhen(), event.getHowLong(), event.isOutgoing(),event.getTimeStamp(), eventBus);
        historyFields.add(0, f);
        if(historyFields.size()>=hFieldPerSite) {
            historyFields = historyFields.subList(0, hFieldPerSite);
        }        panelC.getChildren().clear();
        panelC.getChildren().addAll(historyFields);
    }

    @Subscribe
    public void addCdrUpdateWithoutName(NotFoundCdrNameInDataSourceEvent event) {
        HistoryField f = new HistoryField(event.getWho(), event.getWhen(), event.getHowLong(), event.isOutgoing(),event.getTimeStamp(), eventBus);
        historyFields.add(0, f);
        if(historyFields.size()>=hFieldPerSite) {
            historyFields = historyFields.subList(0, hFieldPerSite);
        }        panelC.getChildren().clear();
        panelC.getChildren().addAll(historyFields);
    }

    @Subscribe
    public void removeCdrAndUpdate(RemoveCdrAndUpdateLocalEvent event) {
        HistoryField f = event.getHistoryField();
        historyFields.remove(f);
        panelC.getChildren().clear();
        panelC.getChildren().addAll(historyFields);

        // Fetch current side (to get newest chagne from database) and clear historyfields
        eventBus.post(new RemoveCdrAndUpdateGlobalEvent(f, historyfieldCount*hFieldPerSite,hFieldPerSite));
        historyFields.clear();
    }

    @Subscribe
    public void setStatus(SetStatusEvent event) {
        internFields.get(event.getIntern()).setStatus(event.getStatus());
    }

    @Subscribe
    public void removeInternAndUpdate(RemoveInternAndUpdateEvent event) {
        sqlLiteConnection.queryNoReturn("Delete from internfields where number='" + event.getNumber() + "'");
        internFields.remove(event.getNumber());
        internNumbers.remove(event.getNumber());
        eventBus.post(new DeAboStatusExtensionEvent(event.getNumber()));
        updateView(generateReducedSetFromList(searchPaneAValue,new ArrayList<>(internFields.values())));
    }

    @Subscribe
    public void incrementCallCount(CallEvent event) {
        if (event.isIntern()) {
            sqlLiteConnection.updateOneAttribute("internfields", "number", event.getPhoneNumber(), "callcount",
                    String.valueOf(Integer.valueOf(sqlLiteConnection.query("Select callcount from internfields where number = '" + event.getPhoneNumber() + "'")) + 1));

            internFields.get(event.getPhoneNumber()).incCount();
            updateView(generateReducedSetFromList(searchPaneAValue,new ArrayList<>(internFields.values())));
        }
    }

    public void updateView(List<InternField> i) {
        if (sortByCallCount) {
            Collections.sort(i, (InternField o1, InternField o2) -> o2.getCount() - o1.getCount());
        } else {
            // Maybe other sorting option
        }
        panelA.getChildren().clear();
        panelA.getChildren().addAll(i);
        panelA.getChildren().add(new NewInternField(eventBus));
    }

    @Subscribe
    public void updateAddressFields(UpdateAddressFieldsEvent event) {
        panelB.getChildren().clear();
        ArrayList<AddressField> addressFields = new ArrayList<>();
        event.getAddressBookEntries().stream().forEach(ent -> addressFields.add(new AddressField(2, ent, eventBus)));
        panelB.getChildren().addAll(addressFields);
    }

    @Subscribe
    public void closeApplication(CloseApplicationSafelyEvent event) {
        eventBus.unregister(this);
    }

    @Subscribe
    public void viewOptionsChanged(ViewOptionsChangedEvent event) {
        this.sortByCallCount = event.isSortByCallCount();
        updateView(generateReducedSetFromList(searchPaneAValue,new ArrayList<>(internFields.values())));
    }

    @Subscribe
    public void serverConnectionLost(ConnectionToServerLostEvent event) {
        for (InternField f : internFields.values()) {
            f.setStatus(4);
        }
    }

    @Subscribe
    public void cdrAmountInDatabaseUpdate(CdrCountEvent event) {
        Logger.getLogger(getClass().getName()).info("Got the info: "+event.getCurrentAmount());
        maxHistoryFieldcount = event.getCurrentAmount();
        if ((historyfieldCount+1) * hFieldPerSite >= maxHistoryFieldcount) {
            this.buttonNext.setDisable(true);
        } else {
            this.buttonNext.setDisable(false);
        }
    }


}
