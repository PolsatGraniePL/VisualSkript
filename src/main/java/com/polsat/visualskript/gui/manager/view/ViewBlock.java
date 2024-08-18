package com.polsat.visualskript.gui.manager.view;

import com.polsat.visualskript.Main;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class ViewBlock extends Pane {

    private final ContextMenu contextMenu = new ContextMenu();
    private boolean contextMenuBuilt = false;

    protected ViewBlock(String patterns, BlockType blockType){
        //build view box
        HBox hbox = new HBox();
        Label label = new Label();
        Region region = new Region();
        Button button = new Button();
        ImageView imageView = new ImageView();

        this.setStyle("-fx-background-color: #"+ blockType.getHexColor()+";");
        label.setText("["+blockType.getName()+"] " + PatternExtractor.getFirstPattern(patterns));
        label.setFont(new Font("System", 24));
        label.setPadding(new Insets(5, 5, 5, 5));
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: rgba(0,0,0,0)");
        button.setPrefSize(25, 25);
        button.setAlignment(Pos.CENTER);
        button.setContentDisplay(ContentDisplay.TOP);
        button.setGraphic(imageView);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        imageView.setImage(new Image(String.valueOf(Main.class.getResource("/images/lines.png"))));

        hbox.getChildren().addAll(label, region, button);
        this.getChildren().add(hbox);

        //Wait 0.01 second and show SelectBoxPopOver with patterns to select.
        new Timeline(new KeyFrame(Duration.seconds(0.01),
                event -> {
                    setCombinations(patterns, this, label, blockType);
                })
        ).playFromStart();

        this.setOnContextMenuRequested((e)->{
            if (!contextMenuBuilt) {
                MenuItem edit = new MenuItem("Edit");
                MenuItem delete = new MenuItem("Delete");
                contextMenu.getItems().addAll(edit, delete);
                edit.setOnAction(event -> {
                    setCombinations(patterns, this, label, blockType);
                });
                delete.setOnAction(event -> {
                    System.out.println("Delete");
                });
            }
            contextMenuBuilt = true;
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });

        button.setOnMouseClicked((mouseEvent)->{
            setCombinations(patterns, this, label, blockType);
        });
    }

    private void setCombinations(String patterns, Pane pane, Label label, BlockType blockType) {
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
        HBox hBox = (HBox) pane.getChildren().get(0);
        Label label = (Label) hBox.getChildren().get(0);
        String[] list = label.getText().split("%");
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
