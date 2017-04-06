package com.johannes.lsctic.panels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.johannes.lsctic.PhoneNumber;
import com.johannes.lsctic.fields.InternField;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by johannes on 06.04.2017.
 */
public class FieldRegister {
    private HashMap<String, InternField> internFields;
    private Map<String, PhoneNumber> internNumbers;

    private EventBus eventBus;

    public FieldRegister(EventBus bus){
        this.eventBus = bus;
        this.eventBus.register(this);

        //internNumbers = sqlLiteConnection.getInterns();
       // internFields = new HashMap();
       // internNumbers.entrySet().stream().forEach(g
       //         -> internFields.put(g.getKey(), new InternField(g.getValue().getName(), g.getValue().getCount(), g.getKey())));
    }

    @Subscribe
    public void handleAction(InternEvent deadEvent) {
        Logger.getLogger(getClass().getName()).info("EVENTBUS WOOOOOOOOORKS");
    }

}
