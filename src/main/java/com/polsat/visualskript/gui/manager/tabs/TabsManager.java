package com.polsat.visualskript.gui.manager.tabs;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class TabsManager {

    public static void addTab(String name, TabPane pane) {
        Tab tab = new Tab(name);
        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();

        pane.getTabs().add(tab);
        pane.getSelectionModel().selectLast();

        tab.setContent(scrollPane);
        //tab.onclose(set opened to false)

        scrollPane.setContent(vBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        vBox.setFillWidth(false);
    }
}
