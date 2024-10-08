package com.polsat.visualskript.system.script;

import com.polsat.visualskript.Main;
import com.polsat.visualskript.gui.Controller;
import com.polsat.visualskript.gui.manager.block.BlockManager;
import com.polsat.visualskript.gui.manager.view.DropViewExpr;
import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.blocks.Structure;
import com.polsat.visualskript.util.ErrorHandler;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;

public class BuildThread extends Thread{

    public void run(){
        try {
            TabPane tabPane = (TabPane) BlockManager.getBuildTab().getSelectionModel().getSelectedItem().getContent();

            String skriptLoc = Main.class.getResource("/scripts/").getPath();
            String skriptName = BlockManager.getBuildTab().getSelectionModel().getSelectedItem().getText();

            File file = new File(skriptLoc + skriptName);

            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println("#open: true");
            printWriter.println();

            tabPane.getTabs().forEach((tab)->{
                VBox vBox = (VBox)((ScrollPane) tab.getContent()).getContent();
                additionalDepth = 0;
                vBox.getChildren().forEach((viewBlock)->{
                    if (viewBlock instanceof ViewBlock block) {
                        printWriter.println(recurency(block, 1));
                    }
                });
                printWriter.println();
            });

            printWriter.close();
            fileWriter.close();

            Scanner scanner = new Scanner(file);
            Platform.runLater(() -> {
                Controller.sfileViewer.setText("");
                while (scanner.hasNextLine()) {
                    Controller.sfileViewer.setText(Controller.sfileViewer.getText() + scanner.nextLine() + "\n");
                }
                scanner.close();
            });

        } catch (Exception e){
            ErrorHandler.alert(e.toString());
        }
    }

    private static int additionalDepth = 0;

    private static String recurency(ViewBlock viewBlock, int depth){
        StringBuilder builder = new StringBuilder();
        String tabs = "\t".repeat(depth+additionalDepth);
        viewBlock.getChildren().forEach((node -> {

            switch (viewBlock.getBlock().getType()){
                case EVENT:
                    builder.append(blockOnHbox((HBox) node)).append(":");
                    break;
                case STRUCTURE:
                    builder.append(structureBlockOnHbox(viewBlock));
                    break;
                case CONDITION, EFFECT:
                    builder.append(tabs).append(blockOnHbox((HBox) node));
                    break;
                case FUNCTION:
                    builder.append(tabs).append(functionBlockOnHbox((HBox) node));
                    break;
                case COMMENT:
                    builder.append(tabs).append("#").append(viewBlock.getTextField().getText());
                    break;
                case SECTION:
                    builder.append(tabs).append(blockOnHbox((HBox) viewBlock.getvBox().getChildren().get(0))).append(":");
                    viewBlock.getDropVBox().getChildren().forEach((hBox)->{
                        if (hBox instanceof ViewBlock block) {
                            builder.append("\n").append(recurency(block, depth+additionalDepth+1));
                        }
                    });
                    break;
            }
        }));
        return builder.toString();
    }

    private static String blockOnHbox(HBox hBox){
        StringBuilder stringBuilder = new StringBuilder();
        hBox.getChildren().forEach((node -> {
            if (node instanceof Label label){
                stringBuilder.append(getNameWithoutType(label.getText()));
            } else if (node instanceof DropViewExpr dropViewExpr){
                stringBuilder.append(space(stringBuilder)).append("%").append(blockOnHbox(dropViewExpr.gethBox())).append("%").append(space(stringBuilder));
            } else if (node instanceof ViewBlock viewBlock){
                viewBlockCompiler(viewBlock, stringBuilder);
            } else {
                stringBuilder.append("[").append(node.getClass().getName()).append("]");
            }
        }));
        return stringBuilder.toString();
    }

