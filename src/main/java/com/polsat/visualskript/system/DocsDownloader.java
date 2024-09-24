package com.polsat.visualskript.system;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

public class DocsDownloader {

    public static boolean start(){
        if (checkUpdate()){
            try {
                // Downloading the latest version of documentation
                URL url = new URL("https://raw.githubusercontent.com/SkriptLang/skript-docs/main/docs/docs.json");
                Scanner scanner = new Scanner(url.openStream());
                PrintWriter writer = new PrintWriter(DocsDownloader.class.getResource("/SkriptDocs.json").getFile());

                // Preparing downloaded documentation for reading and writing to the SkriptDocs.json file
                boolean isInDesc = false;
                writer.println("[");
                while (scanner.hasNext()){
                    String line = scanner.nextLine();
                    if (line.startsWith("  \"description\" : \"") && !line.endsWith("\",")) {
                        writer.println(Jsoup.parse("  \"description\" : [\"" + line.replace("  \"description\" : \"", "") + "\",").text());
                        isInDesc = true;
                    } else if (line.endsWith("\",") && isInDesc) {
                        writer.println(Jsoup.parse("\"" + line.replace("\",", "") + "\"],").text());
                        isInDesc = false;
                    }
                    else{
                        if (isInDesc){
                            writer.println(Jsoup.parse("\"" + line + "\",").text());
                        } else {
                            line = Jsoup.parse(line).text()
                                    .replace("\\\"", "%\\%")
                                    .replace("\"...\"", "\\\"...\\\"\"")
                                    .replace("\"...\"\"...\"", "\\\"...\\\"\\\"...\\\"\"")
                                    .replace("\"...%expression%...\"", "\\\"...%expression%...\\\"\"")
                                    .replace("\"...%%...\"", "\\\"...%%...\\\"\"")
                                    .replace("\"\"", "\"")
                                    .replace("%\\%", "\\\"")
                                    .replace("\", e.g. \"", "\\\", e.g. \\\"")
                                    .replace("\"<.*>\"", "\\\"<.*>\\\"");
                            writer.println(line);
                        }
                    }
                }
                writer.println("]");
                writer.close();
                scanner.close();
                return true;

            } catch (Exception ignore) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkUpdate(){
        try {
            //Downloading the latest version of documentation and current user documentation
            Scanner scannerURL = new Scanner(new URL("https://raw.githubusercontent.com/SkriptLang/skript-docs/main/docs/docs.json").openStream());
            Scanner scannerFile = new Scanner(new File(DocsDownloader.class.getResource("/SkriptDocs.json").getFile()));

            scannerFile.nextLine();
            scannerFile.nextLine();
            String fileVersion = scannerFile.nextLine().replace(" ", "");

            scannerURL.nextLine();
            String URLVersion = scannerURL.nextLine().replace(" ", "");

            scannerURL.close();
            scannerFile.close();
            // Comparing lines with version
            return !Objects.equals(fileVersion, URLVersion);
        }
        catch (Exception e){
            return true;
        }
    }

}
