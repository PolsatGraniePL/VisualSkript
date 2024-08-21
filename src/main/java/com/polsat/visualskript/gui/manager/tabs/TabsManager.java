package com.polsat.visualskript.gui.manager.tabs;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.block.SelectiveBlock;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.blocks.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.beans.Visibility;

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
            Block placedBlock = ((SelectiveBlock) event.getGestureSource()).getBlock();
            if (placedBlock.getType() == BlockType.SECTION ||
                    placedBlock.getType() == BlockType.EFFECT ||
                    placedBlock.getType() == BlockType.FUNCTION ||
                    placedBlock.getType() == BlockType.COMMENT ||
                    placedBlock.getType() == BlockType.CONDITION )
            {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
        vBox.setOnDragDropped(event -> {
            Block placedBlock = ((SelectiveBlock) event.getGestureSource()).getBlock();
            boolean success = false;
            if (placedBlock.getType() == BlockType.SECTION ||
                placedBlock.getType() == BlockType.EFFECT ||
                placedBlock.getType() == BlockType.FUNCTION ||
                placedBlock.getType() == BlockType.COMMENT ||
                placedBlock.getType() == BlockType.CONDITION )
            {
                switch (placedBlock.getType()){
                    case SECTION -> new Section(placedBlock).place(vBox);
                    case EFFECT -> new Effect(placedBlock).place(vBox);
                    case COMMENT -> new Comment(placedBlock).place(vBox);
                    case FUNCTION -> new Function(placedBlock, null, true).place(vBox);
                    case CONDITION -> new Conditions(placedBlock, null, true).place(vBox);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }
}
