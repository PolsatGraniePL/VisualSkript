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

import java.util.*;

import static com.polsat.visualskript.system.script.ScriptParser.build;

public class Structure extends ViewBlock implements Placeable {

    private final StrType type;

    public Structure(Block block, String oldText){
        super(block);
        this.type = StrType.valueOf(block.getName().substring(12).toUpperCase());

        oldText(oldText);
        if (isInExpression()){
            this.setStyle(this.getStyle()+"-fx-background-radius: 25px; -fx-border-radius: 25px;");
            hbox().contextMenu().label("["+block.getName().substring(12)+"]").margins().textField();
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
        contextMenu();

        switch (this.type){
            case ALIASES:
                vBox.getChildren().addAll(new StrAliases());
                break;
            case COMMAND:
                HBox Cmd_hb = new HBox();
                Cmd_hb.setAlignment(Pos.CENTER);
                Cmd_hb.setFillHeight(false);
                Label label = new Label("Arguments: ");
                label.setFont(new Font("System", 24));
                label.setPadding(new Insets(5, 5, 5, 5));
                Cmd_hb.getChildren().addAll(label, new DropViewExpr("arguments list"));
                vBox.getChildren().addAll(Cmd_hb);
                break;
            case FUNCTION:
                HBox Func_hb = new HBox();
                Func_hb.setAlignment(Pos.CENTER);
                Func_hb.setFillHeight(false);
                Label Func_label = new Label("Returns: ");
                Func_label.setFont(new Font("System", 24));
                Func_label.setPadding(new Insets(5, 5, 5, 5));
                Func_hb.getChildren().addAll(Func_label, new DropViewExpr("type"));
                vBox.getChildren().addAll(Func_hb);
                setCombinations();
                break;
            case OPTIONS:
                vBox.getChildren().addAll(new StrOptions());
                break;
            case VARIABLES:
                vBox.getChildren().addAll(new StrVariables());
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

    @Override
    public void buildMenu() {
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().add(delete);
        delete.setOnAction(event -> {
            if (this.getParent() instanceof VBox vbox) {
                //TODO: DELETE CURRENT COMPONENT
            } else {
                ((HBox)this.getParent()).getChildren().set(((HBox)this.getParent()).getChildren().indexOf(this), new DropViewExpr(oldText));
            }
            build();
        });
        if (!isInExpression())
            switch (type){
                case ALIASES:
                    createMenuItems("Add new alias", "Remove alias", StrType.ALIASES);
                    break;
                case COMMAND:
                    for (StrCommand entre : toUse){
                        MenuItem cmdAddItem = new MenuItem("Add/Remove " + entre.getEntreName());
                        contextMenu.getItems().add(cmdAddItem);
                        cmdAddItem.setOnAction(event -> {
                            if (vBox.getChildren().indexOf(entre) > 0) {
                                inUse.remove(entre);
                                toUse.add(entre);
                                vBox.getChildren().remove(entre);
                                return;
                            }
                            toUse.remove(entre);
                            inUse.add(entre);
                            vBox.getChildren().add(entre);
                        });
                    }
                    break;
                case FUNCTION:
                    createMenuItems("Add new argument", "Remove argument", StrType.FUNCTION);
                    break;
                case OPTIONS:
                    createMenuItems("Add new option", "Remove option", StrType.OPTIONS);
                    break;
                case VARIABLES:
                    createMenuItems("Add new variable", "Remove variable", StrType.VARIABLES);
                    break;
            }

    }

    private enum StrType{
        ALIASES,
        COMMAND,
        FUNCTION,
        OPTIONS,
        VARIABLES
    }

    private void createMenuItems(String addItem, String removeItem, StrType type){
        MenuItem varAddItem = new MenuItem(addItem);
        MenuItem varRemoveItem = new MenuItem(removeItem);
        contextMenu.getItems().addAll(varAddItem, varRemoveItem);
        varAddItem.setOnAction(event -> {
            switch (type){
                case ALIASES -> vBox.getChildren().add(new StrAliases());
                case FUNCTION -> vBox.getChildren().add(new StrFunction());
                case OPTIONS -> vBox.getChildren().add(new StrOptions());
                case VARIABLES -> vBox.getChildren().add(new StrVariables());
            }
        });
        varRemoveItem.setOnAction(event -> {
            if (vBox.getChildren().size()>2) {
                vBox.getChildren().remove(vBox.getChildren().size() - 1);
            }
        });
    }

    private static abstract class StrMain extends HBox{
        Label label = new Label();

        //StrAliases, StrOptions, StrVariables, StrFunction
        StrMain(String obj1, String separator, String obj2){
            createBlock(separator);
            this.getChildren().addAll(new DropViewExpr(obj1), label, new DropViewExpr(obj2));
        }
        //StrCommand
        StrMain(String text, String obj){
            createBlock(text);
            this.getChildren().addAll(label, new DropViewExpr(obj));
        }

        private void createBlock(String text){
            label.setText(text);
            label.setFont(new Font("System", 24));
            label.setPadding(new Insets(5, 5, 5, 5));
            this.setAlignment(Pos.CENTER);
            this.setFillHeight(false);
        }
    }

    private static class StrAliases extends StrMain{
        StrAliases(){
            super("custom alias", " = ", "aliases list");
        }
    }

    List<StrCommand> toUse = new ArrayList<>(Arrays.asList(
            new StrCommand("usage"),
            new StrCommand("description"),
            new StrCommand("prefix"),
            new StrCommand("permission"),
            new StrCommand("permission message"),
            new StrCommand("aliases"),
            new StrCommand("executable by"),
            new StrCommand("cooldown"),
            new StrCommand("cooldown message"),
            new StrCommand("cooldown bypass"),
            new StrCommand("cooldown storage")
    ));
    List<StrCommand> inUse = new ArrayList<>();

    private static class StrCommand extends StrMain{
        private final String entre;

        StrCommand(String entre){
            super(entre + ": ", "object");
            this.entre = entre;
        }

        public String getEntreName() {
            return entre;
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