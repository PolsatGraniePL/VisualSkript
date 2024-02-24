package com.polsat.visualskript.gui.manager;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.File;
import java.util.Objects;

public class TabManager {

    private static TabPane buildTabGlobal;

    public static void loadLatestTab(TabPane buildTab) {
        buildTabGlobal = buildTab;
        File folder = new File(ScriptsManager.getScriptPathFolder());
        File[] filesList = folder.listFiles();
        for (File file : filesList) {
            if (file.getName().endsWith(".vsk")) {
                TabManager.addTab(file.getName());
            }
        }
    }

    public static void addTab(String name){
        buildTabGlobal.getTabs().add(new Tab(name));
        buildTabGlobal.getSelectionModel().selectLast();
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
