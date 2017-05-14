package com.johannes.lsctic.panels.gui.fields.internevents;

/**
 * Created by johannesengler on 13.05.17.
 */
public class ReorderDroppedEvent {
    private int resolvedPosition;

    public ReorderDroppedEvent(int resolvedPosition) {
        this.resolvedPosition = resolvedPosition;
    }

    public int getResolvedPosition() {
        return resolvedPosition;
    }
}
