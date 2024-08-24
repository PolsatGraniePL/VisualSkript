package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.drop.DropSystem;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class Comment extends ViewBlock implements Placeable {

    public Comment(Block block) {
        super(block);

        hbox().label("["+block.getName().substring(10)+"]").dropGlowing().textField().contextMenu();
        textField.setOnAction((event -> {
            DropSystem.setCurrentDropUnderNode(this);
            new Comment(block).place(this.getParent());
            DropSystem.setCurrentDropUnderNode(null);
        }));

        hBox.getChildren().addAll(label, textField);
        this.getChildren().add(hBox);
    }

    @Override
    public void place(Node node) {
        BlockPlacer.placeOnVBox(this, node);
    }

    @Override
    public void buildMenu(){
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().add(delete);
        delete.setOnAction(event -> ((VBox)this.getParent()).getChildren().remove(this));
    }
}
