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
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));
            jsonObject.put("opened", status);

            FileWriter writer = new FileWriter(file);
            writer.write(jsonObject.toJSONString());
            writer.close();
        } catch (Exception e){
            new ErrorHandler(e.toString());
        }
    }

    public static boolean getOpened(File file){
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject;
            try{
                jsonObject = (JSONObject) parser.parse(new FileReader(file));
            } catch (Exception ignore) {
                ScriptParser.makeVSkript(file);
                jsonObject = (JSONObject) parser.parse(new FileReader(file));
            }
            return (boolean) jsonObject.get("opened");
        } catch (Exception e){
            new ErrorHandler(e.toString());
            return false;
        }
    }

}
