package com.johannes.lsctic.panels.gui.fields;

public class RemoveCdrAndUpdateEvent {
   private HistoryField historyField;

   public RemoveCdrAndUpdateEvent(HistoryField historyField) {
       this.historyField = historyField;
   }

   public HistoryField getHistoryField() {
       return historyField;
   }
}
