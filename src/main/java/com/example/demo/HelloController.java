package com.example.demo;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.events.MappingStartEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HelloController {

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
            VBox.setMargin(tmpBtn, new Insets(10, 10, 10, 10));
            container.getChildren().add(tmpBtn);
        }
    }
}