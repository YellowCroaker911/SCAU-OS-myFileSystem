package com.demo.myfilesystem;

import com.demo.myfilesystem.kernel.entry.Entry;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import com.demo.myfilesystem.kernel.filetable.FileNode;
import com.demo.myfilesystem.kernel.io.Pointer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

import static com.demo.myfilesystem.kernel.manager.Manager.*;

/**
 * TODO:打开一个文件时新建FileWindowMain实例，新建一个窗口展示文件内容
 * 改成静态也可以
 * mode为"r"/"rw" 只读/读写
 */
public class FileWindowMain {
    public FileWindowMain(EntryTreeNode entry, String mode){
        AnchorPane anchorPane = new AnchorPane();
        TextArea textArea = new TextArea();
        Button save = new Button("保存");
        VBox vBox = new VBox();
        vBox.getChildren().addAll(save,textArea);
        anchorPane.getChildren().add(vBox);
        AnchorPane.setTopAnchor(vBox,50.0);
        AnchorPane.setLeftAnchor(vBox,100.0);
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(800);
        System.out.println(111);

        FileNode fileNode = openFile(entry,mode);
        System.out.println(111);

        String s = readFile(fileNode,fileNode.bytesLength());
        System.out.println(111);

        System.out.println(s);
        textArea.appendText(s);
        textArea.setEditable(true);
//        textArea.setWrapText(true);
        if(mode.equals("w")) {
            String[] str = {""};
            textArea.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                    str[0] = newValue;
                }
            });

            save.setOnAction(event-> {
                textArea.clear();
                textArea.appendText(str[0]);
                System.out.println(1111);
                writeFile(fileNode,str[0]);
            });
        }
        else {
            System.out.println(111);

            textArea.setEditable(false);
        }
        System.out.println(111);

        stage.show();
    }
}
