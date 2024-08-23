package com.polsat.visualskript.gui.manager.view;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.block.SelectiveBlock;
import com.polsat.visualskript.gui.manager.view.blocks.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class DropViewExpr extends ViewBlock {

    public DropViewExpr(String text){

        hbox().thinLabel(text).glowing().margins();

        hBox.getChildren().add(label);
        this.getChildren().add(hBox);

        setOnDragOver(event -> {
            Block placedBlock = ((SelectiveBlock) event.getGestureSource()).getBlock();
            if (
                placedBlock.getType() == BlockType.STRUCTURE ||
                placedBlock.getType() == BlockType.EXPRESSION ||
                placedBlock.getType() == BlockType.TYPE ||
                placedBlock.getType() == BlockType.TYPE_LIST ||
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
            if (placedBlock.getType() == BlockType.STRUCTURE ||
                placedBlock.getType() == BlockType.EXPRESSION ||
                placedBlock.getType() == BlockType.TYPE ||
                placedBlock.getType() == BlockType.TYPE_LIST ||
                placedBlock.getType() == BlockType.FUNCTION ||
                placedBlock.getType() == BlockType.CONDITION)
            {
                switch (placedBlock.getType()){
                    case EXPRESSION -> new Expression(placedBlock, text).place(this);
                    case TYPE -> new Type(placedBlock, text).place(this);
                    case TYPE_LIST -> new TypeList(placedBlock, text).place(this);
                    case FUNCTION -> new Function(placedBlock, text, false).place(this);
                    case CONDITION -> new Conditions(placedBlock, text, false).place(this);
                    case STRUCTURE -> new Structure(placedBlock, text, true).place(this);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

}
