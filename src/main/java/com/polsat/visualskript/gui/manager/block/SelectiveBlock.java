package com.polsat.visualskript.gui.manager.block;

import com.polsat.visualskript.gui.block.Block;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SelectiveBlock extends Pane {

    private final Block block;

    SelectiveBlock(Block block){
        this.block = block;

        //Create objects
        Label tmpLabel = new Label(block.getName());

        //Style objects
        this.getChildren().add(tmpLabel);
        this.setStyle("-fx-border-color:  #020202; -fx-border-radius: 5px ; -fx-background-color: #" + block.getType().getHexColor() + "; -fx-background-radius: 5px; -fx-border-width: 1px");
        tmpLabel.setStyle("-fx-font-color: #000000; -fx-font-weight: bold; -fx-font-size: 14px;");
        tmpLabel.setTooltip(new Tooltip(block.getDescription() + "\n\n" + block.getPattern()));

        VBox.setMargin(this, new Insets(10, 10, 10, 10));
        tmpLabel.setPadding(new Insets(5, 5, 5, 5));

        this.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.setMinWidth(Region.USE_PREF_SIZE);
        this.setMaxWidth(Region.USE_PREF_SIZE);

        //Drag objects system
        this.setOnDragDetected(event -> {
            Dragboard dragboard = this.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("");
            dragboard.setContent(content);
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            dragboard.setDragView(this.snapshot(snapshotParameters, null));

            event.consume();
        });
    }

    public Block getBlock() {
        return block;
    }
}
