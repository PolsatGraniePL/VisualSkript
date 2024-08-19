package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public class Conditions extends ViewBlock {

    private boolean inVBox;

    public Conditions(Block block, boolean inVBox){
        super(block);
        this.inVBox = inVBox;
        if (!inVBox) {
            this.setStyle(this.getStyle()+"-fx-background-radius: 25px;");
            HBox.setMargin(this, new Insets(5, 5, 5, 5));
        }
    }

    public boolean getInVBox() {
        return inVBox;
    }

}
