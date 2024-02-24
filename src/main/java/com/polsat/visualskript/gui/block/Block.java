package com.polsat.visualskript.gui.block;


public class Block {

    private BlockType type;
    private String name;
    private String pattern;
    private String description;

    public Block(BlockType type, String name, String pattern, String description){
        this.type = type;
        this.name = name;
        this.pattern = pattern;
        this.description = description;
    }

    //GET
    public BlockType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPattern() {
        return pattern;
    }

    public String getDescription() {
        return description;
    }

    //SET
    public void setType(BlockType type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
