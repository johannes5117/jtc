package com.johannes.lsctic.panels.gui.fields.callrecordevents;

/**
 * Created by johannes on 15.04.2017.
 */
public class FoundCdrNameInDataSourceEvent {
    private final String name;
    private final String who;
    private final String when;
    private final String howLong;
    private final boolean outgoing;

    public FoundCdrNameInDataSourceEvent(SearchDataSourcesForCdrEvent event,String name) {
        this.name = name;
        this.who = event.getWho();
        this.when = event.getWhen();
        this.howLong = event.getHowLong();
        this.outgoing = event.isOutgoing();
    }

    public String getName() {
        return name;
    }

    public String getWho() {
        return who;
    }

    public String getWhen() {
        return when;
    }

    public String getHowLong() {
        return howLong;
    }

    public boolean isOutgoing() {
        return outgoing;
    }
}
