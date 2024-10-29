package com.polsat.visualskript.system.script;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class StylizedLabel extends Label {
    public StylizedLabel(String text) {
        super(text);
        this.setFont(new Font("System", 24));
        this.setPadding(new Insets(5, 5, 5, 5));
    }
}
