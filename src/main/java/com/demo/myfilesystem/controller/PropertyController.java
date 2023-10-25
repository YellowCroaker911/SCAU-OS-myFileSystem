package com.demo.myfilesystem.controller;

import com.demo.myfilesystem.Main;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import com.demo.myfilesystem.kernel.filetable.FileNode;
import com.demo.myfilesystem.kernel.manager.Manager;
import com.demo.myfilesystem.utils.GenerateDialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import static com.demo.myfilesystem.kernel.manager.Manager.closeFile;
import static com.demo.myfilesystem.kernel.manager.Manager.openFile;
import static com.demo.myfilesystem.utils.Constant.*;

/**
 * 属性面板的controller
 * 支持修改文件是否只读
 */
public class PropertyController {

    @FXML
    private RadioButton CommentButton;

    @FXML
    private ImageView FileImage;

    @FXML
    private Text FileNameText;

    @FXML
    private Text OccupyInfo;

    @FXML
    private Text OccupyTag;

    @FXML
    private RadioButton ReadOnlyButton;

    @FXML
    private RadioButton ReadWriteButton;

    @FXML
    private Text SizeInfo;

    @FXML
    private Text SizeTag;

    @FXML
    private RadioButton SystemFilebutton;

    @FXML
    private BorderPane TypeInfoBorder;

    @FXML
    private Button applyButton;

    @FXML
    private Button closeButton;

    EntryTreeNode entry;
    boolean isReadOnly;
    @FXML
    void applySetting(ActionEvent event) {
        assert entry.getEntry().getInfo().isOnlyRead() != isReadOnly : "改了还一样";
        String attr = (isReadOnly?"1":"0") + entry.getEntry().getInfo().getAttribute().substring(1);
//        System.out.println(entry.getEntry().getInfo().getAttribute() + "   " +attr);
        Manager.alterAttribute(entry, attr);
        applyButton.setDisable(true);
        isReadOnly = !isReadOnly;
        GenerateDialog.AlertInformation("修改成功","", Alert.AlertType.INFORMATION, ButtonType.OK);
    }

    @FXML
    void closeWindow(ActionEvent event) {
        // zmn
        Stage stage =  (Stage)closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void setReadOnly(ActionEvent event) {
        if(ReadOnlyButton.isSelected()){
            ReadWriteButton.setSelected(false);
            isReadOnly = true;
            applyButton.setDisable(!applyButton.isDisable());
        }
        else ReadOnlyButton.setSelected(true);
    }

    @FXML
    void setReadWrite(ActionEvent event) {
        if(ReadWriteButton.isSelected()){
            ReadOnlyButton.setSelected(false);
            isReadOnly = false;
            applyButton.setDisable(!applyButton.isDisable());
        }
        else ReadWriteButton.setSelected(true);
    }

    /**
     * 根据文件信息更新窗口
     * @param entry 文件
     */
    public void initInfo(EntryTreeNode entry) {
        this.entry = entry;
        CommentButton.setSelected(entry.getEntry().getInfo().isCommon());
        SystemFilebutton.setSelected(entry.getEntry().getInfo().isSystem());
        // 设置只读/读写按钮
        if(entry.getEntry().getInfo().isOnlyRead()){
            ReadOnlyButton.setSelected(true);
            ReadWriteButton.setSelected(false);
        }
        else{
            ReadOnlyButton.setSelected(false);
            ReadWriteButton.setSelected(true);
        }
        if(entry.getEntry().getInfo().isDirectory()){
            ReadOnlyButton.setDisable(true);
            ReadWriteButton.setDisable(true);
        }
        // 设置图标
        if(entry.getEntry().getInfo().isDirectory()) {
            FileImage.setImage(DIRECTORY_ICON);
        }
        else{
            FileImage.setImage(FILE_ICON);
        }
        // 文件名
        FileNameText.setText("文件名称：  " + entry.getFullName().replace("$",""));
        // 文件实际大小
        SizeInfo.setText(""+entry.bytesLength()+"bytes");
        // 文件占磁盘大小(块数*块大小)
        OccupyInfo.setText(""+entry.getEntry().list().size()*BYTES_NUM_OF_BLOCK + "bytes");

        applyButton.setDisable(true);
        isReadOnly = entry.getEntry().getInfo().isOnlyRead();
    }
}

