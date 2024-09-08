package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import com.polsat.visualskript.system.pattern.PatternExtractor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import java.util.Objects;

public class Function extends ViewBlock implements Placeable {

    public Function(Block block, String oldText){
        super(block);
        hbox().contextMenu().oldText(oldText);
        this.getChildren().add(hBox);
        if (!getInVBox()) {
            this.setStyle(this.getStyle() + "-fx-background-radius: 25px; -fx-border-radius: 25px;");
            margins();
        } else { dropGlowing(); }
        setupArguments();
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

    private void setupArguments(){
        String pattern = PatternExtractor.getFirstPattern(block.getPattern());
        String[] arguments = pattern.substring(pattern.indexOf("(") + 1, pattern.lastIndexOf(")")).split(", ");

        label("["+pattern.substring(0, pattern.indexOf("("))+"]");
        hBox.getChildren().add(label);

        int index = 0;
        for (String argument : arguments) {
            index++;
            String[] var = argument.split(" ");
            label(var[0]);
            hBox.getChildren().addAll(label, new DropViewExpr(var[1]));
            if (!(index == arguments.length)){
                label(", ");
                hBox.getChildren().add(label);
            }
        }
    }

}