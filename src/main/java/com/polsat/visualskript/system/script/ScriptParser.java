package com.polsat.visualskript.system.script;

import com.polsat.visualskript.util.ErrorHandler;

import java.io.File;

public class ScriptParser {

    //Load .sk to visual skript
    public static void load(File file){
        ScriptManager.setOpened(file, true);

        //TODO: generate TabPane & viewblocks
    }

    //Build visual skript to .sk
    public static void build(){
        try {
            BuildThread build = new BuildThread();
            build.setName("Build");
            build.start();
        } catch (Exception e) {
            ErrorHandler.alert(e.toString());
        }
    }
}
