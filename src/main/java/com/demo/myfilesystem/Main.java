package com.demo.myfilesystem;

import com.demo.myfilesystem.kernel.io.IOtool;
import com.demo.myfilesystem.kernel.manager.Manager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static com.demo.myfilesystem.utils.Constant.*;

/**
 * 由于ROOT_FILE，仅适用于Windows(见GlobalValue)
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 700);
        stage.setMinHeight(400);
        stage.setMinWidth(700);
        stage.setTitle("wwwwww");
//        stage.getIcons().add(new Image(Main.class.getResource("icon.png").toExternalForm()));
        stage.setScene(scene);
//        MainViewController controller = fxmlLoader.getController();
//        controller.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        IOtool.format();
        Manager.init();
        ArrayList<String> pathArray = new ArrayList<>();
        System.out.println(DISK_ICON_SMALL);
        System.out.println(DIRECTORY_ICON);
        System.out.println(FILE_ICON);
        System.out.println(FILE_ICON_SMALL);
        launch();
    }
}

