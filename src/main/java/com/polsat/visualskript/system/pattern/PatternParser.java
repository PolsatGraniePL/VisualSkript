package com.polsat.visualskript.system.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class PatternParser {
    private static int index;
    private static String input;

    public static PatternNode parse(String str) {
        index = 0;
        input = cleanPattern(str);
        return parseOr();
    }

    private static String cleanPattern(String pattern) {
        pattern = pattern.replace(">", "\uEF00");
        pattern = pattern.replace("<", "\uEF01");
        pattern = pattern.replace("\\%", "\uEF02");
        pattern = pattern.replace("%", "\uEF03");
        pattern = pattern.replace("\\(", "\uEF04");
        pattern = pattern.replace("\\)", "\uEF05");
        return pattern;
    }

    private static PatternNode parseOr() {
        ArrayList<PatternNode> patterns = new ArrayList<>();
        patterns.add(parseAnd());
        while (peek() != null && peek() == '|') {
            consume('|');
            patterns.add(parseAnd());
        }
        if (patterns.size() == 1) {
            return patterns.get(0);
        }
        return new PatternNode(PatternNode.OR, patterns);
    }

    private static PatternNode parseAnd() {
        ArrayList<PatternNode> patterns = new ArrayList<>();
        patterns.add(parseSingle());
        while (peek() != null && peek() != '|' && peek() != ')' && peek() != ']') {
            patterns.add(parseSingle());
        }
        if (patterns.size() == 1) {
            return patterns.get(0);
        }
        return new PatternNode(PatternNode.AND, patterns);
    }

    private static PatternNode parseSingle() {
        Character charAt = peek();
        if (charAt == null) {
            return new PatternNode(PatternNode.EMPTY, null);
        } else if (charAt == ' ') {
            consume(' ');
            return new PatternNode(PatternNode.WS, null);
        } else if (charAt == '(') {
            return parseGrouped();
        } else if (charAt == '[') {
            return parseOptional();
        } else if (charAt == '%') {
            return parseExpression();
        } else if (charAt == '<') {
            return parseRegex();
        } else {
            return parseLiteral();
        }
    }

    private static PatternNode parseGrouped() {
        consume('(');
        PatternNode pattern = parseOr();
        if (peek() != null && peek() == ')') {
            consume(')');
        } else {
            System.out.println(input);
            throw new RuntimeException("Expected closing parenthesis ')'");
        }
        return new PatternNode(PatternNode.GROUPED, pattern);
    }

    private static PatternNode parseOptional() {
        consume('[');
        PatternNode pattern = parseOr();
        if (peek() != null && peek() == ']') {
            consume(']');
        } else {
            throw new RuntimeException("Expected closing bracket ']'");
        }
        return new PatternNode(PatternNode.OPTIONAL, pattern);
    }

    private static PatternNode parseExpression() {
        consume('%');
        String expression = consumeWhile(c -> c != '%');
        if (peek() != null && peek() == '%') {
            consume('%');
        } else {
            throw new RuntimeException("Expected closing '%' for expression");
        }
        return new PatternNode(PatternNode.EXPRESSION, expression);
    }

    private static PatternNode parseRegex() {
        consume('<');
        PatternNode pattern = parseOr();
        if (peek() != null && peek() == '>') {
            consume('>');
        } else {
            throw new RuntimeException("Expected closing angle bracket '>'");
        }
        return new PatternNode(PatternNode.REGEX, pattern);
    }

    private static PatternNode parseLiteral() {
        String literal = consumeWhile(c -> !isSpecialCharacter(c));
        return new PatternNode(PatternNode.LITERAL, literal);
    }

    private static void consume(char expectedChar) {
        if (peek() != null && peek() == expectedChar) {
            index++;
        } else {
            throw new RuntimeException("Expected '" + expectedChar + "' but found '" + peek() + "'");
        }
    }

    private static String consumeWhile(Predicate<Character> predicate) {
        StringBuilder result = new StringBuilder();
        while (index < input.length() && predicate.test(input.charAt(index))) {
            result.append(input.charAt(index++));
        }
        return result.toString();
    }

    private static Character peek() {
        return index < input.length() ? input.charAt(index) : null;
    }

    private static boolean isSpecialCharacter(char c) {
        return c == ' ' || c == '(' || c == ')' || c == '[' || c == ']' || c == '|' || c == '%' || c == '<' || c == '>';
    }

    public static ArrayList<String> getCombinations(PatternNode pattern) {
        ArrayList<String> combinations;
        switch (pattern.type) {
            case PatternNode.EMPTY:
                combinations = new ArrayList<>(List.of(""));
                break;
            case PatternNode.WS:
                combinations = new ArrayList<>(List.of(" "));
                break;
            case PatternNode.LITERAL:
                combinations = new ArrayList<>(List.of((String) pattern.value));
                break;
            case PatternNode.AND:
                combinations = combineAnd((ArrayList<PatternNode>) pattern.value);
                break;
            case PatternNode.OPTIONAL:
                ArrayList<String> optional = new ArrayList<>();
                optional.add("");
                optional.addAll(getCombinations((PatternNode) pattern.value));
                combinations = optional;
                break;
            case PatternNode.GROUPED:
                combinations = getCombinations((PatternNode) pattern.value);
                break;
            case PatternNode.OR:
                ArrayList<String> orCombinations = new ArrayList<>();
                for (PatternNode subPattern : (ArrayList<PatternNode>) pattern.value) {
                    orCombinations.addAll(getCombinations(subPattern));
                }
                combinations = orCombinations;
                break;
            case PatternNode.EXPRESSION:
                combinations = new ArrayList<>(List.of("%" + pattern.value + "%"));
                break;
            case PatternNode.REGEX:
                combinations = getCombinations((PatternNode) pattern.value);
                break;
            default:
                throw new IllegalArgumentException("Unknown pattern type: " + pattern.type);
        }
        return combinations;
    }

    static ArrayList<String> cleanCombinations(ArrayList<String> combinations) {
        ArrayList<String> cleanedCombinations = new ArrayList<>();

        for (String combination : combinations) {
            cleanedCombinations.add(combination.trim());
        }

        for (int i = 0; i < cleanedCombinations.size(); i++) {
            cleanedCombinations.set(i, cleanedCombinations.get(i).replaceAll("\\s+", " "));
        }

        for (int i = 0; i < cleanedCombinations.size(); i++) {
            cleanedCombinations.set(i, cleanedCombinations.get(i)
                    .replace("\uEF00", ">")
                    .replace("\uEF01", "<")
                    .replace("\uEF02", "\\%")
                    .replace("\uEF03", "%")
                    .replace("\uEF04", "\\(")
                    .replace("\uEF05", "\\)")
            );
        }

        return cleanedCombinations;
    }

    private static ArrayList<String> combineAnd(ArrayList<PatternNode> patterns) {
        ArrayList<String> result = new ArrayList<>();
        if (patterns.isEmpty()) {
            result.add("");
            return result;
        }
        ArrayList<String> firstCombinations = getCombinations(patterns.get(0));
        ArrayList<String> restCombinations = combineAnd(new ArrayList<>(patterns.subList(1, patterns.size())));
        for (String first : firstCombinations) {
            for (String rest : restCombinations) {
                result.add(first + rest);
            }
        }
        return result;
    }
}
