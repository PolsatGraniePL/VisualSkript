package com.polsat.visualskript.gui.manager.block;

import com.polsat.visualskript.gui.manager.view.ViewBlock;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class BlockPlacer {

    public static void placeBlock(ViewBlock viewBlock, Node node, Boolean isVbox) {
        switch (viewBlock.getBlock().getType()){
            //Drop on buildTab
            case EVENT, STRUCTURE:
                VBox vBox = (VBox) node;
                vBox.getChildren().add(0, viewBlock);
                break;
            //Drop on %xyz%
            case EXPRESSION, TYPE:
                Pane pane = (Pane) node.getParent();
                int index = pane.getChildren().indexOf(node);
                pane.getChildren().set(index, viewBlock);
                break;
            //Drop on VBox
            case SECTION, EFFECT:
                VBox vBox1 = (VBox) node;
                vBox1.getChildren().add(vBox1.getChildren().size()-1, viewBlock);
                break;
            //Drop on VBox and %xyz%
            case FUNCTION, CONDITION:
                if (isVbox){
                    VBox vBox2 = (VBox) node;
                    vBox2.getChildren().add(vBox2.getChildren().size()-1, viewBlock);
                } else {
                    Pane pane1 = (Pane) node.getParent();
                    int index1 = pane1.getChildren().indexOf(node);
                    pane1.getChildren().set(index1, viewBlock);
                }
                break;
        }
    }
}