package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.drop.DropSystem;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import javafx.scene.effect.Glow;

public class Effect extends ViewBlock {

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

}