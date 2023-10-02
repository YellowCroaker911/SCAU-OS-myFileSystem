package com.demo.filesystem.model;

import com.demo.filesystem.kernel.DirectoryEntry;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 右边依次排开的文件/文件夹，还有 Menu也是它下面
 * 在FlowPane范围内的鼠标操作都在这写
 */
public class FileFlowPane extends FlowPane {
    // TODO: 是不是限了个最多打开文件数量(当前没有限)
    private final ArrayList<ThumbnailPane> FileArray;
    private final OperationMenu menu = new OperationMenu();
    public FileFlowPane(){
        FileArray = new ArrayList<>();
    }
    public void openDirectory(DirectoryEntry directoryEntry){
        DirectoryEntry[] entries = directoryEntry.listDirectoryEntries();
        FileArray.clear();
        for(DirectoryEntry de : entries){
            FileArray.add(new ThumbnailPane(de));
        }
        this.getChildren().setAll(FileArray);

        this.setOnMouseClicked(this::clickMouseHandler);
    }

    /**
     * 在中间点击产生的时间
     */
    private void clickMouseHandler(MouseEvent e){
        Node clickNode = e.getPickResult().getIntersectedNode();
        // 点到文件图片或文字，视为点到文件所属框
        if(clickNode instanceof ImageView || clickNode instanceof Text){
            clickNode = clickNode.getParent();
            assert clickNode instanceof ThumbnailPane;
        }
        System.out.println("FileFlowPane.clickMouseHandler: clickNode="+clickNode);
        if(e.getButton() != MouseButton.SECONDARY) menu.hide(); // 不是点右键，菜单隐藏 (可能会造成 bug 点不到菜单按钮)
        // 双击左键
        if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
            if(clickNode instanceof ThumbnailPane thumbnail){   // （什么吊特性
                if(thumbnail.isDirectory()){    // 点到的文件夹
                    openDirectory(thumbnail.getDirectory());    // 打开文件夹
                }
                else{   // 双击文件
                    thumbnail.openFile("r");        // 双击打开 按只读打开
                }
            }
            else {
                System.out.println("unresolve Node="+clickNode);
            }
        }
        // 鼠标右键
        else if(e.getButton() == MouseButton.SECONDARY){
            if(clickNode instanceof ThumbnailPane thumbnail){   // （什么吊特性
                if(thumbnail.isDirectory()){    // 点到的文件夹
                    menu.switchMode(2);
                }
                else{   // 右键文件
                    menu.switchMode(3);
                }
                menu.setEntry(thumbnail.getDirectory());
            }
            else {  // 点到空白处
                menu.switchMode(1);
                menu.setEntry(null);
            }
            menu.show(this, e.getScreenX(), e.getScreenY());
        }
    }
}