    private static String blockOnHboxNoSpace(HBox hBox){
        StringBuilder stringBuilder = new StringBuilder();
        hBox.getChildren().forEach((node -> {
            if (node instanceof Label label){
                stringBuilder.append(getNameWithoutType(label.getText()));
            } else if (node instanceof DropViewExpr dropViewExpr){
                stringBuilder.append("%").append(blockOnHbox(dropViewExpr.gethBox())).append("%");
            } else if (node instanceof ViewBlock viewBlock){
                viewBlockCompilerNoSpace(viewBlock, stringBuilder);
            } else {
                stringBuilder.append("[").append(node.getClass().getName()).append("]");
            }
        }));
        return stringBuilder.toString();
    }

    private static String functionBlockOnHbox(HBox hBox){
        StringBuilder stringBuilder = new StringBuilder();
        hBox.getChildren().forEach((node -> {
            if (node instanceof DropViewExpr dropViewExpr){
                stringBuilder.append(space(stringBuilder)).append("%").append(blockOnHbox(dropViewExpr.gethBox())).append("%").append(space(stringBuilder));
            } else if (node instanceof ViewBlock viewBlock){
                viewBlockCompiler(viewBlock, stringBuilder);
            } else if (node instanceof Label label){
                if (!label.getText().contains(":")){
                    stringBuilder.append(label.getText());
                }
            }
        }));
        return stringBuilder.toString().replaceFirst("\\[", "").replaceFirst("]", "(") + ")";
    }

    private static String structureBlockOnHbox(ViewBlock node) {
        StringBuilder stringBuilder = new StringBuilder();
        node.getChildren().forEach((vbox) -> {
            if (vbox instanceof VBox vBox && node instanceof Structure structure) {
                final boolean[] functionCompiled = {false};
                vBox.getChildren().forEach((hBox) -> {
                    if (hBox instanceof HBox hBoxBlock) {
                        switch (structure.getType()) {
                            case ALIASES, OPTIONS, VARIABLES:
                                if (hBoxBlock.getChildren().size() == 1) {
                                    stringBuilder.append(blockOnHbox(hBoxBlock)).append(":\n");
                                } else {
                                    stringBuilder.append("\t").append(blockOnHbox(hBoxBlock).replaceFirst(" {2}= ", " = ")).append("\n");
                                }
                                break;
                            case COMMAND:
                                if (vBox.getChildren().indexOf(hBox) == 0) {
                                    stringBuilder.append(blockOnHbox(hBoxBlock));
                                } else if (vBox.getChildren().indexOf(hBox) == 1){
                                    stringBuilder.append(blockOnHboxNoSpace(hBoxBlock).replaceFirst("Arguments: ", "").replaceFirst("%arguments list%", "")).append(":");
                                } else {
                                    stringBuilder.append("\n\t").append(blockOnHbox(hBoxBlock));
                                }
                                break;
                            case FUNCTION:
                                if (!functionCompiled[0]) {
                                    stringBuilder.append(functionStructureCompile(vBox).replaceFirst(" :: %type%", ""));
                                    functionCompiled[0] = true;
                                }
                                break;
                        }
                    }
                });
                if (structure.getType() == Structure.StrType.COMMAND){
                    stringBuilder.append("\n\ttrigger:");
                    additionalDepth=1;
                }
            }
        });
        return stringBuilder.toString();
    }

    private static String functionStructureCompile(VBox vBox){
        StringBuilder stringBuilder = new StringBuilder();
        String functionName = blockOnHbox((HBox)vBox.getChildren().get(0));
        String functionReturn = blockOnHboxNoSpace((HBox)vBox.getChildren().get(1)).replaceFirst("Returns:", "");
        String functionArguments = functionStructureCompileArguments(vBox);
        stringBuilder.append(functionName, 0, functionName.length() - 1).append("(").append(functionArguments).append(") ::").append(functionReturn).append(":");
        return stringBuilder.toString();
    }

    private static String functionStructureCompileArguments(VBox vBox){
        StringBuilder stringBuilder = new StringBuilder();
        vBox.getChildren().forEach((hbox) -> {
            if (hbox instanceof HBox hBox) {
                if (!((vBox.getChildren().indexOf(hBox) == 0) || (vBox.getChildren().indexOf(hBox) == 1))){
                    if (!stringBuilder.isEmpty()) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(blockOnHboxNoSpace(hBox));
                }
            }
        });
        return stringBuilder.toString();
    }

