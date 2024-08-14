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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class ViewBlock {

    protected ViewBlock(VBox vbox, String patterns, BlockType blockType){
        //build view box
        Pane pane = new Pane();
        HBox hbox = new HBox();
        Label label = new Label();
        Region region = new Region();
        Button button = new Button();
        ImageView imageView = new ImageView();

        pane.setStyle("-fx-background-color: #"+ blockType.getHexColor()+";");
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
        pane.getChildren().add(hbox);

        //add pane to current VBox.
        vbox.getChildren().add(pane);

        //Wait 0.01 second and show SelectBoxPopOver with patterns to select.
        new Timeline(new KeyFrame(Duration.seconds(0.01),
                event -> {
                    setCombinations(patterns, pane, label, blockType);
                })
        ).playFromStart();

        pane.setOnContextMenuRequested((e)->{
            ContextMenu contextMenu = new ContextMenu();
            MenuItem edit = new MenuItem("Edit");
            MenuItem delete = new MenuItem("Delete");
            contextMenu.getItems().addAll(edit, delete);
            edit.setOnAction(event -> {
                ViewBlock.setCombinations(patterns, pane, label, blockType);
            });
            delete.setOnAction(event -> {
                System.out.println("Delete");
            });
            contextMenu.show(pane, e.getScreenX(), e.getScreenY());
        });
        button.setOnMouseClicked((mouseEvent)->{
            setCombinations(patterns, pane, label, blockType);
        });
    }

    public static void setCombinations(String patterns, Pane pane, Label label, BlockType blockType) {
        List<String> patternList = Arrays.asList(patterns.split("\n"));
        if (patternList.size() == 1){
            //Show only popover with combinations
            List<String> combinationsList = PatternExtractor.getCombinations(PatternExtractor.getFirstPattern(patterns));
            Collections.reverse(combinationsList);
            Platform.runLater(()-> new SelectBoxPopOver(combinationsList, pane, result2 ->{
                Platform.runLater(() -> label.setText("["+blockType.getName()+"] " + result2));
            }));
        } else {
            //Show popovers with patterns and combinations
            new SelectBoxPopOver(patternList, pane, result -> {
                Platform.runLater(() -> label.setText("["+blockType.getName()+"] " + result));
                List<String> combinationsList = PatternExtractor.getCombinations(result);
                Collections.reverse(combinationsList);
                Platform.runLater(()-> new SelectBoxPopOver(combinationsList, pane, result2 ->{
                    Platform.runLater(() -> label.setText("["+blockType.getName()+"] " + result2));
                }));
            });
        }
    }

}
