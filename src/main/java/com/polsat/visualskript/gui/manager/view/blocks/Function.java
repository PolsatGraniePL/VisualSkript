package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.drop.DropSystem;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Function extends ViewBlock implements Placeable {

    private final boolean inVBox;

    public Function(Block block, String oldText, boolean inVBox){
        super(block, oldText);
        this.inVBox = inVBox;
        if (!inVBox) {
            this.setStyle(this.getStyle()+"-fx-background-radius: 25px; -fx-border-radius: 25px;");
            HBox.setMargin(this, new Insets(5, 5, 5, 5));
            VBox.setMargin(this, new Insets(5, 5, 5, 5));
        } else {
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

    public boolean getInVBox() {
        return inVBox;
    }

    @Override
    public void place(Node node) {
        if (this.getInVBox()){
            BlockPlacer.placeOnVBox(this, node);
        }
        BlockPlacer.placeOnExpr(this, node);
    }

}