/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.panels.gui.plugins;

import com.johannes.lsctic.panels.gui.settings.SettingsField;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author johannes
 */
public class LoaderRegister {

    private ArrayList<String> pluginsFound;
    private ArrayList<AddressPlugin> loadedPlugins;
    private String exploredFolder;

    public LoaderRegister() {
        loadedPlugins = new ArrayList<>();
        pluginsFound = new ArrayList<>();
    }

    public void explorePluginFolder(String folderPath) {
        this.pluginsFound = getAvailablePluginsFromFolder(folderPath);
        exploredFolder = folderPath;
    }

    public void activateAllPlugins(Connection con) throws SQLException {
        for (AddressPlugin addressPlugin : loadedPlugins) {
            addressPlugin.readFields(con);
        }
    }

    public void registerHardCodedPlugins(List<String> plugins) {
        this.pluginsFound.addAll(plugins);
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
            Logger.getLogger(getClass().getName()).info(pl);
            try {
                Class<?> loader = Class.forName("com.johannes.lsctic.panels.gui.plugins." + pl + "." + pl);
                Constructor<?> ctor = loader.getConstructor();
                Object object = ctor.newInstance(new Object[]{});
                loadedPlugins.add((AddressPlugin) object);
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                if (pluginsFound.contains(pl + ".class")) {
                    loadedPlugins.add(getInstantiatedClass(pl, folderPath));
                }
            }

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
                if (f.getName().endsWith(".class")) {
                    fileList.add(f.getName());
                }
            }
        }
        return fileList;
    }

    private AddressPlugin getInstantiatedClass(String classname, String folder) {

        try {
            File dir = new File(folder);
            URL loadPath = dir.toURI().toURL();
            URL[] classUrl = new URL[]{loadPath};
            Class loadedClass;
            try(URLClassLoader cl = new URLClassLoader(classUrl)) {
                loadedClass = cl.loadClass(classname);
            }
            return (AddressPlugin) loadedClass.newInstance();
        } catch (IOException | ClassCastException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<SettingsField> getAllSettingsfields() {
        ArrayList<SettingsField> settingsFields = new ArrayList<>();
        for (AddressPlugin plugin : loadedPlugins) {
            settingsFields.add(plugin.getSettingsField());
        }
        return settingsFields;
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
