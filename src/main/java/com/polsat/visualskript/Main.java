package com.polsat.visualskript;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    //
    //TODO:
    // - Miejsce podczas Drop
    // - Edycja miejsca drag and drop
    // - <.+> & %xyz% -> ()
    // - Components: wyświetlanie wszystkich eventów/struktur
    // - Zapisywanie do .json
    // - Wczytywanie z .json
    // - Eksportowanie do .sk
    // - NEXT: Naprawa VBOX aby był skalowalny, a nie zawierał 100% scrollpane.
    // - TextBoxPopOver na każdy TYPES
    // - NEXT: Wyśrodkowanie tekstu wertykalnie.
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
