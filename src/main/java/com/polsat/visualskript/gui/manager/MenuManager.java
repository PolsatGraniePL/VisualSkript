package com.polsat.visualskript.gui.manager;

import com.polsat.visualskript.gui.manager.notification.DialogAlert;
import com.polsat.visualskript.gui.manager.notification.DialogChoice;
import com.polsat.visualskript.gui.manager.notification.DialogInput;
import javafx.scene.control.TabPane;

import java.util.Objects;

public class MenuManager {


    public static void menuCreateScript(){
        String result = DialogInput.Input("Create new script", "Script name: ");
        if (!Objects.isNull(result)){
            if (!Objects.equals(result, "")){
                if(!ScriptsManager.getScriptsList().contains(result)){
                    boolean success = ScriptsManager.createScript(result);
                    if (!success){
                        DialogAlert.alertError("Script is already exists.");
                    }
                } else {
                    DialogAlert.alertError("Script is already exists.");
                }
            } else {
                DialogAlert.alertError("Script name cannot be empty.");
            }
        }
    }

    public static void menuOpenScript(){
        String result = DialogChoice.Choice("Open script", "Script name:", ScriptsManager.getScriptsListWithOpenedStatus(false));
        if(!Objects.isNull(result)){
            if (!result.isEmpty()){
                if(!ScriptsManager.getScriptsListWithOpenedStatus(true).contains(result)){
                    ScriptsManager.openScript(result);
                } else {
                    DialogAlert.alertError("Script is already opened.");
                }
            } else {
                DialogAlert.alertError("Any script must be selected.");
            }
        }
    }

    public static void menuEditScriptName(TabPane buildTab){
        String result = DialogInput.Input("Edit file name", "File name:");
        String selected = null;
        try {selected = buildTab.getSelectionModel().getSelectedItem().getText();} catch (Exception ignore) {}
        if (!Objects.isNull(result)){
            if(!result.isEmpty()){
                if(!Objects.isNull(selected)){
                    if(!ScriptsManager.getScriptsList().contains(result+".vsk")){
                        ScriptsManager.editScriptName(selected, result);
                    } else {
                        DialogAlert.alertError("Script with this name is already exists.");
                    }
                } else {
                    DialogAlert.alertError("You do not have any scripts open.");
                }
            } else {
                DialogAlert.alertError("Script name cannot be empty.");
            }
        }
    }

    public static void menuDeleteScript(){
        String result = DialogChoice.Choice("Open file", "File name:", ScriptsManager.getScriptsList());
        if(!Objects.isNull(result)){
            if (!result.isEmpty()){
                ScriptsManager.deleteScript(result);
            } else {
                DialogAlert.alertError("Any script must be selected.");
            }
        }
    }
}
