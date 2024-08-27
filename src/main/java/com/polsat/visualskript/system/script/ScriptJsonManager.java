package com.polsat.visualskript.system.script;

import com.polsat.visualskript.util.ErrorHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ScriptJsonManager {

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
