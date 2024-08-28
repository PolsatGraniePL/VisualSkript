package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import static com.polsat.visualskript.system.script.ScriptParser.build;

public class Section extends ViewBlock implements Placeable {

    public Section(Block block){
        super(block);

        vbox().label().hbox().emptyPane().showCombinations().contextMenu().dropGlowing().dropVBox();

        dropVBox.getChildren().add(emptyPane);
        vBox.getChildren().addAll(hBox, dropVBox);
        hBox.getChildren().add(label);
        this.getChildren().add(vBox);
    }

    @Override
    public void place(Node node) {
        BlockPlacer.placeOnVBox(this, node);
    }

    @Override
    public void buildMenu(){
        MenuItem edit = new MenuItem("Edit");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(edit, delete);
        edit.setOnAction(event -> {
            setCombinations();
        });
        delete.setOnAction(event -> {
            if (this.getParent() instanceof VBox vboxMain) {
                vboxMain.getChildren().remove(this);
                build();
            }
        });
    }
}