package com.polsat.visualskript.gui.manager.notification;

import javafx.scene.control.ChoiceDialog;

import java.util.ArrayList;
import java.util.Optional;

public class DialogChoice {
    public static String Choice(String title, String content, ArrayList<String> list){
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>();
        choiceDialog.setTitle(title);
        choiceDialog.setHeaderText(null);
        choiceDialog.setGraphic(null);
        choiceDialog.getDialogPane().setContentText(content);
        choiceDialog.getItems().setAll(list);

        Optional<String> result = choiceDialog.showAndWait();
        return result.isPresent() && result.get().equals(choiceDialog.getSelectedItem()) ? result.get() : null;
    }
}
