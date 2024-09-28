package com.polsat.visualskript.system.pattern;

import com.polsat.visualskript.util.ErrorHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
        try {
            PatternNode parsedPattern = PatternParser.parse(pattern);
            combinations.addAll(PatternParser.cleanCombinations(PatternParser.getCombinations(parsedPattern)));
        } catch (Exception e) {
            e.printStackTrace();
            ErrorHandler.alert(e.toString());
        }
        return combinations;
    }
}
