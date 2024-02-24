module com.polsat.visualskript {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.yaml.snakeyaml;
    requires java.logging;

    opens com.polsat.visualskript to javafx.fxml;
    exports com.polsat.visualskript;
    exports com.polsat.visualskript.gui;
    opens com.polsat.visualskript.gui to javafx.fxml;
    exports com.polsat.visualskript.gui.manager;
    opens com.polsat.visualskript.gui.manager to javafx.fxml;
    exports com.polsat.visualskript.gui.block;
    opens com.polsat.visualskript.gui.block to javafx.fxml;
}