/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.plugins;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.johannes.lsctic.SqlLiteConnection;
import com.johannes.lsctic.messagestage.ErrorMessage;
import com.johannes.lsctic.panels.gui.fields.callrecordevents.*;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author johannes
 */
public class PluginRegister {

    private ArrayList<String> pluginsFound;
    private ArrayList<AddressPlugin> loadedPlugins;
    private String exploredFolder;
    private ArrayList<String> approvedPlugins;
    private EventBus eventBus;


    public PluginRegister(EventBus eventBus) {
        loadedPlugins = new ArrayList<>();
        pluginsFound = new ArrayList<>();
        approvedPlugins = new ArrayList<>();
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    public static void addNewLoader(String text) {
        // TODO: Implement function
    }

    public void reloadPlugins(List<String> pluginsToLoad, String folderPath) {
        loadedPlugins.clear();
        explorePluginFolder(folderPath);
        approvedPlugins.forEach((pluginname)-> loadPlugin(pluginname, folderPath));
    }

    public void removeUnloadedPlugins(ArrayList<String> actualPlugins) {
        ArrayList<AddressPlugin> delete = new ArrayList<>();
        for(AddressPlugin addressPlugin : loadedPlugins) {
            if(!actualPlugins.contains(addressPlugin.getName())) {
                approvedPlugins.remove(addressPlugin.getName());
                delete.add(addressPlugin);
            }
        }
        loadedPlugins.removeAll(delete);
    }

    public ArrayList<String> getApprovedPlugins() {
        return approvedPlugins;
    }

    public void loadPlugin(String pluginName, String folderPath) {
        for(AddressPlugin loadedPlugin: loadedPlugins) {
            if(loadedPlugin.getName().equals(pluginName)) {
                Logger.getLogger(getClass().getName()).info("Thats the case here");
                return;
            }
        }

        if (!folderPath.equals(exploredFolder)) {
            explorePluginFolder(folderPath);
        }

            Logger.getLogger(getClass().getName()).info(pluginName + "    " + pluginsFound.toString());

            // Load the plugins from the folder
            if (pluginsFound.contains(pluginName)) {
                try {
                    AddressPlugin plugin = getInstantiatedClass(pluginName, folderPath);
                    loadedPlugins.add(plugin);
                    plugin.getLoader().setEventBus(eventBus);
                    if(!approvedPlugins.contains(pluginName)) {
                        approvedPlugins.add(pluginName);
                    }
                } catch (IOException e1) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e1);
                    new ErrorMessage("Plugin "+ pluginName+" could not be load. The plugin seems to be broken.");
                }
            } else {
                new ErrorMessage("Plugin "+ pluginName+" could not be load. Is the path correct?");
            }


    }

    private AddressPlugin getInstantiatedClass(String classname, String folder) throws IOException {
        File loc = new File(folder);
        File[] flist = loc.listFiles(file -> file.getPath().toLowerCase().endsWith(classname.toLowerCase()+".jar"));
        URL[] urls = new URL[flist.length];
        for (int i = 0; i < flist.length; i++) {
            urls[i] = flist[i].toURI().toURL();
        }
        URLClassLoader ucl = new URLClassLoader(urls);
        ServiceLoader<AddressPlugin> sl = ServiceLoader.load(AddressPlugin.class, ucl);
        Iterator<AddressPlugin> apit = sl.iterator();
        while (apit.hasNext()) {
            AddressPlugin pl = apit.next();
            if (pl.getName().equals(classname)) {
                return pl;
            }
        }
        throw new IOException("Fehler");
    }

    public void explorePluginFolder(String folderPath) {
        this.pluginsFound = getAvailablePluginsFromFolder(folderPath);
        Logger.getLogger(getClass().getName()).info(pluginsFound.toString());
        exploredFolder = folderPath;
    }

    // Todo change to only load the one approved plugin
    public void activateLicensedPlugins(SqlLiteConnection sqlLiteConnection, String pluginname) {
        for (AddressPlugin addressPlugin : loadedPlugins) {
            if(addressPlugin.getName().equals(pluginname)) {
                addressPlugin.setDataFields(sqlLiteConnection.getFieldsForDataSource(addressPlugin.getName()));
                addressPlugin.setOptions(sqlLiteConnection.getOptionsForDataSource(addressPlugin.getName()));
            }
        }
    }

    public void resetSpecificPlugin(SqlLiteConnection sqlLiteConnection, AddressPlugin addressPlugin) {
            addressPlugin.setDataFields(sqlLiteConnection.getFieldsForDataSource(addressPlugin.getName()));
            addressPlugin.setOptions(sqlLiteConnection.getOptionsForDataSource(addressPlugin.getName()));
    }

    private ArrayList<String> getAvailablePluginsFromFolder(String folder) {
        File dir = new File(folder);
        ArrayList<String> fileList = new ArrayList();
        if (dir.isDirectory() && dir.listFiles() != null) {
            for (File f : dir.listFiles()) {
                if (f.getName().endsWith(".jar")) {
                    fileList.add(f.getName().substring(0,f.getName().length()-4));
                }
            }
        }
        return fileList;
    }


    public List<PluginSettingsField> getAllPluginSettingsFields() {
        ArrayList<PluginSettingsField> settingsFields = new ArrayList<>();
        for (AddressPlugin plugin : loadedPlugins) {
            settingsFields.add(plugin.getPluginSettingsField());
        }
        return settingsFields;
    }


    public List<String> getLoadedPluginNames() {
        ArrayList<String> loadedPluginsNames = new ArrayList<>();
        for(AddressPlugin plugin: loadedPlugins) {
            String[] classname = plugin.getClass().getName().split("\\.");
            loadedPluginsNames.add(classname[classname.length-1]);
        }
        return loadedPluginsNames;
    }


    public List<AddressBookEntry> getResultFromEveryPlugin(String query, int number) {
        ArrayList<AddressBookEntry> filteredQuery = new ArrayList<>();

        for (AddressPlugin plugin : loadedPlugins) {
            ArrayList<AddressBookEntry> pluginResult = plugin.getResults(query, number);
            if (pluginResult != null && !pluginResult.isEmpty()) {
                filteredQuery.addAll(pluginResult);
            }
        }
        if (!filteredQuery.isEmpty()) {
            filteredQuery.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        }
        if (filteredQuery.size() > number) {
            return filteredQuery.subList(0, number);
        }
        return filteredQuery;
    }


    public List<AddressPlugin> getAllActivePlugins() {
        return loadedPlugins;
    }


    //If a CDR comes in, it comes without a name -> try to resolve the name and write it into the local cache
    @Subscribe
    public void searchNameToNumber(SearchDataSourcesForCdrEvent event) {
        AtomicInteger searchFinished  = new AtomicInteger(loadedPlugins.size());
        AtomicBoolean found  = new AtomicBoolean(false);
        for (AddressPlugin plugin : loadedPlugins) {
            Logger.getLogger(getClass().getName()).info("invoking Plugin search");
            plugin.resolveNameForNumber(event, searchFinished, found);
        }
    }

    //Resolving Input on the historyfield search -> mapping entered name to number in datasource
    @Subscribe
    public void searchPossibleNumbersToName(ResolveNumberFromNameEvent event) {
        Logger.getLogger(getClass().getName()).info("Event invoked ");

        for (AddressPlugin plugin : loadedPlugins) {
            Logger.getLogger(getClass().getName()).info("Inside Loop");
            plugin.searchPossibleNumbers(event.getName(), event.getLeft(), event.getTimestamp());
        }
    }
    







    public void acceptAllPlugins() {
        for (AddressPlugin l : loadedPlugins) {
            l.getLoader().saved();
            l.getPluginSettingsField().refresh();
        }
    }


    public void discardAllPlugins() {
        for (AddressPlugin l : loadedPlugins) {
            l.getLoader().discarded();
            l.getPluginSettingsField().refresh();
        }
    }

    public String getPluginLicense(String pluginname, String folder) {
            try {
                byte[] keyBytes = Files.readAllBytes(new File(folder+"/"+pluginname+".lic").toPath());
                String msg = new String(keyBytes);
                return msg;
            } catch (IOException e) {
                Platform.runLater(()-> new ErrorMessage("Could not load license file for "+pluginname+". Should be "+folder+"/"+pluginname+".lic"));
            }
            return "";

    }


    public List<String> getPluginsFound() {
        return pluginsFound;
    }
}
