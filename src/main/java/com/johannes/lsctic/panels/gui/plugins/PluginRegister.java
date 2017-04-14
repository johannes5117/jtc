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
            Logger.getLogger(getClass().getName()).info(pl);

            // First search the class in the tool. It may be included. Depending on build.
            try {
                Class<?> loader = Class.forName("com.johannes.lsctic.panels.gui.plugins." + pl + "." + pl);
                Constructor<?> ctor = loader.getConstructor();
                Object object = ctor.newInstance(new Object[]{});
                loadedPlugins.add((AddressPlugin) object);
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                // If its not integrated search in the folder given from the user
                if (pluginsFound.contains(pl + ".jar")) {
                    try {
                        loadedPlugins.add(getInstantiatedClass(pl, folderPath));
                    } catch (IOException e1) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e1);
                    }
                }
            }

        }
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


    public static void addNewLoader(String text) {
        // TODO: Implement function
    }


    private ArrayList<String> getAvailablePluginsFromFolder(String folder) {
        File dir = new File(folder);
        ArrayList<String> fileList = new ArrayList();
        if (dir.isDirectory() && dir.listFiles() != null) {
            for (File f : dir.listFiles()) {
                if (f.getName().endsWith(".jar")) {
                    fileList.add(f.getName());
                }
            }
        }
        return fileList;
    }


    private AddressPlugin getInstantiatedClass(String classname, String folder) throws IOException {
/*
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
            new ErrorMessage("Plugin "+classname+" could not be loaded. Not found internal or in plugin path");
        }
        return null;
        */
        File loc = new File(folder);

        File[] flist = loc.listFiles(file -> file.getPath().toLowerCase().endsWith(".jar"));
        URL[] urls = new URL[flist.length];
        for (int i = 0; i < flist.length; i++)
            urls[i] = flist[i].toURI().toURL();
        URLClassLoader ucl = new URLClassLoader(urls);
        ServiceLoader<AddressPlugin> sl = ServiceLoader.load(AddressPlugin.class, ucl);
        Iterator<AddressPlugin> apit = sl.iterator();
        while (apit.hasNext())
            return apit.next();
        throw new IOException("Fehler");
    }



    public List<SettingsField> getAllSettingsFields() {
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
