package com.demo.myfilesystem.model;

import com.demo.myfilesystem.controller.MainViewController;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 中间依次排开的文件/文件夹(ThumbnailPane)，Menu是他的所属节点。
 * 在这个区域的大部分鼠标事件都应该在这写(Menu选项在OperationMenu写)
 */
public class FileFlowPane extends FlowPane {
    // TODO: 是不是限了个最多打开文件数量(当前没有限)
    private final ArrayList<ThumbnailPane> FileArray = new ArrayList<>();
    private final OperationMenu menu = new OperationMenu(this);
    private EntryTreeNode currentTreeNode = null;   // 当前打开的那个文件夹
    private final MainViewController mainController;
    public FileFlowPane(MainViewController mainController){this.mainController = mainController;}

    /**
     * 选择文件夹打开(在FlowPane重展示)
     * @param TreeNode 目录树的节点
     */
    public void openDirectory(EntryTreeNode TreeNode){
        currentTreeNode = TreeNode;
//        System.out.println("open Directory" + currentTreeNode.getFullName());
        ArrayList<EntryTreeNode> entries = TreeNode.getChildList();
        FileArray.clear();
        for(EntryTreeNode de : entries){
            FileArray.add(new ThumbnailPane(de));
        }
        this.getChildren().setAll(FileArray);
        this.setHgap(5);    // 文件控件的水平间距
        this.setVgap(5);    // 垂直间距
        this.setPadding(new Insets(10,10,10,10));   // 设置FlowPane的边距
        this.setOnMouseClicked(this::clickMouseHandler);    // 设置鼠标点击触发的函数
    }

    /**
     * 刷新，重新读取文件
     * TODO:可能还要加个往上调更新目录树
     */
    public void refresh(){
        mainController.refreshTree(currentTreeNode);
        openDirectory(currentTreeNode);
    }

    /**
     * 在FlowPane区域点击产生的事件
     * 左键单击，左键双击，
     */
    private void clickMouseHandler(MouseEvent e){
        Node clickNode = e.getPickResult().getIntersectedNode();
        // 点到文件图片或文字，视为点到文件所属框
        if(clickNode instanceof ImageView || clickNode instanceof Text){
            clickNode = clickNode.getParent();
            assert clickNode instanceof ThumbnailPane;
        }
        System.out.println("FileFlowPane.clickMouseHandler: clickNode="+clickNode); // debug 用
        if(e.getButton() != MouseButton.SECONDARY) menu.hide(); // 不是点右键，菜单隐藏 (可能会造成 bug 点不到菜单按钮)

        if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1) {
            menu.setThumbnail(null);
            if(clickNode instanceof ThumbnailPane thumbnail) {
                menu.setThumbnail(thumbnail);
            }
        }
        // 双击左键
        else if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
            menu.setThumbnail(null);
            if(clickNode instanceof ThumbnailPane thumbnail){   // （什么吊特性
                if(thumbnail.isDirectory()){    // 点到的文件夹
                    openDirectory(thumbnail.getDirectory());    // 打开文件夹
                }
                else{   // 双击文件
                    thumbnail.openFile(null);        // 双击打开 按只读打开
                }
            }
            else if(clickNode instanceof FileFlowPane){;} // 忽略
            else {
                System.err.println("未定义处理节点 Node="+clickNode);
            }
        }
        // 鼠标右键
        else if(e.getButton() == MouseButton.SECONDARY){
            if(clickNode instanceof ThumbnailPane thumbnail){   // （什么吊特性
                menu.setThumbnail(thumbnail);
                if(thumbnail.isDirectory()){    // 点到的文件夹
                    menu.switchMode(2);
                }
                else{   // 右键文件
                    menu.switchMode(3);
                }
            }
            else {  // 点到空白处
                menu.switchMode(1);
            }
            menu.show(this, e.getScreenX(), e.getScreenY());
        }
    }

    public EntryTreeNode getCurrentTreeNode(){return currentTreeNode;}
}
