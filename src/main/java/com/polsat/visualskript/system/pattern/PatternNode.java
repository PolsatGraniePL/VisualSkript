package com.polsat.visualskript.system.pattern;

public class PatternNode {
    public static final String EMPTY = "Empty";
    public static final String WS = "WS";
    public static final String LITERAL = "Literal";
    public static final String AND = "And";
    public static final String OPTIONAL = "Optional";
    public static final String GROUPED = "Grouped";
    public static final String OR = "Or";
    public static final String EXPRESSION = "Expression";
    public static final String REGEX = "Regex";

    public String type;
    public Object value;

    public PatternNode(String type, Object value) {
        this.type = type;
        this.value = value;
    }
}
