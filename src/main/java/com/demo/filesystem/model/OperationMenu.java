package com.demo.filesystem.model;

import com.demo.filesystem.kernel.DirectoryEntry;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * 右键后展示的菜单
 * TODO: (!!!!急)如果在Menu对文件操作 可能导致结构有点混乱，好一点的做法就是交给FlowPane打开(怎么给会好点)
 * 原项目做法是在这里记录FlowPane 然后它的调方法
 */
public class OperationMenu extends ContextMenu {
    private final MenuItem openReadOnlyButton;      // TODO:根据点击是文件/文件夹 展示文字(只读打开/打开文件夹)
    private final MenuItem openReadWriteButton;
    private final MenuItem newFileButton;         // 新建文件
    private final MenuItem newDirectoryButton;    // 新建文件夹
    private final MenuItem deleteButton;      // 删除文件/空目录
    private final MenuItem propertyButton;    // 显示文件属性(使用新窗口展示)
    private DirectoryEntry entry = null;   // 打开菜单时点到的这个文件是谁
    public OperationMenu(){
        super();
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
        switch (mode){// 增加不可读性
            case 1 -> switchMode_state(0b001100);   // 空白处
            case 2 -> switchMode_state(0b110001);   // 文件夹
            case 3 -> switchMode_state(0b110011);   // 文件
            default -> System.err.println("OperationMenu.switchMode() unknown mode");
        }
    }

    private void switchMode_state(int state){
        for(MenuItem item : this.getItems()){
            item.setDisable((state&1)==1);
            state >>= 1;
        }
    }

    // TODO: 打开文件, 创建文件, 删除, 看属性

    private void openReadOnly(ActionEvent actionEvent){
        if(entry.isDirectory()){    // 打开文件夹

        }
        else{       // 打开文件

        }
    }

    private void openReadWrite(ActionEvent actionEvent) {
        // 要判断该文件是否可以读写打开
    }
    private void newFile(ActionEvent actionEvent) {
    }

    private void newDirectory(ActionEvent actionEvent) {
    }

    private void deleteFile(ActionEvent actionEvent) {
    }

    private void showProperty(ActionEvent actionEvent) {
    }


    public DirectoryEntry getEntry() {
        return entry;
    }

    public void setEntry(DirectoryEntry entry) {
        this.entry = entry;
    }
}
