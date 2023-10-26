module demo {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires jdk.compiler;
    requires org.controlsfx.controls;

    opens com.demo.myfilesystem to javafx.fxml;
    exports com.demo.myfilesystem;
    exports com.demo.myfilesystem.controller;
    exports com.demo.myfilesystem.model;
    opens com.demo.myfilesystem.controller to javafx.fxml;
}