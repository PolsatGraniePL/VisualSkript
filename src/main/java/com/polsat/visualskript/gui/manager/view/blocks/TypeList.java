package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import static com.polsat.visualskript.system.script.ScriptParser.build;

public class TypeList extends ViewBlock implements Placeable {

    public TypeList(Block block, String oldText) {
        super(block);

        this.setStyle(this.getStyle() + "-fx-background-radius: 25px; -fx-border-radius: 25px;");

        vbox().hbox().label("["+block.getName().substring(8)+"]").oldText(oldText).margins().contextMenu();

        hBox.getChildren().add(label);
        vBox.getChildren().addAll(hBox, newDropViewExpr(), newDropViewExpr());
        this.getChildren().add(vBox);
    }

    private DropViewExpr newDropViewExpr() {
        DropViewExpr dropViewExpr = new DropViewExpr("object");
        HBox.setMargin(dropViewExpr, new Insets(5, 5, 5, 5));
        VBox.setMargin(dropViewExpr, new Insets(5, 5, 5, 5));
        return dropViewExpr;
    }

    @Override
    public void place(Node node) {
        BlockPlacer.placeOnExpr(this, node);
    }

    @Override
    public void buildMenu(){
        MenuItem add = new MenuItem("Add object");
        MenuItem remove = new MenuItem("Remove object");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(add, remove, delete);
        add.setOnAction(event -> {
            vBox.getChildren().add(newDropViewExpr());
        });
        remove.setOnAction(event -> {
            if (vBox.getChildren().size()>2){
                vBox.getChildren().remove(vBox.getChildren().size()-1);
            }
        });
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
