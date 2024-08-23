package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Structure extends ViewBlock implements Placeable {

    private final boolean inExpression;

    public Structure(Block block, String oldText, boolean inExpression){
        super(block);
        this.inExpression = inExpression;

        oldText(oldText).hbox().contextMenu();

        this.getChildren().add(hBox);
        if (inExpression){
            this.setStyle(this.getStyle()+"-fx-background-radius: 25px; -fx-border-radius: 25px;");
            label("["+block.getName().substring(12)+"]").margins().textField();
            hBox.getChildren().addAll(label, textField);
            return;
        }
        this.setStyle(this.getStyle()+"-fx-background-radius: 10px 10px 0 0; -fx-border-radius: 10px 10px 0 0;");
        dropGlowing().label();
        hBox.getChildren().add(label);
    }

    public boolean isInExpression() {
        return inExpression;
    }

    @Override
    public void place(Node node) {
        if (this.isInExpression()){
            BlockPlacer.placeOnExpr(this, node);
            return;
        }
        BlockPlacer.placeOnBuildTab(this, node);
    }

    @Override
    public void buildMenu() {
        //TODO: ADD ITEMS ETC. (list block)or new block type.
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().add(delete);
        delete.setOnAction(event -> {
            if (this.getParent() instanceof VBox vbox) {
                //TODO: DELETE CURRENT COMPONENT
            } else {
                ((HBox)this.getParent()).getChildren().set(((HBox)this.getParent()).getChildren().indexOf(this), new DropViewExpr(oldText));
            }
        });
    }

}