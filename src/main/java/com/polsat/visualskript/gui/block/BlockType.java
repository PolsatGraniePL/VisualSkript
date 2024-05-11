package com.polsat.visualskript.gui.block;

public enum BlockType {

    EVENT("Events", "1cb598"),
    CONDITION("Conditions", "b51c1c"),
    SECTION("Sections", "b51c79"),
    EFFECT("Effects", "8bff00"),
    EXPRESSION("Expressions", "ff9800"),
    TYPE("Types", "0064ff"),
    STRUCTURE("Structures", "00dbff"),
    FUNCTION("Functions", "8000ff"),
    ERROR("ERROR", "000000");


    private final String name;
    private final String color;

    BlockType(String name, String color){
        this.name = name;
        this.color = color;
    }

    public String getHexColor(){
        return this.color;
    }

    public String getName(){
        return this.name;
    }
}
