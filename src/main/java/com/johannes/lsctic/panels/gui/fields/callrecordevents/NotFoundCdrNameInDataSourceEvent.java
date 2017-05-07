package com.johannes.lsctic.panels.gui.fields.callrecordevents;

/**
 * Created by johannes on 15.04.2017.
 */
public class NotFoundCdrNameInDataSourceEvent {
    private final String who;
    private final String when;
    private final String howLong;
    private final boolean outgoing;
    private final long timeStamp;

    public NotFoundCdrNameInDataSourceEvent(SearchDataSourcesForCdrEvent event) {
        this.who = event.getWho();
        this.when = event.getWhen();
        this.howLong = event.getHowLong();
        this.outgoing = event.isOutgoing();
        this.timeStamp = event.getTimeStamp();
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

    public long getTimeStamp() {
        return timeStamp;
    }
}
