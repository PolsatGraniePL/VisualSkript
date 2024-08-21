package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.block.SelectiveBlock;
import com.polsat.visualskript.gui.manager.drop.DropSystem;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.placeable;
import com.polsat.visualskript.system.pattern.PatternExtractor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Glow;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Objects;

public class Section extends ViewBlock implements placeable {

    private final ContextMenu contextMenu = new ContextMenu();
    private boolean contextMenuBuilt = false;

    private final VBox dropVBox;

    public Section(Block block){
        super(block);
        this.dropVBox = new VBox();

        //build view box
        VBox vbox = new VBox();
        Label label = new Label();
        HBox hbox = new HBox();
        Pane emptyPane = new Pane();

        this.setStyle("-fx-background-color: #"+ block.getType().getHexColor()+"; -fx-border-color: #000000; ");
        this.setOnDragEntered(event -> {
            DropSystem.setCurrentDropUnderNode(this);
            setEffect(new Glow(0.3));
        });
        this.setOnDragExited(event -> {
            DropSystem.setCurrentDropUnderNode(null);
            setEffect(null);
        });
        hbox.setAlignment(Pos.CENTER);
        hbox.setFillHeight(false);
        vbox.setFillWidth(false);
        dropVBox.setFillWidth(true);
        VBox.setMargin(dropVBox, new Insets(0, 5, 5, 30));
        label.setText("["+block.getType().getName()+"] " + PatternExtractor.getFirstPattern(block.getPattern()));
        label.setFont(new Font("System", 24));
        label.setPadding(new Insets(5, 5, 5, 5));

        dropVBox.getChildren().add(emptyPane);
        vbox.getChildren().addAll(hbox, dropVBox);
        hbox.getChildren().add(label);
        this.getChildren().add(vbox);

        //Wait 0.01 second and show SelectBoxPopOver with patterns to select.
        new Timeline(new KeyFrame(Duration.seconds(0.01),
                event -> {
                    setCombinations(block.getPattern(), this, label, block.getType());
                })
        ).playFromStart();

        this.setOnContextMenuRequested((e) -> {
            e.consume();
            if (!contextMenuBuilt) {
                MenuItem edit = new MenuItem("Edit");
                MenuItem delete = new MenuItem("Delete");
                contextMenu.getItems().addAll(edit, delete);
                edit.setOnAction(event -> {
                    setCombinations(block.getPattern(), this, label, block.getType());
                });
                delete.setOnAction(event -> {
                    if (this.getParent() instanceof VBox vboxMain) {
                        vboxMain.getChildren().remove(this);
                    }
                });
            }
            contextMenuBuilt = true;
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });

        dropVBox.setOnDragOver(event -> {
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
        dropVBox.setOnDragDropped(event -> {
            Block placedBlock = ((SelectiveBlock) event.getGestureSource()).getBlock();
            boolean success = false;
            if (placedBlock.getType() == BlockType.SECTION ||
                    placedBlock.getType() == BlockType.EFFECT ||
                    placedBlock.getType() == BlockType.FUNCTION ||
                    placedBlock.getType() == BlockType.COMMENT ||
                    placedBlock.getType() == BlockType.CONDITION )
            {
                switch (placedBlock.getType()){
                    case SECTION -> new Section(placedBlock).place(dropVBox);
                    case EFFECT -> new Effect(placedBlock).place(dropVBox);
                    case COMMENT -> new Comment(placedBlock).place(dropVBox);
                    case FUNCTION -> new Function(placedBlock, null, true).place(dropVBox);
                    case CONDITION -> new Conditions(placedBlock, null, true).place(dropVBox);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public VBox getDropVBox() {
        return dropVBox;
    }

    @Override
    public void place(Node node) {
        BlockPlacer.placeOnVBox(this, node);
    }
}