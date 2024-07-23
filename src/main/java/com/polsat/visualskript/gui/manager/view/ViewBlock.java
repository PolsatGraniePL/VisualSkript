package com.polsat.visualskript.gui.manager.view;

import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.system.pattern.PatternExtractor;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.controlsfx.control.PopOver;

import java.util.Arrays;
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


        //TODO:
        //Naprawa aby SelectBoxPopOver działało kilka razy, nie tylko pierwszy raz.
        System.out.println("layoutBoundsProperty");
        new SelectBoxPopOver().Show(Arrays.asList(patterns.split("\n")), pane, result ->{
            System.out.println("R1"+result);
            Platform.runLater(()->{
                System.out.println("runLater");
                new SelectBoxPopOver().Show(PatternExtractor.getCombinations(result), pane, result2 ->{
                    System.out.println("R2"+result2);
                    Platform.runLater(() -> {
                        System.out.println("runLater2");
                        label.setText(result2);
                    });
                });
            });
        });


    }

}
