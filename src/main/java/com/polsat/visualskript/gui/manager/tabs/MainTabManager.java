package com.polsat.visualskript.gui.manager.tabs;

import com.polsat.visualskript.gui.manager.FileManager;
import com.polsat.visualskript.gui.manager.ScriptsManager;
import com.polsat.visualskript.system.script.ScriptJsonManager;
import com.polsat.visualskript.util.ErrorHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.File;
import java.util.Objects;

public class MainTabManager {

    private static TabPane buildTabGlobal;

    public static void loadLatestTab(TabPane buildTab) {
        buildTabGlobal = buildTab;
        File folder = new File(ScriptsManager.getScriptPathFolder());
        File[] filesList = folder.listFiles();
        try {
            for (File file : filesList) {
                if (file.getName().endsWith(".sk") && ScriptJsonManager.getOpened(file)) {
                    MainTabManager.addTab(file.getName());
                }
            }
        } catch (Exception e){
            ErrorHandler.alert(e.toString());
        }

    }

    public static void addTab(String name){
        Tab tab = new Tab(name);
        TabPane tabPane = new TabPane();
        buildTabGlobal.getTabs().add(tab);
        buildTabGlobal.getSelectionModel().selectLast();
        tab.setStyle("-fx-background-radius: 0px; -fx-background-insets: 0 1 0 0, 1 2 1 1, 2 3 1 2");
        tab.setContent(tabPane);
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
