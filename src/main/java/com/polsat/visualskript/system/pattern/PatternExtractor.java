package com.polsat.visualskript.system.pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PatternExtractor {

    public static String getFirstPattern(String input) {
        String[] pattern = input.split("\n");
        String firstPattern = pattern[0];

        return input;
    }

    public static void main(String[] args) {
        try {
            String url = URLEncoder.encode("[on] [player['s]] (tool|item held|held item) chang(e|ing)", StandardCharsets.UTF_8);
            Document doc = Jsoup.connect("https://docs.skunity.com/patterns-generator?syntax="+url).get();
            String patterns = doc.getElementsByClass("s t wc hc").first().text();
            System.out.println(patterns);
            System.out.println("-=-=-");
            System.out.println(getFirstPattern(patterns));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
