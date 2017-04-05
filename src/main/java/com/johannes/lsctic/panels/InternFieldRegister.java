package com.johannes.lsctic.panels;

import com.johannes.lsctic.PhoneNumber;
import com.johannes.lsctic.fields.InternField;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by johannes on 06.04.2017.
 */
public class InternFieldRegister {
    private HashMap<String, InternField> internFields;
    private Map<String, PhoneNumber> internNumbers;

    public InternFieldRegister() {
        //internNumbers = sqlLiteConnection.getInterns();
       // internFields = new HashMap();
       // internNumbers.entrySet().stream().forEach(g
       //         -> internFields.put(g.getKey(), new InternField(g.getValue().getName(), g.getValue().getCount(), g.getKey())));
    }

}
