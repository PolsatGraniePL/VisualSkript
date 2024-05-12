package com.polsat.visualskript.gui;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.MenuManager;
import com.polsat.visualskript.gui.manager.TabManager;
import com.polsat.visualskript.gui.manager.block.BlockManager;
import com.polsat.visualskript.gui.manager.notification.DialogAlert;
import com.polsat.visualskript.system.DocsDownloader;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


//TODO
// - W każdym pliku .vsk sekcja "opened":"true/false" w formie JSON aby LoadLatestTab działał poprawnie
// - Przy dodawaniu Tab na TabPage każdy tab musi mieć VBox (System wizualnego języka)
// - zamiana button na pane

public class Controller {

    @FXML private TabPane buildTab;
    @FXML private VBox blockContainer;
    @FXML private TextField textField;

    @FXML private CheckBox checkBoxEvents;
    @FXML private CheckBox checkBoxConditions;
    @FXML private CheckBox checkBoxSections;
    @FXML private CheckBox checkBoxEffects;
    @FXML private CheckBox checkBoxExpressions;
    @FXML private CheckBox checkBoxTypes;
    @FXML private CheckBox checkBoxStructures;
    @FXML private CheckBox checkBoxFunctions;

    private final List<CheckBox> checkBoxes = new ArrayList<>();
    public List<Block> blocksList = new ArrayList<>();

    public void initialize() {
        System.out.println("initialize");

        checkBoxes.addAll(Arrays.asList(
            checkBoxEvents,
            checkBoxConditions,
            checkBoxSections,
            checkBoxEffects,
            checkBoxExpressions,
            checkBoxTypes,
            checkBoxStructures,
            checkBoxFunctions
        ));

        TabManager.loadLatestTab(buildTab);
        loadDefaultBlockList();
        BlockManager.setupTabPane(buildTab, blockContainer);
        BlockManager.putBlocksInContainer(blocksList);
        sortBlocksList();
    }

    private void loadDefaultBlockList() {

        boolean status = DocsDownloader.start();
        if (!status){
            DialogAlert.alertError("Failed to download the latest documentation.");
        }

        List<Block> blocks = BlockManager.getBlocksList();
        blocksList.addAll(blocks);
    }

    public void sortBlocksList() {
        blockContainer.getChildren().clear();
        List<Block> visibleBlocksList = BlockManager.sortBlocksList(blocksList, textField.getText().toLowerCase(), checkBoxes);
        BlockManager.putBlocksInContainer(visibleBlocksList);
    }



//  Menu Controller
    public void menuCreateScript(){
        MenuManager.menuCreateScript();
    }
    public void menuOpenScript(){
        MenuManager.menuOpenScript();
    }
    public void menuEditScriptName(){
        MenuManager.menuEditScriptName(buildTab);
    }
    public void menuDeleteScript(){
        MenuManager.menuDeleteScript();
    }
}