package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class Expression extends ViewBlock implements Placeable {

    public Expression(Block block, String oldText){
        super(block);

        this.setStyle(this.getStyle()+"-fx-background-radius: 25px; -fx-border-radius: 25px;");
        hbox().label().contextMenu().oldText(oldText).margins().showCombinations();

        hBox.getChildren().add(label);
        this.getChildren().add(hBox);
    }

    public Expression(List<Node> controlList, Block block, String oldText) {
        super(controlList, block);

        this.setStyle(this.getStyle()+"-fx-background-radius: 25px; -fx-border-radius: 25px;");
        hbox().label().contextMenu().oldText(oldText).margins();

        this.getChildren().add(hBox);
        setuper();
    }

    @Override
    public void place(Node node) {
        BlockPlacer.placeOnExpr(this, node);
    }

}