package com.polsat.visualskript.gui.manager.view;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.view.popovers.SelectBoxPopOver;
import com.polsat.visualskript.system.pattern.PatternExtractor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.*;

public abstract class ViewBlock extends Pane implements Menu{

    protected ContextMenu contextMenu = new ContextMenu();

    protected final Block block;
    protected String oldText;
    protected Label label;

    protected ViewBlock(Block block){
        this.block = block;
    }

    protected ViewBlock(Block block, String oldText){
        this.block = block;
        this.oldText = oldText;

        //build view box
        HBox hbox = new HBox();
        label = new Label();

        this.setStyle("-fx-background-color: #"+ block.getType().getHexColor()+"; -fx-border-color: #000000; ");
        hbox.setAlignment(Pos.CENTER);
        hbox.setFillHeight(false);
        label.setText("["+block.getType().getName()+"] " + PatternExtractor.getFirstPattern(block.getPattern()));
        label.setFont(new Font("System", 24));
        label.setPadding(new Insets(5, 5, 5, 5));

        hbox.getChildren().add(label);
        this.getChildren().add(hbox);

        //Wait 0.01 second and show SelectBoxPopOver with patterns to select.
        new Timeline(new KeyFrame(Duration.seconds(0.01),
                event -> {
                    setCombinations(block.getPattern(), this, label, block.getType());
                })
        ).playFromStart();

        buildMenu();
        this.setOnContextMenuRequested((e) -> {
            e.consume();
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });
    }

    @Override
    public void buildMenu() {
        MenuItem edit = new MenuItem("Edit");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(edit, delete);
        edit.setOnAction(event -> {
            setCombinations(block.getPattern(), this, label, block.getType());
        });
        delete.setOnAction(event -> {
            if (this.getParent() instanceof VBox vbox) {
                vbox.getChildren().remove(this);
            } else {
                ((HBox)this.getParent()).getChildren().set(((HBox)this.getParent()).getChildren().indexOf(this), new DropViewExpr(oldText));
            }
        });
    }

    public Block getBlock() {
        return block;
    }

    //OTHER METHODS

    protected void setCombinations(String patterns, Pane pane, Label label, BlockType blockType) {
        List<String> patternList = Arrays.asList(patterns.split("\n"));
        if (patternList.size() == 1){
            //Show only popover with combinations
            List<String> combinationsList = PatternExtractor.getCombinations(PatternExtractor.getFirstPattern(patterns));
            Collections.reverse(combinationsList);
            Platform.runLater(()-> new SelectBoxPopOver(combinationsList, pane, result2 ->{
                Platform.runLater(() -> {
                    label.setText("["+blockType.getName()+"] " + result2);
                    setupDropViews(pane);
                });
            }));
        } else {
            //Show popovers with patterns and combinations
            new SelectBoxPopOver(patternList, pane, result -> {
                Platform.runLater(() -> label.setText("["+blockType.getName()+"] " + result));
                List<String> combinationsList = PatternExtractor.getCombinations(result);
                Collections.reverse(combinationsList);
                Platform.runLater(()-> new SelectBoxPopOver(combinationsList, pane, result2 ->{
                    Platform.runLater(() -> {
                        label.setText("["+blockType.getName()+"] " + result2);
                        setupDropViews(pane);
                    });
                }));
            });
        }
    }

    private void setupDropViews(Pane pane){
        HBox hBox = pane.getChildren().get(0) instanceof HBox ? (HBox)pane.getChildren().get(0) : (HBox) ((VBox)pane.getChildren().get(0)).getChildren().get(0);
        Label label = (Label) hBox.getChildren().get(0);
        String[] list = label.getText()
                .replace("<.+>", "%object%")
                .replace("<.*>", "%object%")
                .split("%");
        hBox.getChildren().remove(label);
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < list.length; i++){
            if (i % 2 == 0){
                Label tempLabel = new Label(list[i].trim());
                tempLabel.setFont(new Font("System", 24));
                tempLabel.setPadding(new Insets(5, 5, 5, 5));
                nodes.add(tempLabel);
            } else {
                nodes.add(new DropViewExpr(list[i].trim()));
            }
        }
        hBox.getChildren().addAll(0, nodes);
    }
}
