package com.polsat.visualskript.gui.manager;

import com.polsat.visualskript.Main;
import com.polsat.visualskript.gui.manager.tabs.MainTabManager;
import com.polsat.visualskript.system.script.ScriptJsonManager;
import com.polsat.visualskript.system.script.ScriptParser;
import com.polsat.visualskript.util.ErrorHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ScriptsManager {

    public static boolean createScript(String name){
        boolean success = FileManager.createFile(getScriptPathFolder()+name+".vsk");
        if (success){
            MainTabManager.addTab(name+".vsk");
            ScriptParser.makeVSkript(FileManager.getFileByName(name+".vsk"));
            return true;
        }else{
            return false;
        }
    }
    public static void deleteScript(String name){
        MainTabManager.removeTab(name);
        FileManager.deleteFile(getScriptPathFolder()+name);
    }
    public static void openScript(String name){
        MainTabManager.addTab(name);
        ScriptJsonManager.setOpened(FileManager.getFileByName(name), true);
        ScriptParser.build(FileManager.getFileByName(name));
        //TODO: DELETE ^^, ZMIANA SYSTEMU
    }
    public static void closeScript(String name){
        MainTabManager.removeTab(name);
    }
    public static void editScriptName(String name, String newName){
        FileManager.renameFile(ScriptsManager.getScriptPathFolder()+name, newName);
        MainTabManager.removeTab(name);
        MainTabManager.addTab(newName+".vsk");
    }

    public static ArrayList<String> getScriptsList() {
        ArrayList<String> list = new ArrayList<>();
        try {
            File[] filesList = FileManager.getFileList("/scripts");
            for (File file : filesList) {
                if (file.getName().endsWith(".vsk")) {
                    list.add(file.getName());
                }
            }
        } catch (Exception e){
            new ErrorHandler(e.toString());
        }

        return list;
    }

    public static ArrayList<String> getScriptsListWithOpenedStatus(boolean status) {
        ArrayList<String> list = new ArrayList<>();
        for (String text : ScriptsManager.getScriptsList()){
            if (Objects.equals(ScriptJsonManager.getOpened(FileManager.getFileByName(text)), status)) {
                list.add(text);
            }
        }
        return list;
    }

    public static String getScriptPathFolder(){
        return Main.class.getResource("/scripts").getPath();
    }

}
