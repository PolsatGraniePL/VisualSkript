package com.polsat.visualskript.gui.manager.drop;

import com.polsat.visualskript.gui.manager.view.ViewBlock;

import java.util.ArrayList;
import java.util.List;

public class DropSystem {

    private static int offset = 1;

    private static final List<ViewBlock> nodeList = new ArrayList<>();

    public static ViewBlock getLatestNode() {
        return nodeList.isEmpty() ? null : nodeList.get(nodeList.size()-1);
    }

    public static void addNode(ViewBlock currentDropUnderNode) {
        DropSystem.nodeList.add(currentDropUnderNode);
    }

    public static void removeNode(ViewBlock currentDropUnderNode) {
        DropSystem.nodeList.remove(currentDropUnderNode);
    }

    public static void setOffset(int offset) {
        DropSystem.offset = offset;
    }

    public static int getOffset() {
        return offset;
    }
}
