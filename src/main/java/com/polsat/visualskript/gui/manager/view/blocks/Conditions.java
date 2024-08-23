package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.scene.Node;

public class Conditions extends ViewBlock implements Placeable {

    private final boolean inVBox;

    public Conditions(Block block, String oldText, boolean inVBox){
        super(block);
        this.inVBox = inVBox;
        if (!inVBox) {
            this.setStyle(this.getStyle() + "-fx-background-radius: 25px; -fx-border-radius: 25px;");
            oldText(oldText).margins();
        }
        hbox().label().contextMenu().dropGlowing().showCombinations();
        hBox.getChildren().add(label);
        this.getChildren().add(hBox);
    }

    public boolean getInVBox() {
        return inVBox;
    }

    @Override
    public void place(Node node) {
        if (this.getInVBox()){
            BlockPlacer.placeOnVBox(this, node);
            return;
        }
        BlockPlacer.placeOnExpr(this, node);
    }

}
