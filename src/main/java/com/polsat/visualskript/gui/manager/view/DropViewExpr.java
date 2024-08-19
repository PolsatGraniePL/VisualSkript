package com.polsat.visualskript.gui.manager.view;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.block.SelectiveBlock;
import com.polsat.visualskript.gui.manager.view.blocks.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class DropViewExpr extends Pane {

    DropViewExpr(String text){
        HBox hbox = new HBox();
        Label label = new Label();

        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(label);
        this.getChildren().add(hbox);

        this.setStyle("-fx-background-radius: 25px; -fx-background-color: #ffff00");
        HBox.setMargin(this, new Insets(5, 5, 5, 5));
        label.setText(text);
        label.setFont(new Font("System", 24));
        label.setPadding(new Insets(0, 5, 0, 5));

        setOnDragOver(event -> {
            Block placedBlock = ((SelectiveBlock) event.getGestureSource()).getBlock();
            if (
                placedBlock.getType() == BlockType.EXPRESSION ||
                placedBlock.getType() == BlockType.TYPE ||
                placedBlock.getType() == BlockType.FUNCTION ||
                placedBlock.getType() == BlockType.CONDITION)
            {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
        setOnDragDropped(event -> {
            Block placedBlock = ((SelectiveBlock) event.getGestureSource()).getBlock();
            boolean success = false;
            if (placedBlock.getType() == BlockType.EXPRESSION ||
                placedBlock.getType() == BlockType.TYPE ||
                placedBlock.getType() == BlockType.FUNCTION ||
                placedBlock.getType() == BlockType.CONDITION)
            {
                switch (placedBlock.getType()){
                    case EXPRESSION -> BlockPlacer.placeBlock(new Expression(placedBlock), this);
                    case TYPE -> BlockPlacer.placeBlock(new Type(placedBlock), this);
                    case FUNCTION -> BlockPlacer.placeBlock(new Function(placedBlock, false), this);
                    case CONDITION -> BlockPlacer.placeBlock(new Conditions(placedBlock, false), this);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

}
