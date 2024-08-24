package com.polsat.visualskript.gui.manager.tabs;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.SelectiveBlock;
import com.polsat.visualskript.gui.manager.view.blocks.*;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TabsManager {

    public static void addTab(String name, TabPane pane) {
        Tab tab = new Tab(name);
        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();
        Pane expandPane = new Pane();

        pane.getTabs().add(tab);
        pane.getSelectionModel().selectLast();
        pane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);

        tab.setContent(scrollPane);
        tab.setStyle("-fx-background-radius: 0px");
        //tab.onclose(set opened to false)

        scrollPane.setContent(vBox);
        scrollPane.setFitToHeight(false);
        scrollPane.setFitToWidth(false);
        scrollPane.setPadding(new Insets(10, 10, 10, 10));
        expandPane.setPrefHeight(300);

        vBox.setFillWidth(true);
        vBox.getChildren().add(expandPane);

        vBox.setOnDragOver(event -> {
            if (((SelectiveBlock) event.getGestureSource()).getBlock().getType().getPlaceOnVBox())
            {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
        vBox.setOnDragDropped(event -> {
            Block placedBlock = ((SelectiveBlock) event.getGestureSource()).getBlock();
            boolean success = false;
            if (placedBlock.getType().getPlaceOnVBox())
            {
                placedBlock.getType().place(placedBlock, null, vBox);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }
}
