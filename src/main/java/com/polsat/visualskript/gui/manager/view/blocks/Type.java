package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.Main;
import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.system.pattern.PatternExtractor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Objects;

public class Type extends ViewBlock {

    private final ContextMenu contextMenu = new ContextMenu();
    private boolean contextMenuBuilt = false;

    public Type(Block block){
        super(block, block.getName());
        //build view box
        HBox hbox = new HBox();
        Label label = new Label();
        TextField textField = new TextField();

        this.setStyle("-fx-background-color: #"+ block.getType().getHexColor()+"; -fx-border-color: #000000; -fx-background-radius: 25px; -fx-border-radius: 25px;");
        hbox.setAlignment(Pos.CENTER);
        hbox.setFillHeight(false);
        label.setText("["+block.getName().substring(8)+"]");
        label.setFont(new Font("System", 24));
        label.setPadding(new Insets(5, 5, 5, 5));
        HBox.setMargin(this, new Insets(5, 5, 5, 5));
        textField.setStyle("-fx-background-color: rgba(0,0,0,0)");
        textField.setFont(new Font("System", 24));
        textField.setPrefWidth(0);
        textField.setTranslateX(-5);
        textField.textProperty().addListener((ov, prevText, currText) -> {
            Platform.runLater(() -> {
                Text text = new Text(currText);
                text.setFont(textField.getFont());
                double width = text.getLayoutBounds().getWidth() + textField.getPadding().getLeft() + textField.getPadding().getRight() + 2d;
                textField.setPrefWidth(width);
                textField.positionCaret(textField.getCaretPosition());
            });
        });

        hbox.getChildren().addAll(label, textField);
        this.getChildren().add(hbox);

        if (!Objects.equals(block.getType(), BlockType.TYPE)) {
            //Wait 0.01 second and show SelectBoxPopOver with patterns to select.
            new Timeline(new KeyFrame(Duration.seconds(0.01),
                    event -> {
                        System.out.println("InputText");
                        //setCombinations(block.getPattern(), this, label, block.getType());
                    })
            ).playFromStart();
        }

        this.setOnContextMenuRequested((e) -> {
            if (!contextMenuBuilt) {
                MenuItem edit = new MenuItem("Edit");
                MenuItem delete = new MenuItem("Delete");
                contextMenu.getItems().addAll(edit, delete);
                edit.setOnAction(event -> {
                    System.out.println("InputText");
                    //setCombinations(block.getPattern(), this, label, block.getType());
                });
                delete.setOnAction(event -> {
                    System.out.println("Delete");
                });
            }
            contextMenuBuilt = true;
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });
    }

}