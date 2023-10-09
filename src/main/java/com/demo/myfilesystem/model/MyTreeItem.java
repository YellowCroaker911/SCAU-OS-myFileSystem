package com.demo.myfilesystem.model;

import com.demo.myfilesystem.Main;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

import static com.demo.myfilesystem.utils.Constant.*;

// TODO: 目录树有bug 是否更新孩子数组的判定有问题
public class MyTreeItem extends TreeItem<String> {
    EntryTreeNode treeNode;
    private boolean isInitialized = false;                          // child是否已加载
//    private int getTime = 0;

    public MyTreeItem(EntryTreeNode treeNode){
        super(treeNode.getFullName().replace("$", ""));  // 忽略$符号

        // TODO: 图片改为一次加载
        if(treeNode.getFullName().equals("roo")){   // 根目录(不准确)
            this.setGraphic(new ImageView(new Image(Main.class.getResource("")+"icon/DiskManager.png", 20, 20, true, true)));

        }
        else if(treeNode.getEntry().getInfo().isDirectory()){
            this.setGraphic(new ImageView(new Image(Main.class.getResource("")+"icon/direct.png", 20, 20, true, true)));
        }
        else{
            this.setGraphic(new ImageView(new Image(Main.class.getResource("")+"icon/file.png", 20, 20, true, true)));
        }
//        System.out.println(treeNode.getFullName());
        this.treeNode = treeNode;
//        System.out.println(isDirectory());
    }
    @Override
    public ObservableList<TreeItem<String>> getChildren() {         // 展开时发生事件
        ObservableList<TreeItem<String>> children = super.getChildren();
//        System.out.println("getChildren");
//        DebugTool.printStackTrace("getChildren");
        if (!isInitialized && isExpanded()) {
            isInitialized = true;
            updateChildren(children);
        }
        return children;
    }

    /**
     * 更新该节点的孩子列表
     * 每次只支持多个孩子添加单个孩子删除
     * @param children 孩子
     */
    private void updateChildren(ObservableList<TreeItem<String>> children){
        ArrayList<EntryTreeNode> newChildren = treeNode.getChildList();
        for(EntryTreeNode de : newChildren){    // 将新孩子加到目录树中
            boolean have = false;
            for(TreeItem<String> ch:children){
                if (((MyTreeItem) ch).getEntryTreeNode() == de) {
                    have = true;
                    break;
                }
            }
            if(!have){
                children.add(new MyTreeItem(de));
            }
        }
        for(TreeItem<String> ch:children){
            if(!newChildren.contains(((MyTreeItem)ch).getEntryTreeNode())){
                children.remove(ch);
                break;
            }
        }
    }


    // 判断是否可以点击展开
    @Override
    public boolean isLeaf() {
        return !this.treeNode.getEntry().getInfo().isDirectory();
    }

    public boolean isDirectory(){return this.treeNode.getEntry().getInfo().isDirectory();}

    public void addChildren(EntryTreeNode node){
        this.treeNode.addNode(node);
    }

    public EntryTreeNode getEntryTreeNode(){
        return this.treeNode;
    }

    // 需要重新加载
    public void resetInitialize(){isInitialized = false;}
}
