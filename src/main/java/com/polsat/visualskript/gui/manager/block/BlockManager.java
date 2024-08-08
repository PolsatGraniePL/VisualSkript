package com.polsat.visualskript.gui.manager.block;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.tabs.TabsManager;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class BlockManager {

    private static TabPane buildTab;
    private static VBox blockContainer;

    //Returns a list of blocks from the SkriptDocs.json file
    public static List<Block> getBlocksList(){
        List<Block> list = new ArrayList<>();
        try {

            JSONParser parser = new JSONParser();
            JSONArray jsonFile = (JSONArray) parser.parse(new FileReader(Objects.requireNonNull(BlockManager.class.getResource("/SkriptDocs.json")).getFile()));

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
                            assert currentType != null;
                            BlockType tmpBlockType = switch ((String) currentType) {
                                case "events" -> BlockType.EVENT;
                                case "conditions" -> BlockType.CONDITION;
                                case "Section" -> BlockType.SECTION;
                                case "effects" -> BlockType.EFFECT;
                                case "expressions" -> BlockType.EXPRESSION;
                                case "classes" -> BlockType.TYPE;
                                case "Structure" -> BlockType.STRUCTURE;
                                case "functions" -> BlockType.FUNCTION;
                                default -> BlockType.ERROR;
                            };

                            // PATTERN
                            StringBuilder tmpPatternString = new StringBuilder();
                            for (Object x : patterns) {
                                if (!Objects.equals(x, "pattern_end"))
                                    tmpPatternString.append((String)x).append("\n");
                            }

                            // Block builder
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

    //Returns a list of blocks, according to text or block type.
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

    public static void setupTabPane(TabPane buildTab, VBox blockContainer){
        BlockManager.buildTab = buildTab;
        BlockManager.blockContainer = blockContainer;
    }

    //Wstawia listę bloków do VBox
    public static void putBlocksInContainer(List<Block> listType){
        int index = -1;
        for (Block block : listType) {
            index++;

            //Create objects
            Pane tmpPane = new Pane();
            Label tmpLabel = new Label(block.getName());

            //Style objects
            tmpPane.getChildren().add(tmpLabel);
            tmpPane.setStyle("-fx-border-color:  #020202; -fx-border-radius: 5px ; -fx-background-color: #" + block.getType().getHexColor() + "; -fx-background-radius: 5px; -fx-border-width: 1px");
            tmpLabel.setStyle("-fx-font-color: #000000; -fx-font-weight: bold; -fx-font-size: 14px;");
            tmpLabel.setTooltip(new Tooltip(block.getDescription() + "\n\n" + block.getPattern()));

            VBox.setMargin(tmpPane, new Insets(10, 10, 10, 10));
            tmpLabel.setPadding(new Insets(5, 5, 5, 5));

            tmpPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
            tmpPane.setMinWidth(Region.USE_PREF_SIZE);
            tmpPane.setMaxWidth(Region.USE_PREF_SIZE);

            //Drag objects system
            int finalIndex = index;
            tmpPane.setOnDragDetected(event -> {
                Dragboard dragboard = tmpPane.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();

                content.putString(String.valueOf(finalIndex));
                dragboard.setContent(content);

                dragboard.setDragView(tmpPane.snapshot(null, null));

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
                if (db.hasString() && !Objects.isNull(buildTab.getSelectionModel().getSelectedItem())) {
                    //Drop system
                    Block block1 = listType.get(Integer.parseInt(db.getString()));
                    TabPane tabPane = (TabPane) buildTab.getSelectionModel().getSelectedItem().getContent();
                    switch (block1.getType()){
                        case EVENT:
                            TabsManager.addTab(block1.getName(), tabPane);
                            VBox vBox = (VBox)((ScrollPane) tabPane.getSelectionModel().getSelectedItem().getContent()).getContent();
                            ViewBlock.addEvent(vBox, block1.getPattern());
                            break;
                        case SECTION:
                            TabsManager.addTab(block1.getName(), tabPane);
                            break;
                    }
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });

            //Add object to block container
            blockContainer.getChildren().add(tmpPane);
        }
    }

}
