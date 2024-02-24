package com.polsat.visualskript.gui.manager.notification;

import javafx.scene.control.ChoiceDialog;

import java.util.ArrayList;

public class DialogChoice {

    public static String Choice(String title, String content, ArrayList<String> list){
        ChoiceDialog<Object> choiceDialog = new ChoiceDialog<>();
        choiceDialog.setTitle(title);
        choiceDialog.setHeaderText(null);
        choiceDialog.setGraphic(null);
        choiceDialog.getDialogPane().setContentText(content);
        choiceDialog.getItems().setAll(list);
        choiceDialog.showAndWait();
        return (String) choiceDialog.getSelectedItem();
    }

}