    private static void viewBlockCompiler(ViewBlock viewBlock, StringBuilder stringBuilder){
        switch (viewBlock.getBlock().getType()){
            case CONDITION, EXPRESSION -> stringBuilder.append(space(stringBuilder)).append(blockOnHbox(viewBlock.gethBox())).append(space(stringBuilder));
            case FUNCTION -> stringBuilder.append(space(stringBuilder)).append(functionBlockOnHbox(viewBlock.gethBox())).append(space(stringBuilder));
            case TYPE, STRUCTURE -> {
                switch (getNameWithoutType(viewBlock.getBlock().getName())){
                    case "Text":
                        stringBuilder.append(space(stringBuilder)).append("\"").append(viewBlock.getTextField().getText()).append("\"").append(space(stringBuilder));
                        break;
                    case "Variables":
                        stringBuilder.append(space(stringBuilder)).append("{").append(viewBlock.getTextField().getText()).append("}").append(space(stringBuilder));
                        break;
                    case "Options":
                        stringBuilder.append(space(stringBuilder)).append("{@").append(viewBlock.getTextField().getText()).append("}").append(space(stringBuilder));
                        break;
                    case "World":
                        stringBuilder.append(space(stringBuilder)).append("world \"").append(viewBlock.getTextField().getText()).append("\"").append(space(stringBuilder));
                        break;
                    default:
                        stringBuilder.append(space(stringBuilder)).append(viewBlock.getTextField().getText()).append(space(stringBuilder));
                        break;
                }

            }
            case TYPE_LIST -> {
                switch (((Label)((HBox) viewBlock.getvBox().getChildren().get(0)).getChildren().get(0)).getText()){
                    case "[Combine Texts]":
                        stringBuilder.append(space(stringBuilder)).append("\"");
                        viewBlock.getvBox().getChildren().forEach(block -> {

                            if (block instanceof DropViewExpr dropViewExpr) {
                                stringBuilder.append("%").append(blockOnHbox(dropViewExpr.gethBox())).append("%");
                            } else if (block instanceof ViewBlock viewBlock1) {
                                viewBlockCompilerInText(viewBlock1, stringBuilder);
                            }
                        });
                        stringBuilder.append("\"").append(space(stringBuilder));
                        break;
                    case "[Objects List]":
                        viewBlock.getvBox().getChildren().forEach(block -> {
                            if (viewBlock.getvBox().getChildren().indexOf(block) > 1) {
                                stringBuilder.append(",");
                            }

                            if (block instanceof DropViewExpr dropViewExpr) {
                                stringBuilder.append(space(stringBuilder)).append("%").append(blockOnHbox(dropViewExpr.gethBox())).append("%").append(space(stringBuilder));
                            } else if (block instanceof ViewBlock viewBlock1) {
                                viewBlockCompiler(viewBlock1, stringBuilder);
                            }
                        });
                        break;
                }


            }
        }
    }
    private static void viewBlockCompilerNoSpace(ViewBlock viewBlock, StringBuilder stringBuilder){
        switch (viewBlock.getBlock().getType()){
            case CONDITION, EXPRESSION -> stringBuilder.append(blockOnHbox(viewBlock.gethBox()));
            case FUNCTION -> stringBuilder.append(functionBlockOnHbox(viewBlock.gethBox()));
            case TYPE, STRUCTURE -> {
                switch (getNameWithoutType(viewBlock.getBlock().getName())){
                    case "Text":
                        stringBuilder.append("\"").append(viewBlock.getTextField().getText()).append("\"");
                        break;
                    case "Variables":
                        stringBuilder.append("{").append(viewBlock.getTextField().getText()).append("}");
                        break;
                    case "Options":
                        stringBuilder.append("{@").append(viewBlock.getTextField().getText()).append("}");
                        break;
                    case "World":
                        stringBuilder.append("world \"").append(viewBlock.getTextField().getText()).append("\"");
                        break;
                    default:
                        stringBuilder.append(viewBlock.getTextField().getText());
                        break;
                }

            }
            case TYPE_LIST -> {
                switch (((Label)((HBox) viewBlock.getvBox().getChildren().get(0)).getChildren().get(0)).getText()){
                    case "[Combine Texts]":
                        stringBuilder.append("\"");
                        viewBlock.getvBox().getChildren().forEach(block -> {

                            if (block instanceof DropViewExpr dropViewExpr) {
                                stringBuilder.append("%").append(blockOnHbox(dropViewExpr.gethBox())).append("%");
                            } else if (block instanceof ViewBlock viewBlock1) {
                                viewBlockCompilerInText(viewBlock1, stringBuilder);
                            }
                        });
                        stringBuilder.append("\"");
                        break;
                    case "[Objects List]":
                        viewBlock.getvBox().getChildren().forEach(block -> {
                            if (viewBlock.getvBox().getChildren().indexOf(block) > 1) {
                                stringBuilder.append(",");
                            }

                            if (block instanceof DropViewExpr dropViewExpr) {
                                stringBuilder.append("%").append(blockOnHbox(dropViewExpr.gethBox())).append("%");
                            } else if (block instanceof ViewBlock viewBlock1) {
                                viewBlockCompiler(viewBlock1, stringBuilder);
                            }
                        });
                        break;
                }


            }
        }
    }

