package com.polsat.visualskript.gui.manager;

import com.polsat.visualskript.Main;
import com.polsat.visualskript.util.ErrorHandler;

import java.io.File;
import java.util.Objects;

public class FileManager {

    public static boolean createFile(String path){
        File newFile = new File(path);
        try {
            return newFile.createNewFile();
        }catch (Exception e){
            ErrorHandler.alert(e.toString());
            return false;
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
            ErrorHandler.alert(e.toString());
            return new File[0];
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
