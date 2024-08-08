package com.polsat.visualskript.gui.manager.view;

import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.system.pattern.PatternExtractor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class ViewBlock {

    public static void addEvent(VBox vbox, String patterns){
        //build view box
        Pane pane = new Pane();
        HBox hbox = new HBox();
        Label label = new Label(PatternExtractor.getFirstPattern(patterns));

        pane.setStyle("-fx-background-color: #"+ BlockType.EVENT.getHexColor() +";");
        label.setFont(new Font("System", 24));
        label.setPadding(new Insets(5, 5, 5, 5));

        hbox.getChildren().add(label);
        pane.getChildren().add(hbox);
        //add pane to current VBox.
        vbox.getChildren().add(pane);

        //Wait 0.01 second and show SelectBoxPopOver with patterns to select.
        new Timeline(new KeyFrame(Duration.seconds(0.01),
            event -> {
                new SelectBoxPopOver().Show(Arrays.asList(patterns.split("\n")), pane, result ->{
                    Platform.runLater(() -> label.setText(result));
                    List<String> combinationsList = PatternExtractor.getCombinations(result);
                    Collections.reverse(combinationsList);
                    Platform.runLater(()-> new SelectBoxPopOver().Show(combinationsList, pane, result2 ->{
                        Platform.runLater(() -> label.setText(result2));
                    }));
                });
            })
        ).playFromStart();



    }

}
