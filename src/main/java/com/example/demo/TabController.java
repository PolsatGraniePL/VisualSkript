package com.example.demo;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.File;

public class TabController {

    private static TabPane buildTabGlobal;

    public static void loadLatestTab(TabPane buildTab) {
        File folder = new File("src/main/resources/scripts");
        File[] filesList = folder.listFiles();
        for (File file : filesList) {
            if (file.getName().endsWith(".vsk")) {
                buildTab.getTabs().add(new Tab(file.getName()));
            }
        }
        buildTabGlobal = buildTab;
    }

    public static void addTab(String name){
        buildTabGlobal.getTabs().add(new Tab(name));
    }

    public static void removeTab(int id){
        buildTabGlobal.getTabs().remove(id);
    }


}
