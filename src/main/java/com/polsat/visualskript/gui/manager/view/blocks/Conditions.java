package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.scene.Node;

import java.util.List;
import java.util.Objects;

public class Conditions extends ViewBlock implements Placeable {


    public Conditions(Block block, String oldText){
        super(block);
        hbox().label().contextMenu().showCombinations().oldText(oldText);
        hBox.getChildren().add(label);
        this.getChildren().add(hBox);
        if (!getInVBox()) {
            this.setStyle(this.getStyle() + "-fx-background-radius: 25px; -fx-border-radius: 25px;");
            margins();
        } else { dropGlowing(); }
    }

    public Conditions(List<Node> controlList, Block block, String oldText) {
        super(controlList, block);
        hbox().label().contextMenu().oldText(oldText);
        this.getChildren().add(hBox);
        if (!getInVBox()) {
            this.setStyle(this.getStyle() + "-fx-background-radius: 25px; -fx-border-radius: 25px;");
            margins();
        } else { dropGlowing(); }
        setuper();
    }

    public boolean getInVBox() {
        return Objects.isNull(oldText);
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
