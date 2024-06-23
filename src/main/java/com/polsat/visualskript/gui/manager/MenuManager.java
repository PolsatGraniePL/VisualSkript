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
            if (Objects.equals(result, "")){
                DialogAlert.alertError("Script name cannot be empty.");
                return;
            }
            if(ScriptsManager.getScriptsList().contains(result)){
                DialogAlert.alertError("Script is already exists.");
                return;
            }
            boolean success = ScriptsManager.createScript(result);
            if (!success){
                DialogAlert.alertError("Script is already exists.");
            }
        }
    }

    public static void menuOpenScript(){
        String result = DialogChoice.Choice("Open script", "Script name:", ScriptsManager.getScriptsListWithOpenedStatus(false));
        if(!Objects.isNull(result)){
            if (result.isEmpty()){
                DialogAlert.alertError("Any script must be selected.");
                return;
            }
            if(ScriptsManager.getScriptsListWithOpenedStatus(true).contains(result)){
                DialogAlert.alertError("Script is already opened.");
                return;
            }
            ScriptsManager.openScript(result);
        }
    }

    public static void menuEditScriptName(TabPane buildTab){
        String result = DialogInput.Input("Edit file name", "File name:");
        String selected = null;
        try {selected = buildTab.getSelectionModel().getSelectedItem().getText();} catch (Exception ignore) {}
        if (!Objects.isNull(result)){
            if(result.isEmpty()){
                DialogAlert.alertError("Script name cannot be empty.");
                return;
            }
            if(Objects.isNull(selected)){
                DialogAlert.alertError("You do not have any scripts open.");
                return;
            }
            if(ScriptsManager.getScriptsList().contains(result+".vsk")){
                DialogAlert.alertError("Script with this name is already exists.");
                return;
            }
            ScriptsManager.editScriptName(selected, result);
        }
    }

    public static void menuDeleteScript(){
        String result = DialogChoice.Choice("Open file", "File name:", ScriptsManager.getScriptsList());
        if(!Objects.isNull(result)){
            if (result.isEmpty()){
                DialogAlert.alertError("Any script must be selected.");
                return;
            }
            ScriptsManager.deleteScript(result);
        }
    }
}
