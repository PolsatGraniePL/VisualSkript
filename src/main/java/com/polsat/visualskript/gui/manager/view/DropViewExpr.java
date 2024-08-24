package com.polsat.visualskript.gui.manager.view;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
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
            if (((SelectiveBlock) event.getGestureSource()).getBlock().getType().getPlaceOnExpr())
            {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
        setOnDragDropped(event -> {
            Block placedBlock = ((SelectiveBlock) event.getGestureSource()).getBlock();
            boolean success = false;
            if (placedBlock.getType().getPlaceOnExpr())
            {
                placedBlock.getType().place(placedBlock, text, this);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

}
