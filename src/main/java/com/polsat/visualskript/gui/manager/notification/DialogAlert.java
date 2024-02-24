package com.polsat.visualskript.gui.manager.notification;

import javafx.scene.control.Alert;

public class DialogAlert {

        public static void alertError(String content){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(content);
            alert.show();
        }
}
