package com.polsat.visualskript.system.script;

import com.polsat.visualskript.util.ErrorHandler;

import java.io.File;

public class ScriptManager {

    public static void setOpened(File file, boolean status){
        System.out.println("Opened na " + status + " dla " + file.getName());
        try {
            //SKRIPT OPTIONS: status
        } catch (Exception e){
            ErrorHandler.alert(e.toString());
        }
    }

    public static boolean getOpened(File file){
        try {
            //GET SKRIPT OPTIONS: status
        } catch (Exception e){
            ErrorHandler.alert(e.toString());
            return false;
        }
        return true;
    }

}
