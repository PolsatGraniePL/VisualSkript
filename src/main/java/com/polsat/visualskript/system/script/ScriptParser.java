package com.polsat.visualskript.system.script;

import com.polsat.visualskript.Main;
import com.polsat.visualskript.gui.manager.block.BlockManager;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.util.ErrorHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
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
                    System.out.println(recurency(block));
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String recurency(ViewBlock viewBlock){
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        viewBlock.getChildren().forEach((node -> {
            if (node instanceof HBox hBoxBlock){
                //NORMAL BLOCK
                builder.append(blockOnHbox(hBoxBlock));
            } else if (node instanceof VBox) {
                //SECTION
                builder.append(blockOnHbox((HBox) ((VBox)viewBlock.getChildren().get(0)).getChildren().get(0)));
                viewBlock.getDropVBox().getChildren().forEach((hBox)->{
                    if (hBox instanceof ViewBlock block) {
                        builder.append(recurency(block));
                    }
                });
            }
        }));
        return builder.toString();
    }

    private static String blockOnHbox(HBox hBox){
        StringBuilder stringBuilder = new StringBuilder();
        hBox.getChildren().forEach((node -> {
            if (node instanceof Label label){
                stringBuilder.append(label.getText());
            } else if (node instanceof DropViewExpr dropViewExpr){
                stringBuilder.append(" %").append(((Label) ((HBox) dropViewExpr.getChildren().get(0)).getChildren().get(0)).getText()).append("% ");
            } else {
                stringBuilder.append(node.getClass().getName());
            }
        }));
        return stringBuilder.toString();
    }

}
