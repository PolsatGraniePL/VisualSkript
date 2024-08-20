package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.drop.DropSystem;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import javafx.scene.effect.Glow;
import javafx.scene.layout.VBox;

public class Effect extends ViewBlock {

    public Effect(Block block){
        super(block);
        this.setOnDragEntered(event -> {
            DropSystem.setCurrentdropUnderNode(this);
            setEffect(new Glow(0.3));
        });
        this.setOnDragExited(event -> {
            DropSystem.setCurrentdropUnderNode(null);
            setEffect(null);
        });
    }

}