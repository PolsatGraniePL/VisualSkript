package com.example.demo;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.events.NodeEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

//TODO
// - YML na JSON zamienić [SkriptParserYaml -> SkriptParserJson] 58:53 (https://github.com/SkriptLang/skript-docs/blob/main/docs/docs.json)
// - Zamiana 'src/main/resources/' na getClass().getResource(path) etc.
// - W każdym pliku .vsk sekcja "opened":"true/false" w formie JSON aby LoadLatestTab działał poprawnie
// - Ogarnąć anty debilizm dla każdego menuItemController[]
// - Przy dodawaniu Tab na TabPage każdy tab musi mieć VBox

public class HelloController {

    @FXML private TabPane buildTab;
    @FXML private VBox container;
    @FXML private TextField textField;

    @FXML private CheckBox checkBoxEvents;
    @FXML private CheckBox checkBoxConditions;
    @FXML private CheckBox checkBoxSections;
    @FXML private CheckBox checkBoxEffects;
    @FXML private CheckBox checkBoxExpressions;
    @FXML private CheckBox checkBoxTypes;
    @FXML private CheckBox checkBoxStructures;
    private final List<CheckBox> checkBoxes = new ArrayList<>();

    public List<Block> blocksList = new ArrayList<>();

    public void initialize() {
        System.out.println("initialize");

        TabController.loadLatestTab(buildTab);
        loadDefaultBlockList();
        putBlocksInContainer(blocksList);
    }

    private void loadDefaultBlockList() {

        checkBoxes.add(checkBoxEvents);
        checkBoxes.add(checkBoxConditions);
        checkBoxes.add(checkBoxSections);
        checkBoxes.add(checkBoxEffects);
        checkBoxes.add(checkBoxExpressions);
        checkBoxes.add(checkBoxTypes);
        checkBoxes.add(checkBoxStructures);

        try{
            InputStream yamlInput = new FileInputStream("src/main/resources/SkriptParserYaml.yml");
            Yaml yaml = new Yaml();
            Map<String , Object> yamlMaps = yaml.load(yamlInput);

            for (Map.Entry<String, Object> data : yamlMaps.entrySet()) {
                final Map<String, Object> module_name = (Map<String, Object>) yamlMaps.get(data.getKey());
                BlockType tmpBlockType = switch (module_name.get("type").toString()) {
                    case "Event" -> BlockType.EVENT;
                    case "Condition" -> BlockType.CONDITION;
                    case "Section" -> BlockType.SECTION;
                    case "Effect" -> BlockType.EFFECT;
                    case "Expression" -> BlockType.EXPRESSION;
                    case "Type" -> BlockType.TYPE;
                    case "Structure" -> BlockType.STRUCTURE;
                    default -> BlockType.ERROR;
                };

                StringBuilder tmpPatternString = new StringBuilder();
                for (String x: (ArrayList<String>) module_name.get("pattern")) {
                    tmpPatternString.append(x).append("\n");
                }

                Block tmpBlock = new Block(tmpBlockType, "["+tmpBlockType.getName()+"] " + module_name.get("name").toString(), tmpPatternString.toString(), module_name.get("description").toString());
                blocksList.add(tmpBlock);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void sortblocksList(){
        container.getChildren().clear();
        List<Block> visibleBlocksList = new ArrayList<>();
        for (Block block : blocksList) {
            if (block.getName().toLowerCase().contains(textField.getText().toLowerCase())) {
                for (CheckBox checkBoxTemp : checkBoxes) {
                    if (checkBoxTemp.isSelected() && Objects.equals(block.getType().getName(), checkBoxTemp.getText())) {
                        visibleBlocksList.add(block);
                    }
                }
            }
        }
        putBlocksInContainer(visibleBlocksList);
    }

    private void putBlocksInContainer(List<Block> listType){
        for (int i = 0; i < listType.size(); i++) {
            Button tmpBtn = new Button(listType.get(i).getName());
            tmpBtn.setStyle("-fx-border-color: #000000; -fx-border-radius: 2px ; -fx-background-color: #"+ listType.get(i).getType().getHexColor() +"; -fx-font-color: #000000; -fx-font-weight: bold; -fx-font-size: 15px; ");
            tmpBtn.setTooltip(new Tooltip(listType.get(i).getDescription()+"\n\n"+listType.get(i).getPattern()));

            tmpBtn.setOnDragDetected(event -> {
                Dragboard dragboard = tmpBtn.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString(tmpBtn.getText());
                dragboard.setContent(content);

                dragboard.setDragView(tmpBtn.snapshot(null, null));

                event.consume();
            });

            buildTab.setOnDragOver(event -> {
                if (event.getGestureSource() != buildTab && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.ANY);
                }
                event.consume();
            });

            buildTab.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    System.out.println(db.getString());
                    System.out.println(buildTab.getSelectionModel().getSelectedItem().getText());
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });

            VBox.setMargin(tmpBtn, new Insets(10, 10, 10, 10));
            container.getChildren().add(tmpBtn);
        }
    }

//  Menu Controller

