package com.polsat.visualskript.gui;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.DefaultBlocks;
import com.polsat.visualskript.gui.manager.FileManager;
import com.polsat.visualskript.gui.manager.MenuManager;
import com.polsat.visualskript.gui.manager.block.BlockManager;
import com.polsat.visualskript.gui.manager.notification.DialogAlert;
import com.polsat.visualskript.gui.manager.tabs.MainTabManager;
import com.polsat.visualskript.system.DocsDownloader;
import com.polsat.visualskript.system.script.ScriptParser;
import com.polsat.visualskript.util.ErrorHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Controller {

    static public TextArea sfileViewer;
    @FXML public SplitPane splitPane;
    @FXML public TextArea fileViewer;
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
    private final List<Block> blocksList = new ArrayList<>();

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

        setFileViewer(fileViewer);
        MainTabManager.loadLatestTab(buildTab);
        loadDefaultBlockList();
        BlockManager.setupTabPane(buildTab, blockContainer, splitPane);
        sortBlocksList();
    }

    private void loadDefaultBlockList() {

        boolean status = DocsDownloader.start();
        if (!status){
            DialogAlert.alertError("Failed to download the latest documentation.");
        }

        List<Block> blocks = BlockManager.getBlocksList();
        blocks.addAll(DefaultBlocks.getDefaultBlocks());
        blocksList.addAll(blocks);
    }

    public void sortBlocksList() {
        blockContainer.getChildren().clear();
        List<Block> visibleBlocksList = BlockManager.sortBlocksList(blocksList, textField.getText().toLowerCase(), checkBoxes);
        BlockManager.putBlocksInContainer(visibleBlocksList);
    }

    public void setFileViewer(TextArea fileViewer) {
        sfileViewer = fileViewer;
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

    // Script settings Controller
    public void buildScript(){
        ScriptParser.build();
    }
    public void openScriptLocation(){
        try {
            File file = FileManager.getFileByName(BlockManager.getBuildTab().getSelectionModel().getSelectedItem().getText()).getParentFile();
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            ErrorHandler.alert(e.toString());
        }
    }

}