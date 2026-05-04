module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires jmh.core;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.data;
    opens org.example.data;
    requires jdk.unsupported;
    requires java.desktop;
    opens org.example.graph to jmh.core;
    opens org.example.controllers to javafx.fxml;

}
