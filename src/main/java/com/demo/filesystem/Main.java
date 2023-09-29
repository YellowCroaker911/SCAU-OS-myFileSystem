package com.demo.filesystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 由于ROOT_FILE，仅适用于Windows(见GlobalValue)
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
//        System.out.println("qwe");
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("oriview.fxml"));
//        System.out.println("qwe");
//        Parent tmp = fxmlLoader.load();
//        Scene scene = new Scene(tmp, 1100, 700);
//        stage.setMinHeight(400);
//        stage.setMinWidth(700);
//        stage.setTitle("wwwwww");
//        System.out.println("qwe");
////        stage.getIcons().add(new Image(Main.class.getResource("icon.png").toExternalForm()));
//        stage.setScene(scene);
//        System.out.println("qwe");
//
////        MainViewController controller = fxmlLoader.getController();
////        controller.setStage(stage);
//        stage.show();
//        System.out.println("qwe");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
//        System.out.println(System.currentTimeMillis());
        launch();
    }
}

