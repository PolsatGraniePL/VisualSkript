package com.polsat.visualskript.gui.manager.tabs;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabsManager {

    public static void addTab(String name, TabPane pane) {
        Tab tab = new Tab(name);
        pane.getTabs().add(tab);
        pane.getSelectionModel().selectLast();
        //tab.onclose(set opened to false)
    }
}
