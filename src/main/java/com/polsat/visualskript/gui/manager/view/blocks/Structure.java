package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import javafx.scene.layout.VBox;

public class Structure extends ViewBlock {

    public Structure(Block block){
        super(block);
        this.setStyle(this.getStyle()+"-fx-background-radius: 10px 10px 0 0;");
    }

}