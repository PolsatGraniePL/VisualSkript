package com.polsat.visualskript;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    //
    //TODO:
    // - Components: wyświetlanie wszystkich eventów/struktur
    // - Zapisywanie do .sk
    // - Wczytywanie z .sk
    // - Działający blok funkcji
    // - Placement nad/pod blokiem w zależności od YDrop
    //

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/Layout.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Visual Skript");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
