module com.polsat.visualskript {
    requires javafx.controls;
    requires javafx.fxml;

    requires json.simple;
    requires org.jsoup;
    requires org.controlsfx.controls;
    requires java.desktop;

    opens com.polsat.visualskript to javafx.fxml;
    opens com.polsat.visualskript.gui to javafx.fxml;
    exports com.polsat.visualskript;
    exports com.polsat.visualskript.gui;
}
