/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johannes.lsctic.address.loaders;

import com.johannes.lsctic.OptionsStorage;
import com.johannes.lsctic.address.AdressPlugin;

import java.io.File;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author johannes
 */
public class LoaderRegister {

    ArrayList<String> pluginsFound;
    ArrayList<AdressPlugin> loadedPlugins;
    private String folderpath;

    public LoaderRegister() {

    }

    public ArrayList<String> explorePluginFolder(String folderpath) {
        this.folderpath = folderpath;
        this.pluginsFound = new ArrayList<>();
        this.pluginsFound.add("MysqlPlugin");
        //getAvailablePlugins();
        return this.pluginsFound;
    }

    public void loadPlugins(ArrayList<String> pluginsToLoad) {
        for (String pl : pluginsToLoad) {
            if (pluginsFound.contains(pl)) {
                loadedPlugins.add(getInstantiatedClass(pl, this.folderpath));
            }
        }
    }
    
   /* public  AddressLoader getLoader(String text, OptionsStorage op) {
        switch(text){
            case("ldap"):
               return 
            case("mysql"):
                return new MySqlLoader(op);
        }
        return null;
    }*/

    public static void addNewLoader(String text) {
        Logger.getLogger("sdfaf").info(text);
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

    private AdressPlugin getInstantiatedClass(String classname, String folder) {

        try {
            File dir = new File(folder);
            URL loadPath = dir.toURI().toURL();
            URL[] classUrl = new URL[]{loadPath};
            ClassLoader cl = new URLClassLoader(classUrl);

            Class loadedClass = cl.loadClass(classname);

            AdressPlugin modInstance = (AdressPlugin) loadedClass.newInstance();
            return modInstance;
        } catch (MalformedURLException | ClassCastException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger("Plugin konnte nicht geladen werden!");
        }
        return null;
    }
}
