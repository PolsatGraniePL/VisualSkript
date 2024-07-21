package com.polsat.visualskript.gui.manager.tabs;

import javafx.geometry.Insets;
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
        pane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);

        tab.setContent(scrollPane);
        tab.setStyle("-fx-background-radius: 0px");
        //tab.onclose(set opened to false)

        scrollPane.setContent(vBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10, 10, 10, 10));

        vBox.setFillWidth(false);
    }
}
