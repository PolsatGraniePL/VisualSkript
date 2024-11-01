package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

import static com.polsat.visualskript.system.script.ScriptParser.build;

public class Type extends ViewBlock implements Placeable {

    public Type(Block block, String oldText){
        super(block);

        this.setStyle(this.getStyle() + "-fx-background-radius: 25px; -fx-border-radius: 25px;");
        hbox().label("["+block.getName().substring(8)+"]").textField().oldText(oldText).toolTip(oldText).contextMenu().margins();

        hBox.getChildren().addAll(label, textField);
        this.getChildren().add(hBox);
    }

    public Type(String text, Block block, String oldText) {
        super(block);

        this.setStyle(this.getStyle() + "-fx-background-radius: 25px; -fx-border-radius: 25px;");
        hbox().label("["+block.getName().substring(8)+"]").textField(text).oldText(oldText).toolTip(oldText).contextMenu().margins();

        hBox.getChildren().addAll(label, textField);
        this.getChildren().add(hBox);
        setuper();
    }

    @Override
    public void place(Node node) {
        BlockPlacer.placeOnExpr(this, node);
    }

    @Override
    public void buildMenu(){
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(delete);
        delete.setOnAction(event -> {
            if (this.getParent() instanceof VBox vbox) {
                vbox.getChildren().remove(this);
                return;
            }
            ((HBox)this.getParent()).getChildren().set(((HBox)this.getParent()).getChildren().indexOf(this), new DropViewExpr(oldText));
            build();
        });
    }

}