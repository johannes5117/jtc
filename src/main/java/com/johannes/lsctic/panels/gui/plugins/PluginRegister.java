/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.plugins;

import com.johannes.lsctic.messagestage.ErrorMessage;
import com.johannes.lsctic.panels.gui.settings.SettingsField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author johannes
 */
public class PluginRegister {

    private ArrayList<String> pluginsFound;
    private ArrayList<AddressPlugin> loadedPlugins;
    private String exploredFolder;


    public PluginRegister() {
        loadedPlugins = new ArrayList<>();
        pluginsFound = new ArrayList<>();
    }


    public void reloadPlugins(List<String> pluginsToLoad, String folderPath) {
        loadedPlugins.clear();
        explorePluginFolder(folderPath);
        loadPlugins(pluginsToLoad, folderPath);
    }


    public void loadPlugins(List<String> pluginsToLoad, String folderPath) {
        if (!folderPath.equals(exploredFolder)) {
            explorePluginFolder(folderPath);
        }
        for (String pl : pluginsToLoad) {
            Logger.getLogger(getClass().getName()).info(pl + "    " + pluginsFound.toString());

            // Load the plugins from the folder
            if (pluginsFound.contains(pl)) {
                try {
                    AddressPlugin plugin = getInstantiatedClass(pl, folderPath);
                    loadedPlugins.add(plugin);
                } catch (IOException e1) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e1);
                    new ErrorMessage("Plugin "+ pl+" could not be load. The plugin seems to be broken.");
                }
            } else {
                new ErrorMessage("Plugin "+ pl+" could not be load. Is the path correct?");
            }
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
                return apit.next();
        }
        throw new IOException("Fehler");
    }


    public void explorePluginFolder(String folderPath) {
        this.pluginsFound = getAvailablePluginsFromFolder(folderPath);
        Logger.getLogger(getClass().getName()).info(pluginsFound.toString());
        exploredFolder = folderPath;
    }


    public void activateAllPlugins(Connection con) throws SQLException {
        for (AddressPlugin addressPlugin : loadedPlugins) {
            addressPlugin.readFields(con);
        }
    }

    public static void addNewLoader(String text) {
        // TODO: Implement function
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


    public List<SettingsField> getAllSettingsFields() {
        ArrayList<SettingsField> settingsFields = new ArrayList<>();
        for (AddressPlugin plugin : loadedPlugins) {
            settingsFields.add(plugin.getSettingsField());
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


    public void acceptAllPlugins() {
        for (AddressPlugin l : loadedPlugins) {
            l.getLoader().saved();
        }
    }


    public void discardAllPlugins() {
        for (AddressPlugin l : loadedPlugins) {
            l.getLoader().discarded();
        }
    }


    public List<String> getPluginsFound() {
        return pluginsFound;
    }
}
