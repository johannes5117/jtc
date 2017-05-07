package com.johannes.lsctic.panels.gui.fields.callrecordevents;

/**
 * Created by johannes on 07.04.2017.
 */
public class AddCdrAndUpdateEvent {
    private final String who;
    private final String when;
    private final String howLong;
    private final boolean outgoing;
    private final long timeStamp;
    public AddCdrAndUpdateEvent(String who, String when, String howLong, boolean outgoing, long timeStamp) {
        this.who = who;
        this.when = when;
        this.howLong = howLong;
        this.outgoing = outgoing;
        this.timeStamp = timeStamp;
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