    private static void viewBlockCompilerInText(ViewBlock viewBlock, StringBuilder stringBuilder){
        switch (viewBlock.getBlock().getType()){
            case CONDITION, EXPRESSION -> stringBuilder.append("%").append(blockOnHbox(viewBlock.gethBox())).append("%");
            case FUNCTION -> stringBuilder.append(functionBlockOnHbox(viewBlock.gethBox()));
            case TYPE, STRUCTURE -> {
                switch (getNameWithoutType(viewBlock.getBlock().getName())){
                    case "Text":
                        stringBuilder.append(viewBlock.getTextField().getText());
                        break;
                    case "Variables":
                        stringBuilder.append("%{").append(viewBlock.getTextField().getText()).append("}%");
                        break;
                    case "Options":
                        stringBuilder.append("{@").append(viewBlock.getTextField().getText()).append("}");
                        break;
                    default:
                        stringBuilder.append("%").append(viewBlock.getTextField().getText()).append("%");
                        break;
                }

            }
            case TYPE_LIST -> {
                switch (((Label)((HBox) viewBlock.getvBox().getChildren().get(0)).getChildren().get(0)).getText()){
                    case "[Combine Texts]":
                        viewBlock.getvBox().getChildren().forEach(block -> {

                            if (block instanceof DropViewExpr dropViewExpr) {
                                stringBuilder.append("%").append(blockOnHbox(dropViewExpr.gethBox())).append("%");
                            } else if (block instanceof ViewBlock viewBlock1) {
                                viewBlockCompilerInText(viewBlock1, stringBuilder);
                            }
                        });
                        break;
                    case "[Objects List]":
                        viewBlock.getvBox().getChildren().forEach(block -> {
                            if (viewBlock.getvBox().getChildren().indexOf(block) > 1) {
                                stringBuilder.append(",");
                            }

                            if (block instanceof DropViewExpr dropViewExpr) {
                                stringBuilder.append(space(stringBuilder)).append("%").append(blockOnHbox(dropViewExpr.gethBox())).append("%").append(space(stringBuilder));
                            } else if (block instanceof ViewBlock viewBlock1) {
                                viewBlockCompilerInText(viewBlock1, stringBuilder);
                            }
                        });
                        break;
                }


            }
        }
    }

    private static String getNameWithoutType(String text){
        return text.substring(Objects.equals(text.indexOf("] "), -1) ? 0 : text.indexOf("] ") + 2);
    }

    private static String space(StringBuilder builder){
        if (!Objects.equals(builder.lastIndexOf(" "), builder.length()-1)){
            return " ";
        }
        return "";
    }
}
