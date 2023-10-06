package com.demo.myfilesystem.model;

import com.demo.myfilesystem.kernel.entry.Entry;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import com.demo.myfilesystem.test.DebugTool;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.Debug;

public class myTreeItem extends TreeItem<String> {
    EntryTreeNode treeNode;
    private boolean isInitialized = false;                          // child是否已加载
    private int getTime = 0;

    public myTreeItem(EntryTreeNode treeNode){
        super(treeNode.getFullName());
//        System.out.println(treeNode.getFullName());
        this.treeNode = treeNode;
//        System.out.println(isDirectory());
    }

    @Override
    public ObservableList<TreeItem<String>> getChildren() {         // 展开时发生事件
        ObservableList<TreeItem<String>> children = super.getChildren();
//        System.out.println("getChildren");
//        DebugTool.printStackTrace("getChildren");
        getTime++;  // 可能有 bug
        if (getTime%3==1 && isExpanded()) {     // 每次展开会调用3次这个函数 在第一次做加载
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
