package com.polsat.visualskript.gui.manager.view.blocks;


import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.drop.DropSystem;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Glow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Event extends ViewBlock implements Placeable {

    public Event(Block block) {
        super(block, null);
        this.setStyle(this.getStyle()+"-fx-background-radius: 10px 10px 0 0; -fx-border-radius: 10px 10px 0 0; -fx-border-color: black;");
        this.setOnDragEntered(event -> {
            DropSystem.setCurrentDropUnderNode(this);
            setEffect(new Glow(0.3));
        });
        this.setOnDragExited(event -> {
            DropSystem.setCurrentDropUnderNode(null);
            setEffect(null);
        });
    }

    @Override
    public void place(Node node) {
        BlockPlacer.placeOnBuildTab(this, node);
    }

    @Override
    public void buildMenu() {
        //TODO: ADD ITEMS ETC. (list block)or new block type.
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().add(delete);
        delete.setOnAction(event -> {
            //TODO: DELETE CURRENT COMPONENT
        });
    }

}
