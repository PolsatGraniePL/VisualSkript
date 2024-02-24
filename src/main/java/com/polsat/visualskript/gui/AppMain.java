package com.polsat.visualskript.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/polsat/visualskript/AppView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Visual Skript");
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(650);
        stage.setMaximized(true);
        stage.show();
    }

    public static void loadApp() {
        launch();
    }
}
