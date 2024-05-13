package com.polsat.visualskript.gui.manager;

import com.polsat.visualskript.gui.manager.block.BlockManager;
import com.polsat.visualskript.system.script.ScriptJsonManager;
import com.polsat.visualskript.system.script.ScriptParser;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Objects;

public class TabManager {

    private static TabPane buildTabGlobal;

    public static void loadLatestTab(TabPane buildTab) {
        buildTabGlobal = buildTab;
        File folder = new File(ScriptsManager.getScriptPathFolder());
        File[] filesList = folder.listFiles();
        try {
            for (File file : filesList) {
                if (file.getName().endsWith(".vsk")) {
                    if (ScriptJsonManager.getOpened(file)){
                        TabManager.addTab(file.getName());
                    }
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public static void addTab(String name){
        Tab tab = new Tab(name);
        buildTabGlobal.getTabs().add(tab);
        buildTabGlobal.getSelectionModel().selectLast();
        tab.setOnClosed(event -> ScriptJsonManager.setOpened(FileManager.getFileByName(name), false));
    }

    public static void removeTab(int id){
        buildTabGlobal.getTabs().remove(id);
        buildTabGlobal.getSelectionModel().selectLast();
    }
    public static void removeTab(String name){
        for (int i = 0; i < buildTabGlobal.getTabs().size(); i++) {
            if (Objects.equals(name, buildTabGlobal.getTabs().get(i).getText())){
                buildTabGlobal.getTabs().remove(i);
                return;
            }
        }
        buildTabGlobal.getSelectionModel().selectLast();
    }
}
