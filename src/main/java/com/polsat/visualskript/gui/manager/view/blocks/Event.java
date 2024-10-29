package com.polsat.visualskript.gui.manager.view.blocks;


import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockManager;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.polsat.visualskript.system.script.ScriptParser.build;

public class Event extends ViewBlock implements Placeable {

    public Event(Block block) {
        super(block);

        this.setStyle(this.getStyle()+"-fx-background-radius: 10px 10px 0 0; -fx-border-radius: 10px 10px 0 0; -fx-border-color: black;");
        hbox().label().contextMenu().dropGlowing().showCombinations();

        hBox.getChildren().add(label);
        this.getChildren().add(hBox);
    }

    public Event(List<Node> controlList, Block block) {
        super(controlList, block);

        this.setStyle(this.getStyle()+"-fx-background-radius: 10px 10px 0 0; -fx-border-radius: 10px 10px 0 0; -fx-border-color: black;");
        hbox().contextMenu().dropGlowing();

        this.getChildren().add(hBox);
        setuper();
    }

    @Override
    public void place(Node node) {
        BlockPlacer.placeOnBuildTab(this, node);
    }

    @Override
    public void buildMenu() {
        MenuItem edit = new MenuItem("Edit");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(edit, delete);
        edit.setOnAction(event -> {
            setCombinations();
        });
        delete.setOnAction(event -> {
            TabPane tabPane = (TabPane) BlockManager.getBuildTab().getSelectionModel().getSelectedItem().getContent();
            tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedItem());
            build();
        });
    }

}
