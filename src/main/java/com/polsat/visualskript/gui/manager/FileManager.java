package com.polsat.visualskript.gui.manager;

import com.polsat.visualskript.Main;
import com.polsat.visualskript.system.script.ScriptParser;

import java.io.File;
import java.util.Objects;

public class FileManager {

    public static boolean createFile(String path){
        File newFile = new File(path);
        try {
            return newFile.createNewFile();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static boolean deleteFile(String path){
        File file = new File(path);
        return file.delete();
    }

    public static boolean renameFile(String path, String newName){
        File file = new File(path);
        return file.renameTo(new File(ScriptsManager.getScriptPathFolder()+newName+".vsk"));
    }

    public static File[] getFileList(String folderName){
        try {
            return new File(Main.class.getResource(folderName).toURI()).listFiles();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static File getFileByName(String name){
        for (File file : getFileList("/scripts")){
            if (Objects.equals(file.getName(), name)){
                return file;
            }
        }
        return null;
    }
}
