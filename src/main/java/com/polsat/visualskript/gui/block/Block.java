package com.polsat.visualskript.gui.block;


public class Block {

    private String id;
    private BlockType type;
    private String name;
    private String pattern;
    private String since;
    private String description;
    private String example;

    public Block(String id, BlockType type, String name, String pattern, String since, String description, String example){
        this.id = id;
        this.type = type;
        this.name = name;
        this.pattern = pattern;
        this.since = since;
        this.description = description;
        this.example = example;
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

}
