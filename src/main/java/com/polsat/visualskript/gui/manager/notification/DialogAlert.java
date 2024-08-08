package com.polsat.visualskript.gui.manager.notification;

import javafx.scene.control.Alert;

public class DialogAlert {

    public static void alertError(String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    public static void alertError(String content, String header){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
