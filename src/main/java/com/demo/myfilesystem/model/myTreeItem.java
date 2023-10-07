package com.demo.myfilesystem.model;

import com.demo.myfilesystem.Main;
import com.demo.myfilesystem.kernel.entry.Entry;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import com.demo.myfilesystem.test.DebugTool;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.Debug;

// TODO: 目录树有bug 是否更新孩子数组的判定有问题
public class myTreeItem extends TreeItem<String> {
    EntryTreeNode treeNode;
    private boolean isInitialized = false;                          // child是否已加载
//    private int getTime = 0;

    public myTreeItem(EntryTreeNode treeNode){
        super(treeNode.getFullName().replace("$", ""));  // 忽略$符号
        String path;
        // TODO:减少冗余代码，此处和Thumbnail获取图片冗余了
        if(treeNode.getFullName().equals("roo")){   // 根目录(不准确)
            path = Main.class.getResource("")+"icon/DiskManager.png";
        }
        else if(treeNode.getEntry().getInfo().isDirectory()){
            path = Main.class.getResource("")+"icon/direct.png";
        }
        else{
            path = Main.class.getResource("")+"icon/file.png";
        }
        this.setGraphic(new ImageView(new Image(path,20,20,true,true)));
//        System.out.println(treeNode.getFullName());
        this.treeNode = treeNode;
//        System.out.println(isDirectory());
    }

    @Override
    public ObservableList<TreeItem<String>> getChildren() {         // 展开时发生事件
        ObservableList<TreeItem<String>> children = super.getChildren();
//        System.out.println("getChildren");
//        DebugTool.printStackTrace("getChildren");
//        getTime++;  // 可能有 bug
//        if (getTime%3==1 && isExpanded()) {     // 每次展开会调用3次这个函数 在第一次做加载
        if (!isInitialized && isExpanded()) {
            isInitialized = true;
            children.clear();
            //            assert(treeNode.isDirectory());
            for (EntryTreeNode de : treeNode.getChildList())     // 根据子目录项生成child
                if (treeNode.getEntry().getInfo().isDirectory())
                    children.add(new myTreeItem(de));
        }
        return children;
    }

    // 判断是否可以点击展开
    @Override
    public boolean isLeaf() {
        return this.treeNode.isLeaf();
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
