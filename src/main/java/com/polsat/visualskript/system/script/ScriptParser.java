package com.polsat.visualskript.system.script;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;

public class ScriptParser {

    public static void parse(File file){

    }
    public static void makeVSkript(File file){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("opened", true);
        jsonObject.put("blocks", new JSONArray());
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonObject.toJSONString());
            writer.close();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
