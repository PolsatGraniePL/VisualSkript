package com.polsat.visualskript.gui.manager.block;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import javafx.scene.control.CheckBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.*;

public class BlockManager {

    public static List<Block> getBlocksList(){
        List<Block> list = new ArrayList<>();
        try {

            JSONParser parser = new JSONParser();
            JSONArray jsonFile = (JSONArray) parser.parse(new FileReader(BlockManager.class.getResource("/SkriptDocs.json").getFile()));

            for (Object jsonTypes : jsonFile){
                for (Object currentType : ((JSONObject) jsonTypes).keySet()) {
                    if (Objects.equals(currentType, "skriptVersion"))
                        continue;
                    JSONObject block = (JSONObject) jsonTypes;
                    JSONArray blocks = (JSONArray) block.get(currentType);
                    for (Object currentBlockLoop : blocks){
                        if (Objects.isNull(((JSONObject) currentBlockLoop).get("end"))){
                            JSONObject currentBlock = (JSONObject) currentBlockLoop;

                            String id = (String) currentBlock.get("id");
                            String name = (String) currentBlock.get("name");
                            JSONArray patterns = (JSONArray) currentBlock.get("patterns");
                            String since = (String) currentBlock.get("since");
                            Object description;
                            String examples = (String) currentBlock.get("examples");

                            // DESCRIPTION
                            try {
                                description = currentBlock.get("description");
                                StringBuilder tmpDescString = new StringBuilder();
                                for (Object x : (JSONArray) description) {
                                    tmpDescString.append((String)x).append("\n");
                                }
                                description = tmpDescString.toString();
                            } catch (Exception ignore){
                                description = currentBlock.get("description");
                            }

                            // BLOCK TYPE
                            BlockType tmpBlockType = switch ((String) currentType) {
                                case "events" -> BlockType.EVENT;
                                case "conditions" -> BlockType.CONDITION;
                                //TODO: case "Section" -> BlockType.SECTION;
                                case "effects" -> BlockType.EFFECT;
                                case "expressions" -> BlockType.EXPRESSION;
                                case "classes" -> BlockType.TYPE;
                                //TODO: case "Structure" -> BlockType.STRUCTURE;
                                case "functions" -> BlockType.FUNCTION;
                                default -> BlockType.ERROR;
                            };

                            // PATTERN
                            StringBuilder tmpPatternString = new StringBuilder();
                            for (Object x : patterns) {
                                if (!Objects.equals(x, "pattern_end"))
                                    tmpPatternString.append((String)x).append("\n");
                            }

                            Block tempBlock = new Block(id, tmpBlockType, "[" + tmpBlockType.getName() + "] " + name, tmpPatternString.toString(), since, description.toString(), examples);
                            list.add(tempBlock);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static List<Block> sortBlocksList(List<Block> blocksList, String text, List<CheckBox> checkBoxes) {
        List<Block> visibleBlocksList = new ArrayList<>();
        blocksList.sort(Comparator.comparing(Block::getType));
        for (Block block : blocksList) {
            if (block.getName().toLowerCase().contains(text)) {
                for (CheckBox checkBoxTemp : checkBoxes) {
                    if (checkBoxTemp.isSelected() && Objects.equals(block.getType().getName(), checkBoxTemp.getText())) {
                        visibleBlocksList.add(block);
                    }
                }
            }
        }
        return visibleBlocksList;
    }


}