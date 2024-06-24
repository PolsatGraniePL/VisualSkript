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
            while (reader.hasNextLine()){
                jsonString.append(reader.nextLine());
            }
            reader.close();

            JSONParser parser = new JSONParser();
            JSONObject mainObject = (JSONObject) parser.parse(jsonString.toString());

            for (Object key : mainObject.keySet()){

                String keyStr = (String)key;
                Object keyValue = mainObject.get(keyStr);

                if (Objects.equals(keyStr, "structures")){
                    for (Object structureList : ((JSONArray) keyValue)){
                        System.out.println("XXX: " + ((JSONObject)((JSONArray) structureList).get(0)).get("Info"));
                        for (Object itemList : ((JSONArray) structureList)){
                            Object obj = itemList;
                            if (Objects.isNull(((JSONObject) obj).get("Info")))
                                System.out.println(separateStringAndJSONArray((JSONObject) obj));
                        }
                    }
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    static ArrayList<String> list = new ArrayList<>(Arrays.asList("Event", "Effect", "Section", "Conditionals", "Expression", "Structure", "Info"));

    private static String separateStringAndJSONArray(JSONObject json){
        try {
            for (Object key : json.keySet()){
                String keyStr = (String)key;
                Object keyValue = json.get(keyStr);
                for (Object key2 : (JSONArray) keyValue){
                    JSONObject object = (JSONObject)key2;
                    for (Object x : object.keySet()){
                        String key2Str = (String)x;
                        Object key2Value = object.get(key2Str);
                        if (list.contains(x)){
                            return separateStringAndJSONArray(object);
                        } else {
                            //return "["+keyStr+"|"+key2+"|"+key2Str + "] " +  key2Value.toString();
                            return key2Value.toString();
                        }
                    }
                }
            }
            return "none";
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
