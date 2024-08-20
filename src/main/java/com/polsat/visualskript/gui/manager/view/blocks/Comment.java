package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.drop.DropSystem;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Comment extends ViewBlock {

    private final ContextMenu contextMenu = new ContextMenu();
    private boolean contextMenuBuilt = false;

    public Comment(Block block) {
        super(block);
        //build view box
        HBox hbox = new HBox();
        Label label = new Label();
        TextField textField = new TextField();

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
        label.setText("["+block.getName().substring(10)+"]");
        label.setFont(new Font("System", 24));
        label.setPadding(new Insets(5, 5, 5, 5));
        HBox.setMargin(textField, new Insets(5, 5, 5, 5));

        textField.setStyle("-fx-background-radius: 25px; -fx-background-color: #c6c6c6; -fx-border-radius: 25px; -fx-focus-color: transparent; -fx-border-color: #000000");
        textField.setFont(new Font("System", 20));
        textField.setTranslateX(-5);
        textField.setPrefWidth(25);
        textField.textProperty().addListener((ov, prevText, currText) -> Platform.runLater(() -> {
            Text text = new Text(currText);
            text.setFont(textField.getFont());
            double width = text.getLayoutBounds().getWidth() + textField.getPadding().getLeft() + textField.getPadding().getRight() + 4d;
            textField.setPrefWidth(width);
            textField.positionCaret(textField.getCaretPosition());
        }));
        textField.setOnAction((event -> {
            BlockPlacer.placeBlock(new Comment(block), this.getParent());
        }));

        hbox.getChildren().addAll(label, textField);
        this.getChildren().add(hbox);

        new Timeline(new KeyFrame(Duration.seconds(0.01), event -> textField.requestFocus())).playFromStart();

        this.setOnContextMenuRequested((e) -> {
            if (!contextMenuBuilt) {
                MenuItem delete = new MenuItem("Delete");
                delete.setOnAction(event -> ((VBox)this.getParent()).getChildren().remove(this));
            }
            contextMenuBuilt = true;
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });
    }
}
