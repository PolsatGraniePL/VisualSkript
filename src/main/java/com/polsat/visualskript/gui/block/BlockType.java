package com.polsat.visualskript.gui.block;

import com.polsat.visualskript.gui.manager.view.ViewBlock;
import com.polsat.visualskript.gui.manager.view.blocks.*;
import com.polsat.visualskript.util.ErrorHandler;
import javafx.scene.Node;

public enum BlockType implements BlockTypePlacer{

    EVENT("Events", "1cb598", true, false, false){
        @Override
        public void place(Block block, String oldText, Node node){
            new Event(block).place(node);
        }
    },
    CONDITION("Conditions", "ff3031", false, true, true){
        @Override
        public void place(Block block, String oldText, Node node){
            new Conditions(block, oldText).place(node);
        }
    },
    SECTION("Sections", "b51c79", false, false, true){
        @Override
        public void place(Block block, String oldText, Node node){
            new Section(block).place(node);
        }
    },
    EFFECT("Effects", "8bff00", false, false, true){
        @Override
        public void place(Block block, String oldText, Node node){
            new Effect(block).place(node);
        }
    },
    EXPRESSION("Expressions", "ff9800", false, true, false){
        @Override
        public void place(Block block, String oldText, Node node){
            new Expression(block, oldText).place(node);
        }
    },
    TYPE("Types", "0064ff", false, true, false){
        @Override
        public void place(Block block, String oldText, Node node){
            new Type(block, oldText).place(node);
        }
    },
    TYPE_LIST("Types", "0064ff", false, true, false){
        @Override
        public void place(Block block, String oldText, Node node){
            new TypeList(block, oldText).place(node);
        }
    },
    STRUCTURE("Structures", "00dbff", true, true, false){
        @Override
        public void place(Block block, String oldText, Node node){
            new Structure(block, oldText).place(node);
        }
    },
    FUNCTION("Functions", "8000ff", false, true, true){
        @Override
        public void place(Block block, String oldText, Node node){
            new Function(block, oldText).place(node);
        }
    },
    COMMENT("Effects", "3d7100", false, false, true){
        @Override
        public void place(Block block, String oldText, Node node){
            new Comment(block).place(node);
        }
    },
    ERROR("ERROR", "000000", false, false, false){
        @Override
        public void place(Block block, String oldText, Node node){
            ErrorHandler.alert("Error block");
        }
    };


    private final String name;
    private final String color;
    private final Boolean placeOnBuildTab;
    private final Boolean placeOnExpr;
    private final Boolean placeOnVBox;

    BlockType(String name, String color, Boolean placeOnBuildTab, Boolean placeOnExpr, Boolean placeOnVBox){
        this.name = name;
        this.color = color;
        this.placeOnBuildTab = placeOnBuildTab;
        this.placeOnExpr = placeOnExpr;
        this.placeOnVBox = placeOnVBox;
    }

    public String getHexColor(){
        return this.color;
    }

    public String getName(){
        return this.name;
    }

    public Boolean getPlaceOnBuildTab(){
        return this.placeOnBuildTab;
    }

    public Boolean getPlaceOnExpr(){
        return this.placeOnExpr;
    }

    public Boolean getPlaceOnVBox(){
        return this.placeOnVBox;
    }
}
