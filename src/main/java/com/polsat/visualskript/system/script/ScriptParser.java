package com.polsat.visualskript.system.script;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class ScriptParser {



    // JSON TO TEXT-skript
    public static void build(File file){

        try {
            StringBuilder jsonString = new StringBuilder();
            Scanner reader = new Scanner(file);
            while (reader.hasNext()){
                jsonString.append(reader.next());
            }

            JSONParser parser = new JSONParser();
            JSONObject mainObject = (JSONObject) parser.parse(jsonString.toString());

            for (Object key : mainObject.keySet()){

                String keyStr = (String)key;
                Object keyValue = mainObject.get(keyStr);

                if (Objects.equals(keyStr, "blocks")){
                    for (Object key2 : ((JSONArray) keyValue)){
                        Object obj = key2;
                        System.out.println(separateStringAndJSONArray((JSONObject) obj));
                    }
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    ArrayList<String> list = new ArrayList<>(Arrays.asList("Event", "Effect", "Section", "Conditionals", "Expression", ""));

    private static String separateStringAndJSONArray(JSONObject json){
        try {
            for (Object key : json.keySet()){
                String keyStr = (String)key;
                Object keyValue = json.get(keyStr);
                JSONParser parser = new JSONParser();
                JSONArray arraySection = (JSONArray) parser.parse(keyValue.toString());
                for (Object key2 : arraySection){
                    JSONObject object = (JSONObject)key2;
                    for (Object x : object.keySet()){
                        if (x == "" || "" || ""){

                        } else {
                            System.out.println("String: ");
                        }
                    }
                    System.out.println("[" + keyStr + "] " + object);
                }
            }
            return "%nl%";
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    //JSON to visual language
    public static void read(File file) {

    }

    //Empty file to json
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
