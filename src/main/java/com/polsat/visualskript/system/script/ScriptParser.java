package com.polsat.visualskript.system.script;

import com.polsat.visualskript.Main;
import com.polsat.visualskript.gui.manager.block.BlockManager;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.blocks.Effect;
import com.polsat.visualskript.gui.manager.view.blocks.Expression;
import com.polsat.visualskript.util.ErrorHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ScriptParser {

    //Load .sk to visual skript
    public static void load(File file){
        //if option is not exist
        //  add options
    }

    //Build visual skript to .sk
    public static void build(){
        //NEW THREAD
        try {
            TabPane tabPane = (TabPane) BlockManager.getBuildTab().getSelectionModel().getSelectedItem().getContent();
            VBox vBox = (VBox)((ScrollPane) tabPane.getSelectionModel().getSelectedItem().getContent()).getContent();

            String skriptLoc = Main.class.getResource("/scripts/").getPath();
            String skriptName = BlockManager.getBuildTab().getSelectionModel().getSelectedItem().getText();

            File file = new File(skriptLoc + skriptName);

            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            System.out.println("-=-=-=-=-");

            vBox.getChildren().forEach((viewBlock)->{
                if (viewBlock instanceof ViewBlock block) {
                    System.out.println(recurency(block, 1));
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String recurency(ViewBlock viewBlock, int depth){
        StringBuilder builder = new StringBuilder();
        String tabs = "\t".repeat(depth);
        viewBlock.getChildren().forEach((node -> {

            switch (viewBlock.getBlock().getType()){
                case EVENT:
                    builder.append(blockOnHbox((HBox) node)).append(":");
                    break;
                case STRUCTURE:
                    builder.append(blockOnHbox((HBox) node)).append(":");
                    break;
                case CONDITION, EFFECT:
                    builder.append(tabs).append(blockOnHbox((HBox) node));
                    break;
//                case FUNCTION:
//                    TODO
//                    break;
                case COMMENT:
                    builder.append(tabs).append("#").append(viewBlock.getTextField().getText());
                    break;
                case SECTION:
                    builder.append(tabs).append(blockOnHbox((HBox) viewBlock.getvBox().getChildren().get(0))).append(":");
                    viewBlock.getDropVBox().getChildren().forEach((hBox)->{
                        if (hBox instanceof ViewBlock block) {
                            builder.append("\n").append(recurency(block, depth+1));
                        }
                    });
                    break;
            }
        }));
        return builder.toString();
    }

    private static String blockOnHbox(HBox hBox){
        StringBuilder stringBuilder = new StringBuilder();
        hBox.getChildren().forEach((node -> {
            if (node instanceof Label label){
                stringBuilder.append(getNameWithoutType(label.getText()));
            } else if (node instanceof DropViewExpr dropViewExpr){
                stringBuilder.append(space(stringBuilder)).append("%").append(blockOnHbox(dropViewExpr.gethBox())).append("%").append(space(stringBuilder));
            } else if (node instanceof ViewBlock viewBlock){
                viewBlockCompiler(viewBlock, stringBuilder);
            } else {
                stringBuilder.append("[").append(node.getClass().getName()).append("]");
            }
        }));
        return stringBuilder.toString();
    }

    private static void viewBlockCompiler(ViewBlock viewBlock, StringBuilder stringBuilder){
        switch (viewBlock.getBlock().getType()){
            case CONDITION, EXPRESSION -> stringBuilder.append(space(stringBuilder)).append(blockOnHbox(viewBlock.gethBox())).append(space(stringBuilder));
            case TYPE, STRUCTURE -> {
                switch (getNameWithoutType(viewBlock.getBlock().getName())){
                    case "Text":
                        stringBuilder.append(space(stringBuilder)).append("\"").append(viewBlock.getTextField().getText()).append("\"").append(space(stringBuilder));
                        break;
                    case "Variables":
                        stringBuilder.append(space(stringBuilder)).append("{").append(viewBlock.getTextField().getText()).append("}").append(space(stringBuilder));
                        break;
                    case "Options":
                        stringBuilder.append(space(stringBuilder)).append("{@").append(viewBlock.getTextField().getText()).append("}").append(space(stringBuilder));
                        break;
                    case "World":
                        stringBuilder.append(space(stringBuilder)).append("world \"").append(viewBlock.getTextField().getText()).append("\"").append(space(stringBuilder));
                        break;
                    default:
                        stringBuilder.append(space(stringBuilder)).append(viewBlock.getTextField().getText()).append(space(stringBuilder));
                }

            }
            case TYPE_LIST -> {
                viewBlock.getvBox().getChildren().forEach(block -> {
                    System.out.println();

                    if (viewBlock.getvBox().getChildren().indexOf(block) > 1) {
                        stringBuilder.append(",");
                    }

                    if (block instanceof DropViewExpr dropViewExpr){
                        stringBuilder.append(space(stringBuilder)).append("%").append(blockOnHbox(dropViewExpr.gethBox())).append("%").append(space(stringBuilder));
                    } else if (block instanceof ViewBlock viewBlock1) {
                        viewBlockCompiler(viewBlock1, stringBuilder);
                    }
                });
            }
        }
    }

    private static String getNameWithoutType(String text){
        return text.substring(Objects.equals(text.indexOf("] "), -1) ? 0 : text.indexOf("] ") + 2);
    }

    private static String space(StringBuilder builder){
        if (!Objects.equals(builder.lastIndexOf(" "), builder.length()-1)){
             return " ";
        }
        return "";
    }

}
