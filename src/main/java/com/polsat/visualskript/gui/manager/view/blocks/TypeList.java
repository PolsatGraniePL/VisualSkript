package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class TypeList extends ViewBlock {

    private final ContextMenu contextMenu = new ContextMenu();
    private boolean contextMenuBuilt = false;

    public TypeList(Block block) {
        super(block, block.getName());
        //build view box
        VBox vBox = new VBox();
        HBox hbox = new HBox();
        Label label = new Label();

        this.setStyle("-fx-background-color: #"+ block.getType().getHexColor()+"; -fx-border-color: #000000; -fx-background-radius: 25px; -fx-border-radius: 25px;");

        vBox.setFillWidth(false);
        vBox.setAlignment(Pos.CENTER_LEFT);

        hbox.setAlignment(Pos.CENTER);
        hbox.setFillHeight(false);
        label.setText("["+block.getName().substring(8)+"]");
        label.setFont(new Font("System", 24));
        label.setPadding(new Insets(5, 5, 5, 5));
        HBox.setMargin(this, new Insets(5, 5, 5, 5));
        VBox.setMargin(this, new Insets(5, 5, 5, 5));

        hbox.getChildren().add(label);
        vBox.getChildren().addAll(hbox, newDropViewExpr(), newDropViewExpr());
        this.getChildren().add(vBox);

        this.setOnContextMenuRequested((e) -> {
            if (!contextMenuBuilt) {
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
                    System.out.println("Delete");
                });
            }
            contextMenuBuilt = true;
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });
    }

    private DropViewExpr newDropViewExpr() {
        DropViewExpr dropViewExpr = new DropViewExpr("Object");
        HBox.setMargin(dropViewExpr, new Insets(5, 5, 5, 5));
        VBox.setMargin(dropViewExpr, new Insets(5, 5, 5, 5));
        return dropViewExpr;
    }
}