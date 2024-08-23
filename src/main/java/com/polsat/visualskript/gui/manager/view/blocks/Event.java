package com.polsat.visualskript.gui.manager.view.blocks;


import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

public class Event extends ViewBlock implements Placeable {

    public Event(Block block) {
        super(block);

        this.setStyle(this.getStyle()+"-fx-background-radius: 10px 10px 0 0; -fx-border-radius: 10px 10px 0 0; -fx-border-color: black;");
        hbox().label().contextMenu().dropGlowing().showCombinations();

        hBox.getChildren().add(label);
        this.getChildren().add(hBox);
    }

    @Override
    public void place(Node node) {
        BlockPlacer.placeOnBuildTab(this, node);
    }

    @Override
    public void buildMenu() {
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().add(delete);
        delete.setOnAction(event -> {
            //TODO: DELETE CURRENT COMPONENT
        });
    }

}
