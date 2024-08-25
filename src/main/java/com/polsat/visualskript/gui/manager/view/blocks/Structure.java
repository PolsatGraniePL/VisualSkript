package com.polsat.visualskript.gui.manager.view.blocks;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.manager.block.BlockPlacer;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.Placeable;
import com.polsat.visualskript.gui.manager.view.popovers.SelectBoxPopOver;
import com.polsat.visualskript.system.pattern.PatternExtractor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Structure extends ViewBlock implements Placeable {

    private StrType type;

    public Structure(Block block, String oldText){
        super(block);
        System.out.println(block.getName().substring(12));
        switch (block.getName().substring(12)){
            case "Aliases" -> this.type = StrType.ALIASES;
            case "Command" -> this.type = StrType.COMMAND;
            case "Function" -> this.type = StrType.FUNCTION;
            case "Options" -> this.type = StrType.OPTIONS;
            case "Variables" -> this.type = StrType.VARIABLES;
        }

        if (isInExpression()){
            this.setStyle(this.getStyle()+"-fx-background-radius: 25px; -fx-border-radius: 25px;");
            oldText(oldText).hbox().contextMenu().label("["+block.getName().substring(12)+"]").margins().textField();
            hBox.getChildren().addAll(label, textField);
            this.getChildren().add(hBox);
            return;
        }
        this.setStyle(this.getStyle()+"-fx-background-radius: 10px 10px 0 0; -fx-border-radius: 10px 10px 0 0;");


        vbox().hbox().dropGlowing().label();
        hBox.getChildren().add(label);
        vBox.getChildren().add(hBox);
        this.getChildren().add(vBox);
        setupDropViews();

        switch (this.type){
            case ALIASES:
                vBox.getChildren().addAll(new StrAliases(), new StrAliases());
                break;
            case COMMAND:
                HBox Cmd_hb = new HBox();
                Cmd_hb.setAlignment(Pos.CENTER);
                Cmd_hb.setFillHeight(false);
                Label label = new Label("Arguments: ");
                label.setFont(new Font("System", 24));
                label.setPadding(new Insets(5, 5, 5, 5));
                Cmd_hb.getChildren().addAll(label, new DropViewExpr("arguments list"));
                vBox.getChildren().addAll(Cmd_hb, new StrCommand("permission"), new StrCommand("aliases"));
                break;
            case FUNCTION:
                HBox Func_hb = new HBox();
                Func_hb.setAlignment(Pos.CENTER);
                Func_hb.setFillHeight(false);
                Label Func_label = new Label("Returns: ");
                Func_label.setFont(new Font("System", 24));
                Func_label.setPadding(new Insets(5, 5, 5, 5));
                Func_hb.getChildren().addAll(Func_label, new DropViewExpr("type"));
                vBox.getChildren().addAll(Func_hb, new StrFunction(), new StrFunction());
                setCombinations();
                break;
            case OPTIONS:
                vBox.getChildren().addAll(new StrOptions(), new StrOptions());
                break;
            case VARIABLES:
                vBox.getChildren().addAll(new StrVariables(), new StrVariables());
                break;

        }

    }

    public boolean isInExpression() {
        return Objects.nonNull(oldText);
    }

    @Override
    public void place(Node node) {
        if (this.isInExpression()){
            BlockPlacer.placeOnExpr(this, node);
            return;
        }
        BlockPlacer.placeOnBuildTab(this, node);
    }

    //TODO: buildMenu zależne od typu str. (Add, remove argument)
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

    private enum StrType{
        ALIASES,
        COMMAND,
        FUNCTION,
        OPTIONS,
        VARIABLES
    }

    private static class StrMain extends HBox{
        //StrAliases, StrOptions, StrVariables, StrFunction
        StrMain(String obj1, String separator, String obj2){
            Label label = new Label(separator);
            label.setFont(new Font("System", 24));
            label.setPadding(new Insets(5, 5, 5, 5));
            this.setAlignment(Pos.CENTER);
            this.setFillHeight(false);
            this.getChildren().addAll(new DropViewExpr(obj1), label, new DropViewExpr(obj2));
        }
        //StrCommand
        StrMain(String text, String obj){
            Label label = new Label(text);
            label.setFont(new Font("System", 24));
            label.setPadding(new Insets(5, 5, 5, 5));
            this.setAlignment(Pos.CENTER);
            this.setFillHeight(false);
            this.getChildren().addAll(label, new DropViewExpr(obj));
        }
    }

    private static class StrAliases extends StrMain{
        StrAliases(){
            super("custom alias", " = ", "aliases list");
        }
    }

    private static class StrCommand extends StrMain{
        StrCommand(String entre){
            super(entre + ": ", "object");
        }
    }

    private static class StrFunction extends StrMain{
        StrFunction(){
            super("variable name", ": ", "type");
        }
    }

    private static class StrOptions extends StrMain{
        StrOptions(){
            super("option name", ": ", "object");
        }
    }

    private static class StrVariables extends StrMain{
        StrVariables(){
            super("variable", " = ", "object");
        }
    }

}