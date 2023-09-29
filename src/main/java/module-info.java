module demo {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires jdk.compiler;

    opens com.demo.filesystem to javafx.fxml;
    exports com.demo.filesystem;
}