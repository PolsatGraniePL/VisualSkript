package com.polsat.visualskript.gui.manager.view.popovers;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SelectBoxPopOver {

    private final double WIDTH = 500;
    private final double HEIGHT = 200;

    public SelectBoxPopOver(List<String> list, Node obj, Consumer<String> callback) {
        PopOver pop = new PopOver();
        pop.setContentNode(getPane(list, callback, pop));

        pop.setAnimated(false);
        pop.setPrefWidth(WIDTH);
        pop.setPrefHeight(HEIGHT);
        pop.setTitle("Select Box");

        Platform.runLater(() -> {
            pop.show(obj);
            pop.requestFocus();
        });

    }

    private Pane getPane(List<String> list, Consumer<String> callback, PopOver pop){
        Pane pane = new Pane();
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        ListView<String> listView = new ListView<>();
        Label label = new Label();
        TextField textField = new TextField();

        pane.setPrefSize(WIDTH, HEIGHT);
        pane.setStyle("-fx-background-color: #0d1117");

        vbox.setPrefSize(WIDTH, HEIGHT);
        vbox.setFillWidth(true);

        hbox.setFillHeight(true);
        HBox.setHgrow(textField, Priority.ALWAYS);

        label.setFont(new Font("System", 20));
        label.setTextFill(Color.WHITE);
        label.setPadding(new Insets(0, 0, 0, 5));
        label.setText("Search:");


        textField.setFont(new Font("Arial", 16));
        textField.setOnKeyTyped((keyEvent)->{
            String text = textField.getText();
            List<String> tempList = new ArrayList<>();
            for (String str : list){
                if (str.toLowerCase().contains(text.toLowerCase())){
                    tempList.add(str);
                }
            }
            listView.getItems().setAll(tempList);
        });

        listView.getItems().setAll(list);
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            pop.hide();
            callback.accept(newValue);
        });

        pane.getChildren().add(vbox);
        vbox.getChildren().addAll(hbox, listView);
        hbox.getChildren().addAll(label, textField);
        return pane;
    }
}
