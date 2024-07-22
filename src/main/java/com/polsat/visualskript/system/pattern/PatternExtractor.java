package com.polsat.visualskript.system.pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class PatternExtractor {

    public static String getFirstPattern(String input) {
        String[] pattern = input.split("\n");
        String firstPattern = pattern[0];

        return input;
    }

    public static String[] downloadCombinations(String pattern){
        try {
            String url = URLEncoder.encode(RepairChar.applyTempChar(pattern), StandardCharsets.UTF_8);
            String data = Jsoup.connect("https://rcgc.pl/VisuakSkript/api/index.php?pattern="+url).ignoreContentType(true).execute().body();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(data);
            JSONArray array = (JSONArray) json.get("combinations");
            for (Object obj : array){
                String str = (String) obj;
                System.out.println(str);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return new String[0];
        //"neither %objects% \u003E %objects%"
    }

    static class RepairChar{
        public static String applyTempChar(String input){
            return input.replace(">", "›").replace("<", "‹");
        }
        public static String repairTempChar(String input){
            return input.replace("›", ">").replace("‹", "<");
        }
    }

}
