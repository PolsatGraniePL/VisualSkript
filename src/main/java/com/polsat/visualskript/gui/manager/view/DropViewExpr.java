package com.polsat.visualskript.gui.manager.view;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.block.SelectiveBlock;
import com.polsat.visualskript.gui.manager.view.blocks.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class DropViewExpr extends Pane {

    DropViewExpr(String text){
        Label label = new Label();
        this.getChildren().add(label);
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
            if (
                placedBlock.getType() == BlockType.EXPRESSION ||
                placedBlock.getType() == BlockType.TYPE ||
                placedBlock.getType() == BlockType.FUNCTION ||
                placedBlock.getType() == BlockType.CONDITION)
            {
                Pane parent = (Pane) this.getParent();
                int index = parent.getChildren().indexOf(this);
                switch (placedBlock.getType()){
                    case EXPRESSION -> parent.getChildren().set(index, new Expression(placedBlock.getPattern(), placedBlock.getType()));
                    case TYPE -> parent.getChildren().set(index, new Type(placedBlock.getPattern(), placedBlock.getType()));
                    case FUNCTION -> parent.getChildren().set(index, new Function( placedBlock.getPattern(), placedBlock.getType()));
                    case CONDITION -> parent.getChildren().set(index, new Conditions(placedBlock.getPattern(), placedBlock.getType()));
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

}
