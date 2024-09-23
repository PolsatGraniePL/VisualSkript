package com.polsat.visualskript.system.script;

import com.polsat.visualskript.util.ErrorHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ScriptManager {

    public static void setOpened(File file, boolean status){
        try {
            Scanner scanner = new Scanner(file);
            ArrayList<String> list = new ArrayList<>();

            while (scanner.hasNextLine()) {
                list.add(scanner.nextLine());
            }
            scanner.close();

            if (!list.isEmpty() && list.get(0).startsWith("#status: ")) {
                list.remove(0);
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("#status: "+status + "\n");
            for (String line : list) {
                fileWriter.write(line + "\n");
            }
            fileWriter.close();
        } catch (Exception e){
            ErrorHandler.alert(e.toString());
        }
    }

    public static boolean getOpened(File file){
        try {
            Scanner scanner = new Scanner(file);
            ArrayList<String> list = new ArrayList<>();

            while (scanner.hasNextLine()) {
                list.add(scanner.nextLine());
            }
            scanner.close();

            return Boolean.parseBoolean(list.get(0).replace("#status: ",""));
        } catch (Exception e){
            return false;
        }
    }

}
