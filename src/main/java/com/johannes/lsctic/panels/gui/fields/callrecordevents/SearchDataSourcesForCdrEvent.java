package com.johannes.lsctic.panels.gui.fields.callrecordevents;

/**
 * Created by johannes on 15.04.2017.
 */
public class SearchDataSourcesForCdrEvent {
    private AddCdrAndUpdateEvent event;
    public SearchDataSourcesForCdrEvent(AddCdrAndUpdateEvent event) {
        this.event = event;
    }
    public String getWho() {
        return event.getWho();
    }

    public String getWhen() {
        return event.getWhen();
    }

    public String getHowLong() {
        return event.getHowLong();
    }

    public boolean isOutgoing() {
        return event.isOutgoing();
    }
}
