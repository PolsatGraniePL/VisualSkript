package com.polsat.visualskript.system.script;

import com.polsat.visualskript.Main;
import com.polsat.visualskript.gui.Controller;
import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockManager;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.blocks.Effect;
import com.polsat.visualskript.gui.manager.view.blocks.Expression;
import com.polsat.visualskript.gui.manager.view.blocks.Structure;
import com.polsat.visualskript.util.ErrorHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.MeshView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ScriptParser {

    //Load .sk to visual skript
    public static void load(File file){
        ScriptManager.setOpened(file, true);
        //TODO: generate TabPane & viewblocks
    }

    //Build visual skript to .sk
    public static void build(){
        try {
            BuildThread build = new BuildThread();
            build.setName("Build");
            build.start();
        } catch (Exception e) {
            ErrorHandler.alert(e.toString());
        }
    }
}
