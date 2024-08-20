package com.polsat.visualskript.gui.manager.block;

import com.polsat.visualskript.gui.manager.drop.DropSystem;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.blocks.Conditions;
import com.polsat.visualskript.gui.manager.view.blocks.Function;
import com.polsat.visualskript.gui.manager.view.blocks.Structure;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class BlockPlacer {

    public static void placeBlock(ViewBlock viewBlock, Node node) {
        switch (viewBlock.getBlock().getType()){
            //Drop on buildTab
            case EVENT:
                VBox vBox = (VBox) node;
                vBox.getChildren().add(0, viewBlock);
                break;
            //Drop on %xyz%
            case EXPRESSION, TYPE, TYPE_LIST:
                Pane pane = (Pane) node.getParent();
                int index = pane.getChildren().indexOf(node);
                pane.getChildren().set(index, viewBlock);
                break;
            //Drop on VBox
            case SECTION, EFFECT, COMMENT:
                VBox vBox1 = (VBox) node;
                if (!Objects.isNull(DropSystem.getCurrentDropUnderNode())){
                    int index1 = vBox1.getChildren().indexOf(DropSystem.getCurrentDropUnderNode());
                    vBox1.getChildren().add(index1+1, viewBlock);
                    break;
                }
                vBox1.getChildren().add(vBox1.getChildren().size()-1, viewBlock);
                break;
            //Drop on VBox and %xyz%
            case CONDITION:
                if (((Conditions)viewBlock).getInVBox()){
                    VBox vBox2 = (VBox) node;
                    if (!Objects.isNull(DropSystem.getCurrentDropUnderNode())){
                        int index2 = vBox2.getChildren().indexOf(DropSystem.getCurrentDropUnderNode());
                        vBox2.getChildren().add(index2+1, viewBlock);
                        break;
                    }
                    vBox2.getChildren().add(vBox2.getChildren().size()-1, viewBlock);
                } else {
                    Pane pane1 = (Pane) node.getParent();
                    int indexe1 = pane1.getChildren().indexOf(node);
                    pane1.getChildren().set(indexe1, viewBlock);
                }
                break;
            case FUNCTION:
                if (((Function)viewBlock).getInVBox()){
                    VBox vBox3 = (VBox) node;
                    if (!Objects.isNull(DropSystem.getCurrentDropUnderNode())){
                        int index3 = vBox3.getChildren().indexOf(DropSystem.getCurrentDropUnderNode());
                        vBox3.getChildren().add(index3+1, viewBlock);
                        break;
                    }
                    vBox3.getChildren().add(vBox3.getChildren().size()-1, viewBlock);
                } else {
                    Pane pane1 = (Pane) node.getParent();
                    int indexe1 = pane1.getChildren().indexOf(node);
                    pane1.getChildren().set(indexe1, viewBlock);
                }
                break;
            //Drop on buildTab and %xyz%
            case STRUCTURE:
                if (((Structure)viewBlock).isInExpression()){
                    Pane pane1 = (Pane) node.getParent();
                    int indexe1 = pane1.getChildren().indexOf(node);
                    pane1.getChildren().set(indexe1, viewBlock);
                } else {
                    VBox vBox4 = (VBox) node;
                    vBox4.getChildren().add(0, viewBlock);
                    break;
                }
                break;

        }
    }
}