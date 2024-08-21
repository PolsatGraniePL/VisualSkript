package com.polsat.visualskript.gui.manager.view;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.block.SelectiveBlock;
import com.polsat.visualskript.gui.manager.drop.DropSystem;
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

public class DropViewExpr extends Pane {

    public DropViewExpr(String text){
        HBox hbox = new HBox();
        Label label = new Label();

        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(label);
        this.getChildren().add(hbox);

        this.setOnDragEntered(event -> setEffect(new Glow(0.3)));
        this.setOnDragExited(event -> setEffect(null));
        this.setStyle("-fx-background-radius: 25px; -fx-background-color: #ffc0cb; -fx-border-color: #000000; -fx-border-radius: 25px;");
        HBox.setMargin(this, new Insets(5, 5, 5, 5));
        VBox.setMargin(this, new Insets(5, 5, 5, 5));
        label.setText(text);
        label.setFont(new Font("System", 24));
        label.setPadding(new Insets(0, 5, 0, 5));

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
