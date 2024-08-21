package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Expression extends ViewBlock implements Placeable {

    public Expression(Block block, String oldText){
        super(block, oldText);
        this.setStyle(this.getStyle()+"-fx-background-radius: 25px; -fx-border-radius: 25px;");
        HBox.setMargin(this, new Insets(5, 5, 5, 5));
        VBox.setMargin(this, new Insets(5, 5, 5, 5));
    }

    @Override
    public void place(Node node) {
        BlockPlacer.placeOnExpr(this, node);
    }

}