    public void menuItemControllerCreateFile(){
        // CZY NIE ISTNIEJE
        // CZY NAZWA NIE PUSTA
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Create new file");
        textInputDialog.setHeaderText(null);
        textInputDialog.setGraphic(null);
        textInputDialog.getDialogPane().setContentText("File name: ");

        textInputDialog.showAndWait();
        String result = textInputDialog.getResult();
        File newFile = new File("src/main/resources/scripts/"+result+".vsk");
        try {
            if (newFile.createNewFile()){
                TabController.addTab(result+".vsk");
                buildTab.getSelectionModel().selectLast();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("File is already exists.");
                alert.show();
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public void menuItemControllerOpenFile(){
        // CZY COKOLWIEK WYBRANE
        // CZY ISTNIEJE W TABACH
        ChoiceDialog choiceDialog = new ChoiceDialog<>();
        choiceDialog.setTitle("Open file");
        choiceDialog.setHeaderText(null);
        choiceDialog.setGraphic(null);
        choiceDialog.getDialogPane().setContentText("File name:");

        File folder = new File("src/main/resources/scripts");
        File[] filesList = folder.listFiles();
        for (File file : filesList) {
            if (file.getName().endsWith(".vsk")) {
                choiceDialog.getItems().add(file.getName());
            }
        }

        choiceDialog.showAndWait();
        String result = choiceDialog.getSelectedItem().toString();
        TabController.addTab(result);
    }
    public void menuItemControllerEditFileName(){
        // CZY NAZWA NIE PUSTA
        // CZY AKTUALNIE JEST WYBRANY JAKIŚ TAB
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Edit file name");
        textInputDialog.setHeaderText(null);
        textInputDialog.setGraphic(null);
        textInputDialog.getDialogPane().setContentText("File name: ");

        textInputDialog.showAndWait();
        String result = textInputDialog.getResult();
        File file = new File("src/main/resources/scripts/"+buildTab.getSelectionModel().getSelectedItem().getText());
        file.renameTo(new File("src/main/resources/scripts/"+result+".vsk"));
        TabController.removeTab(buildTab.getSelectionModel().getSelectedIndex());
        TabController.addTab(result+".vsk");
        buildTab.getSelectionModel().selectLast();
    }
    public void menuItemControllerDeleteFile(){
        // CZY COKOLWIEK WYBRANE
        ChoiceDialog choiceDialog = new ChoiceDialog<>();
        choiceDialog.setTitle("Open file");
        choiceDialog.setHeaderText(null);
        choiceDialog.setGraphic(null);
        choiceDialog.getDialogPane().setContentText("File name:");

        File folder = new File("src/main/resources/scripts");
        File[] filesList = folder.listFiles();
        for (File file : filesList) {
            if (file.getName().endsWith(".vsk")) {
                choiceDialog.getItems().add(file.getName());
            }
        }

        choiceDialog.showAndWait();
        String result = choiceDialog.getSelectedItem().toString();
        for (Tab tab : buildTab.getTabs()){
            if (tab.getText().equals(result)){
                buildTab.getTabs().remove(tab);
                break;
            }
        }
        File file = new File("src/main/resources/scripts/"+result);
        file.delete();
    }
}