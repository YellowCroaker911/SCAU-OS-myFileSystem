package com.demo.myfilesystem;

import com.demo.myfilesystem.controller.PropertyController;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import com.demo.myfilesystem.kernel.io.IOtool;
import com.demo.myfilesystem.kernel.manager.Manager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * 属性界面窗口主函数
 * 显示一个文件的属性
 */
public class PropertyMain {

    public PropertyMain(Stage ParentStage, EntryTreeNode entry) {
        FXMLLoader fxmlLoader = new FXMLLoader(com.demo.myfilesystem.PropertyMain.class.getResource("PropertyView.fxml"));
        Scene scene = null;
        try {
            scene= new Scene(fxmlLoader.load(), 400, 600);
        }catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.initOwner(ParentStage);   // 父窗口关子窗口跟着关
        stage.setMinHeight(400);
        stage.setMinWidth(200);
        stage.setTitle("属性");
//        stage.getIcons().add(new Image(Main.class.getResource("icon.png").toExternalForm()));
        stage.setScene(scene);
        PropertyController controller = fxmlLoader.getController();
        controller.initInfo(entry);
        stage.show();
    }


}
