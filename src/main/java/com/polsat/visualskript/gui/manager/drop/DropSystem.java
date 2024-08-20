package com.polsat.visualskript.gui.manager.drop;

import com.polsat.visualskript.gui.manager.view.ViewBlock;

public class DropSystem {

    private static ViewBlock currentdropUnderNode;

    public static ViewBlock getCurrentdropUnderNode() {
        return currentdropUnderNode;
    }

    public static void setCurrentdropUnderNode(ViewBlock currentdropUnderNode) {
        DropSystem.currentdropUnderNode = currentdropUnderNode;
    }
}
