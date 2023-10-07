package com.demo.myfilesystem.model;

import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import com.demo.myfilesystem.kernel.manager.Manager;
import com.demo.myfilesystem.utils.GenerateDialog;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;

/**
 * 右键后展示的菜单
 * 原项目做法是在这里记录FlowPane 然后它的调方法
 */
public class OperationMenu extends ContextMenu {
    private final MenuItem openReadOnlyButton;      // TODO:根据点击是文件/文件夹 展示文字(只读打开/打开文件夹)
    private final MenuItem openReadWriteButton;
    private final MenuItem newFileButton;         // 新建文件
    private final MenuItem newDirectoryButton;    // 新建文件夹
    private final MenuItem deleteButton;      // 删除文件/空目录
    private final MenuItem propertyButton;    // 显示文件属性(使用新窗口展示)
    private ThumbnailPane thumbnail = null;   // 打开菜单时点到的这个文件是谁
    private FileFlowPane flowPane = null;
    public OperationMenu(FileFlowPane parent){
        super();
        flowPane = parent;
        openReadOnlyButton = new MenuItem("打开");
        openReadWriteButton = new MenuItem("读写打开");
        newFileButton = new MenuItem("新建文件");
        newDirectoryButton = new MenuItem("新建文件夹");
        deleteButton = new MenuItem("删除文件");
        propertyButton = new MenuItem("属性");
        this.getItems().addAll( openReadOnlyButton, openReadWriteButton,
                                newFileButton, newDirectoryButton,
                                deleteButton, propertyButton);

//        openReadOnlyButton.setOnAction(open);
        openReadOnlyButton.setOnAction(this::openReadOnly);
        openReadWriteButton.setOnAction(this::openReadWrite);
        newFileButton.setOnAction(this::newFile);
        newDirectoryButton.setOnAction(this::newDirectory);
        deleteButton.setOnAction(this::deleteFile);
        propertyButton.setOnAction(this::showProperty);
    }




    /**
     * 根据点到了什么设置按钮是否能按
     * @param mode 1/2/3 空白处/文件夹/文件
     */
    public void switchMode(int mode){
        assert this.getItems().size() != 6: "number of buttons changed";
        switch (mode){// 增加不可读性 (x
            case 1 -> switchMode_state(0b001100);   // 空白处
            case 2 -> switchMode_state(0b111101);   // 文件夹
            case 3 -> switchMode_state(0b111111);   // 文件
            default -> System.err.println("OperationMenu.switchMode() unknown mode");
        }
    }

    private void switchMode_state(int state){
        for(MenuItem item : this.getItems()){
            item.setDisable((state&1)==0);
            state >>= 1;
        }
    }

     // 打开文件, 创建文件, 删除, 看属性
    private void openReadOnly(ActionEvent actionEvent){
        //TODO: 只读打开
        if(thumbnail.getDirectory().getEntry().getInfo().isDirectory()){    // 打开文件夹
            flowPane.openDirectory(thumbnail.getDirectory());
        }
        else{       // 打开文件
            thumbnail.openFile("r");
        }
    }

    private void openReadWrite(ActionEvent actionEvent) {
        // TODO: 读写打开 要判断该文件是否可以读写打开
        assert(!thumbnail.isDirectory());
        // 只读文件不可读写打开
        if(thumbnail.getDirectory().getEntry().getInfo().isOnlyRead()){
            GenerateDialog.AlertInformation(
                    "该文件为只读文件，不可读写打开!","",AlertType.ERROR,ButtonType.OK);
            return;
        }
        thumbnail.openFile("rw");
    }

    private void newFile(ActionEvent actionEvent) {
        //TODO: 测试新建文件
        Dialog<String> dialog = GenerateDialog.NewFileDialog(1);
        Optional<String> option = dialog.showAndWait();
        if(option.isPresent()){     // 确认新建
            String FullName = option.get();
            assert (FullName.length() == 6);
//            System.err.println(FullName);
            if(FullName.matches("(^\\$\\$\\$\\...$)|(^...\\.\\$\\$$)")){  // (文件名为空 or 扩展名为空)正则可能有问题
                GenerateDialog.AlertInformation(
                        "文件名和扩展名不能为空", "", AlertType.ERROR, ButtonType.OK);
            }
            else{
                // TODO:设置默认的文件属性
                int result = Manager.createEntry(FullName, "00000000", flowPane.getCurrentTreeNode());
                if(result != 1){   // 创建文件失败
                    GenerateDialog.AlertInformation(
                            "创建文件失败", "", AlertType.ERROR, ButtonType.OK);
                }
                else{   // 创建文件成功 刷新flowPane
                    flowPane.refresh();
                    GenerateDialog.AlertInformation(
                            "创建文件成功", "", AlertType.INFORMATION, ButtonType.OK);
                }
            }
//            System.out.println("新建文件OK" + option.get());
        }
    }

    private void newDirectory(ActionEvent actionEvent) {
        //TODO: 测试新建文件夹
        Dialog<String> dialog = GenerateDialog.NewFileDialog(0);
        Optional<String> option = dialog.showAndWait();
        if(option.isPresent()){     // 确认新建
            String FullName = option.get();
            assert(FullName.length() == 3);
            if(FullName.equals("$$$")){ // 文件夹名字为空
                GenerateDialog.AlertInformation(
                        "文件夹名称不能为空", "", AlertType.ERROR, ButtonType.OK);
            }
            else{
                int result = Manager.createEntry(FullName, "00010000", flowPane.getCurrentTreeNode());
                if(result != 1){   // 创建文件夹失败
                    GenerateDialog.AlertInformation(
                            "创建文件夹失败", "", AlertType.ERROR, ButtonType.OK);
                }
                else{   // 创建文件成功 刷新flowPane
                    flowPane.refresh();
                    GenerateDialog.AlertInformation(
                            "创建文件夹成功", "", AlertType.INFORMATION, ButtonType.OK);
                }
            }
//            System.out.println("新建文件夹OK" + option.get());
        }
    }

    private void deleteFile(ActionEvent actionEvent) {
        //TODO: 测试删除文件
        if(thumbnail == null){
            System.err.println("deleteFile() entry为空");
            return;
        }
        int result = Manager.deleteEntry(thumbnail.getDirectory().getFullName(), flowPane.getCurrentTreeNode());
        if(result != 1){
            GenerateDialog.AlertInformation("删除文件失败","", AlertType.ERROR, ButtonType.OK);
        }
        else{
            flowPane.refresh();
            GenerateDialog.AlertInformation("删除文件成功","",AlertType.INFORMATION, ButtonType.OK);
        }
    }

    private void showProperty(ActionEvent actionEvent) {

        //TODO: 展示文件属性
    }


    public ThumbnailPane getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ThumbnailPane thumbnail) {
        if(this.thumbnail != null) {
            this.thumbnail.unSelect();
        }
        this.thumbnail = thumbnail;
        if(this.thumbnail != null) {
            this.thumbnail.Select();
        }
    }

    public void setFlowPane(FileFlowPane flowPane){this.flowPane = flowPane;}
}
