package com.polsat.visualskript.system.script;

import com.polsat.visualskript.util.ErrorHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class ScriptParser {



    // JSON TO Skript
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
                        System.out.println("Info: " + ((JSONObject)((JSONArray) structureList).get(0)).get("Info"));
                        latestDepth = 0;
                        for (Object itemList : ((JSONArray) structureList)){
                            if (Objects.isNull(((JSONObject) itemList).get("Info")))
                                System.out.println(separateStringAndJSONObject((JSONObject) itemList, 1));
                        }
                    }
                }
            }
        } catch (Exception e){
            new ErrorHandler(e.toString());
        }
    }

    static ArrayList<String> listAll = new ArrayList<>(Arrays.asList("Event", "Effect", "Section", "Conditionals", "Expression", "Structure"));
    static ArrayList<String> listNewLine = new ArrayList<>(Arrays.asList("Effect", "Section", "Structure", "Event"));
    static int latestDepth = 0;

    private static String separateStringAndJSONObject(JSONObject json, int depth){
        StringBuilder stringBuilder = new StringBuilder();
        String tabs = "\t".repeat(Math.max(0, depth));
        try {
            for (Object key : json.keySet()){
                String keyStr = (String)key;
                Object keyValue = json.get(keyStr);
                for (Object key2 : (JSONArray) keyValue){
                    JSONObject object = (JSONObject)key2;
                    for (Object x : object.keySet()){
                        String key2Str = (String)x;
                        Object key2Value = object.get(key2Str);
                        if (listAll.contains(key2Str)){
                            if (listNewLine.contains(key2Str)){
                                if (latestDepth < depth){
                                    stringBuilder.append(":\n").append(tabs).append(separateStringAndJSONObject(object, depth+1));
                                } else {
                                    stringBuilder.append("\n").append(tabs).append(separateStringAndJSONObject(object, depth+1));
                                }
                                latestDepth = depth;
                            } else {
                                stringBuilder.append(separateStringAndJSONObject(object, depth)).append(" ");
                            }
                        } else {
                            switch (key2Str){
                                case "Text":
                                    stringBuilder.append("\"").append(key2Value.toString()).append("\"").append(" ");
                                    break;
                                case "Variable":
                                    stringBuilder.append("{").append(key2Value.toString()).append("}").append(" ");
                                    break;
                                case "Options":
                                    stringBuilder.append("{@").append(key2Value.toString()).append("}").append(" ");
                                    break;
                                case "World":
                                    stringBuilder.append("world \"").append(key2Value.toString()).append("\"").append(" ");
                                    break;
                                default:
                                    stringBuilder.append(key2Value.toString()).append(" ");
                            }
                        }
                    }
                }
            }
            return stringBuilder.toString().trim();
        }
        catch (Exception e){
            new ErrorHandler(e.toString());
            return "Error";
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
            new ErrorHandler(e.toString());
        }
    }

}
