package com.polsat.visualskript.gui.manager.view;

import com.polsat.visualskript.Main;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.system.pattern.PatternExtractor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
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

// TODO: WSZYSTKO NA OBIEKTOWOŚĆ JEBNĄĆ

public class ViewBlock {

    public static void addEvent(VBox vbox, String patterns){
        //build view box
        Pane pane = new Pane();
        HBox hbox = new HBox();
        Label label = new Label();
        Region region = new Region();
        Button button = new Button();
        ImageView imageView = new ImageView();

        //TODO: After clicking the button, SelectBoxPopOver opens

        pane.setStyle("-fx-background-color: #"+ BlockType.EVENT.getHexColor() +";");
        label.setText("[Event] " + PatternExtractor.getFirstPattern(patterns));
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
                setCombinations(patterns, pane, label);
            })
        ).playFromStart();

        button.setOnMouseClicked((mouseEvent)->{
            setCombinations(patterns, pane, label);
        });

    }

    private static void setCombinations(String patterns, Pane pane, Label label) {
        List<String> patternList = Arrays.asList(patterns.split("\n"));
        if (patternList.size() == 1){
            //Show only popover with combinations
            List<String> combinationsList = PatternExtractor.getCombinations(PatternExtractor.getFirstPattern(patterns));
            Collections.reverse(combinationsList);
            Platform.runLater(()-> new SelectBoxPopOver().Show(combinationsList, pane, result2 ->{
                Platform.runLater(() -> label.setText("[Event] " + result2));
            }));
        } else {
            //Show popovers with patterns and combinations
            new SelectBoxPopOver().Show(patternList, pane, result -> {
                Platform.runLater(() -> label.setText("[Event] " + result));
                List<String> combinationsList = PatternExtractor.getCombinations(result);
                Collections.reverse(combinationsList);
                Platform.runLater(()-> new SelectBoxPopOver().Show(combinationsList, pane, result2 ->{
                    Platform.runLater(() -> label.setText("[Event] " + result2));
                }));
            });
        }
    }

}
