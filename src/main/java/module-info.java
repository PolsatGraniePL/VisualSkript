module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.yaml.snakeyaml;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}