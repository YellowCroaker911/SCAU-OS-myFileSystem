package com.demo.myfilesystem;

import com.demo.myfilesystem.controller.MainViewController;
import com.demo.myfilesystem.kernel.entry.Entry;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import com.demo.myfilesystem.kernel.filetable.FileNode;
import com.demo.myfilesystem.kernel.io.IOtool;
import com.demo.myfilesystem.kernel.io.Pointer;
import com.demo.myfilesystem.test.DebugTool;
import com.demo.myfilesystem.utils.GenerateDialog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import java.io.File;
import java.io.IOException;

import static com.demo.myfilesystem.kernel.manager.Manager.*;

/**
 * 打开一个文件时新建FileWindowMain实例，新建一个窗口展示文件内容
 * 改成静态也可以
 * mode为"r"/"rw" 只读/读写
 * 传stage用来绑定新窗口的爹
 * 传controller用来更新硬盘信息
 */
public class FileWindowMain {
    public FileWindowMain(EntryTreeNode entry, String mode, Stage ParentStage, MainViewController MainController) {
        BorderPane borderPane = new BorderPane();
        TextArea textArea = new TextArea();
        TextArea textArea1 = new TextArea();
        Text ReadText = new Text("文件内容");
        Text WriteText = new Text("写入预览");
        Button save = new Button("写入");
        VBox vBox = new VBox();
        vBox.getChildren().addAll(ReadText, textArea1, WriteText,textArea,save);
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox);
        borderPane.setPadding(new Insets(10,50,10,50));
//        AnchorPane.setTopAnchor(vBox,50.0);
//        AnchorPane.setLeftAnchor(vBox,100.0);

        FileNode fileNode;
        try {
            fileNode = openFile(entry, mode);
        }catch (Exception e){
            GenerateDialog.AlertInformation("打开文件失败", e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK);
            return;
        }

        Scene scene = new Scene(borderPane);
        Stage stage = new Stage();
        stage.setTitle(entry.getFullName().replace("$",""));
        stage.initOwner(ParentStage);   // 父窗口关子窗口跟着关
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(800);
        textArea.setEditable(true);
        textArea1.appendText(readFile(fileNode,fileNode.bytesLength()));
        textArea1.setEditable(false);

        textArea1.setWrapText(true);
        textArea.setWrapText(true);


        if(mode.equals("w")) {
            textArea.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                    if(newValue.contains("#")){
                        Notifications.create()
                                .text("不能输入井号'#'")
                                .owner(stage)
                                .position(Pos.TOP_CENTER)
                                .hideAfter(Duration.seconds(5))
                                .show();
                        textArea.setText(oldValue);
                        return;
                    }
                    textArea.setText(newValue);
                }
            });
            save.setOnAction(event-> {
                try {
                    var beg = System.currentTimeMillis();
                    writeFile(fileNode, textArea.getText());
                    var end = System.currentTimeMillis();
                    textArea1.appendText(textArea.getText());
                    MainController.refreshDiskInfo();
                    GenerateDialog.AlertInformation("保存成功", "consume time = " + (end-beg) + "ms", Alert.AlertType.INFORMATION, ButtonType.OK);
                }catch (Exception e){
                    MainController.refreshDiskInfo();
                    GenerateDialog.AlertInformation("写入错误", e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK);
                }
            });
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    closeFile(fileNode);
                    DebugTool.print(16);
                }
            });
        }
        else {
            save.setDisable(true);
            textArea.setEditable(false);
//            textArea.appendText(readFile(fileNode,fileNode.bytesLength()));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    closeFile(entry);
                }
            });
        }
        stage.show();
    }
}
