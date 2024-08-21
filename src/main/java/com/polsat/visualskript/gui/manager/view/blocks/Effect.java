package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.drop.DropSystem;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.placeable;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class Effect extends ViewBlock implements placeable {

    public Effect(Block block){
        super(block, null);
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
        BlockPlacer.placeOnVBox(this, node);
    }

}