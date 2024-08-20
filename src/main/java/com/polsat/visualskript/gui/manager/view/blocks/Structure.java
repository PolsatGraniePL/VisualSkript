package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.drop.DropSystem;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.system.pattern.PatternExtractor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Objects;

public class Structure extends ViewBlock {

    private final ContextMenu contextMenu = new ContextMenu();
    private boolean contextMenuBuilt = false;

    private final boolean inExpression;

    public Structure(Block block, String oldText, boolean inExpression){
        super(block);
        this.inExpression = inExpression;
        HBox hbox = new HBox();
        Label label = new Label();
        if (inExpression){
            TextField textField = new TextField();

            this.setStyle("-fx-background-color: #"+ block.getType().getHexColor()+"; -fx-border-color: #000000; -fx-background-radius: 25px; -fx-border-radius: 25px;");
            HBox.setMargin(this, new Insets(5, 5, 5, 5));
            VBox.setMargin(this, new Insets(5, 5, 5, 5));
            hbox.setAlignment(Pos.CENTER);
            hbox.setFillHeight(false);
            label.setText("["+block.getName().substring(12)+"]");
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
        } else {
            //build view box

            this.setStyle("-fx-background-color: #"+ block.getType().getHexColor()+"; -fx-border-color: #000000; -fx-background-radius: 10px 10px 0 0; -fx-border-radius: 10px 10px 0 0;");
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
            label.setText("["+block.getType().getName()+"] " + PatternExtractor.getFirstPattern(block.getPattern()));
            label.setFont(new Font("System", 24));
            label.setPadding(new Insets(5, 5, 5, 5));

            hbox.getChildren().add(label);
            this.getChildren().add(hbox);

            this.setOnContextMenuRequested((e) -> {
                e.consume();
                if (!contextMenuBuilt) {
                    //TODO: ADD ITEMS ETC. (list block)or new block type.
                    MenuItem delete = new MenuItem("Delete");
                    contextMenu.getItems().add(delete);
                    delete.setOnAction(event -> {
                        if (this.getParent() instanceof VBox vbox) {
                            if (Objects.equals(block.getType(), BlockType.EVENT) || Objects.equals(block.getType(), BlockType.STRUCTURE)){
                                //TODO: DELETE CURRENT COMPONENT
                            } else {
                                vbox.getChildren().remove(this);
                            }
                        } else {
                            ((HBox)this.getParent()).getChildren().set(((HBox)this.getParent()).getChildren().indexOf(this), new DropViewExpr(oldText));
                        }
                    });
                }
                contextMenuBuilt = true;
                contextMenu.show(this, e.getScreenX(), e.getScreenY());
            });
        }
    }

    public boolean isInExpression() {
        return inExpression;
    }

}