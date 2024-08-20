package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.Main;
import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.system.pattern.PatternExtractor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Objects;

public class Type extends ViewBlock {

    private final ContextMenu contextMenu = new ContextMenu();
    private boolean contextMenuBuilt = false;

    public Type(Block block, String oldText){
        super(block);
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
        VBox.setMargin(this, new Insets(5, 5, 5, 5));
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

        hbox.getChildren().addAll(label, textField);
        this.getChildren().add(hbox);

        this.setOnContextMenuRequested((e) -> {
            e.consume();
            if (!contextMenuBuilt) {
                MenuItem delete = new MenuItem("Delete");
                contextMenu.getItems().addAll(delete);
                delete.setOnAction(event -> {
                    ((HBox)this.getParent()).getChildren().set(((HBox)this.getParent()).getChildren().indexOf(this), new DropViewExpr(oldText));
                });
            }
            contextMenuBuilt = true;
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });
    }

}