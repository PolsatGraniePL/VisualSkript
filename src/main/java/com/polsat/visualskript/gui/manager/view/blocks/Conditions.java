package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.drop.DropSystem;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import javafx.geometry.Insets;
import javafx.scene.effect.Glow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Conditions extends ViewBlock {

    private final boolean inVBox;

    public Conditions(Block block, boolean inVBox){
        super(block);
        this.inVBox = inVBox;
        if (!inVBox) {
            this.setStyle(this.getStyle()+"-fx-background-radius: 25px; -fx-border-radius: 25px;");
            HBox.setMargin(this, new Insets(5, 5, 5, 5));
            VBox.setMargin(this, new Insets(5, 5, 5, 5));
        } else {
            this.setOnDragEntered(event -> {
                DropSystem.setCurrentdropUnderNode(this);
                setEffect(new Glow(0.3));
            });
            this.setOnDragExited(event -> {
                DropSystem.setCurrentdropUnderNode(null);
                setEffect(null);
            });
        }
    }

    public boolean getInVBox() {
        return inVBox;
    }

}
