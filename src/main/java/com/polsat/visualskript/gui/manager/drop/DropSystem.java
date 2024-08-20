package com.polsat.visualskript.gui.manager.drop;

import com.polsat.visualskript.gui.manager.view.ViewBlock;

public class DropSystem {

    private static ViewBlock currentDropUnderNode;

    public static ViewBlock getCurrentDropUnderNode() {
        return currentDropUnderNode;
    }

    public static void setCurrentDropUnderNode(ViewBlock currentDropUnderNode) {
        DropSystem.currentDropUnderNode = currentDropUnderNode;
    }
}
