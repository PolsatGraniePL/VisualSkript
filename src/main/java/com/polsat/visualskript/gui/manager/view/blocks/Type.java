package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public class Type extends ViewBlock {

    public Type(Block block){
        super(block);
        this.setStyle(this.getStyle()+"-fx-background-radius: 25px; -fx-border-radius: 25px;");
        HBox.setMargin(this, new Insets(5, 5, 5, 5));
    }

}