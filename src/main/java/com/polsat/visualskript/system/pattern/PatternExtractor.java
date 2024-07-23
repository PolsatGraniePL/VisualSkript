package com.polsat.visualskript.system.pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PatternExtractor {

    public static String getFirstPattern(String patterns) {
        String[] pattern = patterns.split("\n");
        return pattern[0];
    }

    public static String getFirstCombination(String pattern) {
        ArrayList<String> combinations = getCombinations(pattern);
        return combinations.get(0);
    }

    public static String getLatestCombination(String pattern) {
        ArrayList<String> combinations = getCombinations(pattern);
        return combinations.get(combinations.size() - 1);
    }

    public static ArrayList<String> getCombinations(String pattern) {
        ArrayList<String> combinations = new ArrayList<>();
        for (Object obj : downloadCombinations(pattern)){
            combinations.add((String) obj);
        }
        return combinations;
    }

    public static JSONArray downloadCombinations(String pattern){
        try {
            String url = URLEncoder.encode(pattern, StandardCharsets.UTF_8);
            String data = Jsoup.connect("https://rcgc.pl/VisuakSkript/api/index.php?pattern="+url).ignoreContentType(true).execute().body();
            JSONObject json = (JSONObject) new JSONParser().parse(data);
            JSONArray array = (JSONArray) json.get("combinations");
            return array;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
