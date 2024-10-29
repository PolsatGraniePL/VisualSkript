package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.scene.Node;

import java.util.List;

public class Effect extends ViewBlock implements Placeable {

    public Effect(Block block){
        super(block);

        hbox().label().contextMenu().dropGlowing().showCombinations();

        hBox.getChildren().add(label);
        this.getChildren().add(hBox);

    }

    public Effect(List<Node> controlList, Block block) {
        super(controlList, block);

        hbox().label().contextMenu().dropGlowing();

        this.getChildren().add(hBox);
        setuper();
    }

    @Override
    public void place(Node node) {
        BlockPlacer.placeOnVBox(this, node);
    }

}