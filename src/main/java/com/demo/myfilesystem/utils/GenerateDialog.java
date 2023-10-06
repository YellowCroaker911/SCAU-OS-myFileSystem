package com.demo.myfilesystem.utils;


import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

/**
 * 都是方法
 * 产生相应功能的对话框
 */
public class GenerateDialog {
    /**
     * 新建文件的窗口，让用户输入文件名字
     * 取消新建文件返回的dialog的信息为空
     * @param mode 新建文件的类型(文件/文件夹) 0文件夹  1文件
     * @return 返回一个对话框对象
     */
    public static Dialog<String> NewFileDialog(int mode){
        Dialog<String> dialog = new Dialog<>();
        GridPane gridPane = new GridPane();
        TextField FileName = new RegexTextField("^[^.$]{0,3}$");
        TextField FileType = new RegexTextField("^[^.$]{0,2}$");
        if(mode == 0) {
            gridPane.add(new Label("文件夹名称: "), 0, 0);
            gridPane.add(FileName, 1, 0);
        } else if (mode == 1) {
            gridPane.add(new Label("文件名称: "), 0, 0);
            gridPane.add(FileName, 1, 0);
            gridPane.add(new Label("文件类型: "), 0, 1);
            gridPane.add(FileType, 1, 1);
        }
        else {
            System.err.println("GenerateDialog.NewFileDialog(): mode must be 0 or 1");
        }

        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(buttonType -> {
            if(buttonType == ButtonType.OK){
                StringBuilder name = new StringBuilder(FileName.getText());
                StringBuilder type = new StringBuilder(FileType.getText());
                while(name.length() < 3) name.append("$");  // 长度不够用$补齐
                while(type.length() < 2) type.append("$");
                if(mode == 0) return name.toString();
                else return name + "." + type;
            }
            // 没有点ok
            return null;
        });
        return dialog;
    }

    /**
     * 弹窗信息窗口，给用户反馈信息
     * @param HeaderInfo    信息头
     * @param Content       信息内容
     * @param alertType     弹窗类型 Alert.AlertType.
     * @param buttonType    弹窗提供的按钮 ButtonType.
     * @return  返回用户按了那个按钮 都没按就返回null
     */
    public static ButtonType AlertInformation(String HeaderInfo, String Content, Alert.AlertType alertType, ButtonType... buttonType){
        Alert alert = new Alert(alertType, "", buttonType);
        alert.setHeaderText(HeaderInfo);
        alert.setContentText(Content);
        Optional<ButtonType> optional = alert.showAndWait();
        return optional.orElse(null);
    }
}
