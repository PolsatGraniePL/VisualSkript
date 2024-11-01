package com.polsat.visualskript.gui.manager.view;

import com.polsat.visualskript.gui.block.Block;
import com.polsat.visualskript.gui.block.BlockType;
import com.polsat.visualskript.gui.manager.block.SelectiveBlock;
import com.polsat.visualskript.gui.manager.drop.DropSystem;
import com.polsat.visualskript.gui.manager.view.blocks.Structure;
import com.polsat.visualskript.gui.manager.view.popovers.SelectBoxPopOver;
import com.polsat.visualskript.system.pattern.PatternExtractor;
import com.polsat.visualskript.util.ErrorHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.*;

import static com.polsat.visualskript.system.script.ScriptParser.build;

public abstract class ViewBlock extends Pane implements Menu {

    protected ContextMenu contextMenu = new ContextMenu();

    protected Block block;
    protected String oldText;

    protected HBox hBox;
    protected Label label;
    protected TextField textField;
    protected VBox vBox;
    protected Pane emptyPane;
    protected VBox dropVBox;
    protected Tooltip tooltip;

    /**For DropViewExpr*/
    protected ViewBlock(){
        this.setStyle("-fx-background-radius: 25px; -fx-background-color: #ffc0cb; -fx-border-color: #000000; -fx-border-radius: 25px;");
    }

    /**For any other placeable block*/
    protected ViewBlock(Block block){
        this.block = block;
        this.setStyle("-fx-background-color: #"+ block.getType().getHexColor()+"; -fx-border-color: #000000; ");
    }

//<editor-fold desc="Blocks loader">

    List<Node> controlList;

    /**For load blocks*/
    protected ViewBlock(List<Node> controlList, Block block){
        this.setStyle("-fx-background-color: #"+ block.getType().getHexColor()+"; -fx-border-color: #000000; ");
        this.controlList = controlList;
        this.block = block;
    }

    protected void setuper(){
        if (!Objects.isNull(controlList)) {
            for (Node control : controlList) {
                if (control instanceof Label) {
                    System.out.println(this.getClass());
                    hBox.getChildren().add(control);
                } else if (control instanceof ViewBlock) {
                    hBox.getChildren().add(control);
                } else {
                    ErrorHandler.alert("Unknown control type: " + control.getClass().getSimpleName());
                }
            }
        }
    }

//</editor-fold>

