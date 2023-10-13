package com.demo.myfilesystem.controller;

import com.demo.myfilesystem.Main;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import static com.demo.myfilesystem.utils.Constant.BYTES_NUM_OF_BLOCK;

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

    @FXML
    void applySetting(ActionEvent event) {

    }

    @FXML
    void closeWindow(ActionEvent event) {

    }

    @FXML
    void setReadOnly(ActionEvent event) {

    }

    @FXML
    void setReadWrite(ActionEvent event) {

    }

    /**
     * 根据文件信息更新窗口
     * @param entry 文件
     */
    public void initInfo(EntryTreeNode entry){
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
        // 设置图标
        if(entry.getEntry().getInfo().isDirectory()) {
            FileImage.setImage(new Image(Main.class.getResource("")+"icon/direct.png", 30, 30, true, true));
        }
        else{
            FileImage.setImage(new Image(Main.class.getResource("")+"icon/file.png", 30, 30, true, true));
        }
        // 文件名
        FileNameText.setText(entry.getFullName().replace("$",""));
        // 文件实际大小
        SizeInfo.setText(""+entry.getEntry().getInfo().getLength()+"bytes");
        // 文件占磁盘大小(块数*块大小)
        OccupyInfo.setText(""+entry.getEntry().list().size()*BYTES_NUM_OF_BLOCK + "bytes");
    }
}

