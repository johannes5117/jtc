package com.johannes.lsctic.panels.gui.plugins.pluginevents;

/**
 * Created by johannes on 01.05.17.
 */
public class PluginLicenseApprovedEvent {
    private String pluginname;

    public PluginLicenseApprovedEvent(String pluginname) {
        this.pluginname = pluginname;
    }

    public String getPluginname() {
        return pluginname;
    }
}
