package com.polsat.visualskript.gui.manager;

import com.polsat.visualskript.Main;
import com.polsat.visualskript.system.script.ScriptJsonManager;
import com.polsat.visualskript.system.script.ScriptParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ScriptsManager {

    public static boolean createScript(String name){
        boolean success = FileManager.createFile(getScriptPathFolder()+name+".vsk");
        if (success){
            TabManager.addTab(name+".vsk");
            ScriptParser.makeVSkript(FileManager.getFileByName(name+".vsk"));
            return true;
        }else{
            return false;
        }
    }
    public static void deleteScript(String name){
        TabManager.removeTab(name);
        FileManager.deleteFile(getScriptPathFolder()+name);
    }
    public static void openScript(String name){
        TabManager.addTab(name);
        ScriptJsonManager.setOpened(FileManager.getFileByName(name), true);
        ScriptParser.parse(FileManager.getFileByName(name));
    }
    public static void closeScript(String name){
        TabManager.removeTab(name);
    }
    public static void editScriptName(String name, String newName){
        FileManager.renameFile(ScriptsManager.getScriptPathFolder()+name, newName);
        TabManager.removeTab(name);
        TabManager.addTab(newName+".vsk");
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
            throw new RuntimeException(e);
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
