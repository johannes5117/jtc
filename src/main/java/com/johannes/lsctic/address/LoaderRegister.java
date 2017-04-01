/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.address;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author johannes
 */
public class LoaderRegister {

    ArrayList<String> pluginsFound;
    ArrayList<AddressPlugin> loadedPlugins;
    private String exploredFolder;

    public LoaderRegister() {

    }

    public void explorePluginFolder(String folderPath) {
        this.pluginsFound = getAvailablePluginsFromFolder(folderPath);
        exploredFolder = folderPath;
    }

    public void loadPlugins(ArrayList<String> pluginsToLoad, String folderPath) {
        if(!folderPath.equals(exploredFolder)) {
            explorePluginFolder(folderPath);
        }
        for (String pl : pluginsToLoad) {
            if (pluginsFound.contains(pl)) {
                loadedPlugins.add(getInstantiatedClass(pl, folderPath));
            }
        }
    }

    public static void addNewLoader(String text) {
        // TODO: Implement function
    }

    private ArrayList<String> getAvailablePluginsFromFolder(String folder) {
        File dir = new File(folder);
        ArrayList<String> fileList = new ArrayList();
        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".class")) {
                fileList.add(f.getName());
            }
        }
        return fileList;
    }

    private AddressPlugin getInstantiatedClass(String classname, String folder) {

        try {
            File dir = new File(folder);
            URL loadPath = dir.toURI().toURL();
            URL[] classUrl = new URL[]{loadPath};
            ClassLoader cl = new URLClassLoader(classUrl);

            Class loadedClass = cl.loadClass(classname);

            AddressPlugin modInstance = (AddressPlugin) loadedClass.newInstance();
            return modInstance;
        } catch (MalformedURLException | ClassCastException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger("Plugin konnte nicht geladen werden!");
        }
        return null;
    }

    public List<AddressBookEntry> getResultFromEveryPlugin(String query, int number) {
        ArrayList<AddressBookEntry> filteredQuery = new ArrayList<>();
        for(AddressPlugin plugin: loadedPlugins){
            ArrayList<AddressBookEntry> pluginResult = plugin.getResults(query, number);
            if(pluginResult!=null && pluginResult.isEmpty()!=false) {
                filteredQuery.addAll(pluginResult);
            }
        }
        if(!filteredQuery.isEmpty()) {
            filteredQuery.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        }
        if(filteredQuery.size()>number) {
            return filteredQuery.subList(0,number);
        }
        return filteredQuery;
    }
}
