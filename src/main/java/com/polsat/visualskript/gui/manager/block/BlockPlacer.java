package com.polsat.visualskript.gui.manager.block;

import com.polsat.visualskript.gui.manager.drop.DropSystem;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class BlockPlacer {

    public static void placeOnBuildTab(ViewBlock block, Node node) {
        VBox vBox = (VBox) node;
        vBox.getChildren().add(0, block);
    }

    public static void placeOnExpr(ViewBlock block, Node node) {
        Pane pane = (Pane) node.getParent();
        int index = pane.getChildren().indexOf(node);
        pane.getChildren().set(index, block);
    }

    public static void placeOnVBox(ViewBlock block, Node node) {
        VBox vBox1 = (VBox) node;
        if (!Objects.isNull(DropSystem.getCurrentDropUnderNode())){
            int index1 = vBox1.getChildren().indexOf(DropSystem.getCurrentDropUnderNode());
            vBox1.getChildren().add(index1+1, block);
            return;
        }
        vBox1.getChildren().add(vBox1.getChildren().size()-1, block);
    }
}