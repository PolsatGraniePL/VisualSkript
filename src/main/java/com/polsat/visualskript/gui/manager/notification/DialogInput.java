package com.polsat.visualskript.gui.manager.notification;

import javafx.scene.control.TextInputDialog;

public class DialogInput {
    public static String Input(String title, String content){
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle(title);
        textInputDialog.setHeaderText(null);
        textInputDialog.setGraphic(null);
        textInputDialog.getDialogPane().setContentText(content);

        textInputDialog.showAndWait();
        return textInputDialog.getResult();
    }
}