    @Override
    public void buildMenu() {
        MenuItem edit = new MenuItem("Edit");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(edit, delete);
        edit.setOnAction(event -> {
            setCombinations();
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

    public Block getBlock() {
        return block;
    }

    public VBox getDropVBox() {
        return dropVBox;
    }

    public HBox gethBox() {
        return hBox;
    }

    public VBox getvBox() {
        return vBox;
    }

    public TextField getTextField() {
        return textField;
    }

    /**<h1>UTIL METHODS</h1>**/

    protected void setCombinations() {
        List<String> patternList = Arrays.asList(block.getPattern().split("\n"));
        if (patternList.size() == 1){
            //Show only popover with combinations
            List<String> combinationsList = PatternExtractor.getCombinations(PatternExtractor.getFirstPattern(block.getPattern()));
            showCombinations(combinationsList);
            return;
        }
        //Show popovers with patterns and combinations
        new SelectBoxPopOver(patternList, this, result -> {
            Platform.runLater(() -> {
                if (block.getType().getPlaceOnExpr()) {
                    label.setText(result);
                } else {
                    label.setText("[" + block.getType().getName() + "] " + result);
                }
            });
            List<String> combinationsList = PatternExtractor.getCombinations(result);
            showCombinations(combinationsList);
        });
    }

    private void showCombinations(List<String> combinationsList) {
        Collections.reverse(combinationsList);
        Platform.runLater(()-> new SelectBoxPopOver(combinationsList, this, result2 ->{
            Platform.runLater(() -> {
                if (block.getType().getPlaceOnExpr()){
                    label.setText(result2);
                } else {
                    label.setText("[" + block.getType().getName() + "] " + result2);
                }
                setupDropViews();
            });
        }));
    }

    protected void setupDropViews(){
        String[] list = label.getText()
                .replace("<.+>", "%object%")
                .replace("<.*>", "%object%")
                .replace("<\\d+>", "%number%")
                .replace("\\%", "\uEF02")
                .split("%");
        hBox.getChildren().clear();
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < list.length; i++){
            if (i % 2 == 0){
                Label tempLabel = new Label(list[i].trim().replace("\uEF02", "%"));
                tempLabel.setFont(new Font("System", 24));
                tempLabel.setPadding(new Insets(5, 5, 5, 5));
                nodes.add(tempLabel);
            } else {
                nodes.add(new DropViewExpr(list[i].trim()));
            }
        }
        hBox.getChildren().addAll(0, nodes);
        build();
    }

    /** <h1>BUILDER</h1>**/

    public ViewBlock contextMenu(){
        buildMenu();
        this.setOnContextMenuRequested((e) -> {
            e.consume();
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });
        return this;
    }

    public ViewBlock hbox(){
        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setFillHeight(false);
        return this;
    }

    public ViewBlock thinLabel(String text){
        label(text);
        label.setPadding(new Insets(0, 5, 0, 5));
        return this;
    }

    public ViewBlock label(String text){
        label = new Label(text);
        label.setFont(new Font("System", 24));
        label.setPadding(new Insets(5, 5, 5, 5));
        return this;
    }

    public ViewBlock label(){
        if (block.getType().getPlaceOnExpr() && Objects.equals(block.getType(), BlockType.STRUCTURE)){
            label(PatternExtractor.getFirstPattern(block.getPattern()));
            return this;
        }
        label("["+block.getType().getName()+"] " + PatternExtractor.getFirstPattern(block.getPattern()));
        return this;
    }

    public ViewBlock dropGlowing(){
        this.setOnDragOver(event -> {
            if ((this.getLayoutBounds().getMaxY()/2)>event.getY()){
                //UP
                InnerShadow effect = new InnerShadow();
                effect.setInput(new Glow(0.2));
                effect.setOffsetY(2);
                setEffect(effect);
                DropSystem.setOffset(0);
            } else {
                //DOWN
                InnerShadow effect = new InnerShadow();
                effect.setInput(new Glow(0.2));
                effect.setOffsetY(-2);
                setEffect(effect);
                DropSystem.setOffset(1);
            }
        });
        this.setOnDragEntered(event -> {
            if (((SelectiveBlock) event.getGestureSource()).getBlock().getType().getPlaceOnVBox()) {
                DropSystem.addNode(this);
            }
        });
        this.setOnDragExited(event -> {
            DropSystem.removeNode(this);
            setEffect(null);
        });
        return this;
    }

    public ViewBlock glowing(){
        this.setOnDragEntered(event -> {
            if (((SelectiveBlock) event.getGestureSource()).getBlock().getType().getPlaceOnExpr())
                setEffect(new Glow(0.2));
        });
        this.setOnDragExited(event -> setEffect(null));
        return this;
    }

    public ViewBlock oldText(String oldText) {
        this.oldText = oldText;
        return this;
    }

    public ViewBlock toolTip(String text) {
        tooltip = new Tooltip(text);
        Tooltip.install(this, tooltip);
        return this;
    }

    public ViewBlock margins() {
        HBox.setMargin(this, new Insets(5, 0, 5, 0));
        VBox.setMargin(this, new Insets(5, 5, 5, 5));
        return this;
    }

    public ViewBlock textField() {
        textField("");
        new Timeline(new KeyFrame(Duration.seconds(0.01), event -> textField.requestFocus())).playFromStart();
        return this;
    }

    public ViewBlock textField(String string) {
        textField = new TextField(string);
        textField.setStyle("-fx-background-radius: 25px; -fx-background-color: #c6c6c6; -fx-border-radius: 25px; -fx-focus-color: transparent; -fx-border-color: #000000");
        textField.setFont(new Font("System", 20));
        textField.setTranslateX(-5);
        textField.setPrefWidth(25);
        textField.textProperty().addListener((ov, prevText, currText) -> Platform.runLater(() -> {
            Text text = new Text(currText);
            text.setFont(textField.getFont());
            double width = text.getLayoutBounds().getWidth() + textField.getPadding().getLeft() + textField.getPadding().getRight() + 4d;
            textField.setPrefWidth(width);
            textField.positionCaret(textField.getCaretPosition());
            build();
        }));
        HBox.setMargin(textField, new Insets(5, 5, 5, 5));
        return this;
    }

    public ViewBlock showCombinations() {
        //Wait 0.01 second and show SelectBoxPopOver with patterns to select.
        new Timeline(new KeyFrame(Duration.seconds(0.01), event -> setCombinations())).playFromStart();
        return this;
    }

    public ViewBlock vbox() {
        vBox = new VBox();
        vBox.setFillWidth(false);
        vBox.setAlignment(Pos.CENTER_LEFT);
        return this;
    }

    public ViewBlock dropVBox() {
        dropVBox = new VBox();
        dropVBox.setFillWidth(true);
        this.setOnDragOver(event -> {
            if (((SelectiveBlock) event.getGestureSource()).getBlock().getType().getPlaceOnVBox())
            {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
        this.setOnDragDropped(event -> {
            Block placedBlock = ((SelectiveBlock) event.getGestureSource()).getBlock();
            boolean success = false;
            if (placedBlock.getType().getPlaceOnVBox())
            {
                placedBlock.getType().place(placedBlock, null, dropVBox);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        VBox.setMargin(dropVBox, new Insets(0, 5, 5, 30));
        return this;
    }

    public ViewBlock emptyPane() {
        emptyPane = new Pane();
        return this;
    }
}
