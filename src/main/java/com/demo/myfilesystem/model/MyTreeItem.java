package com.demo.myfilesystem.model;

import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class MyTreeItem extends TreeItem<String> {
    EntryTreeNode treeNode;
    private boolean isInitialized = false;                          // child是否已加载

    public MyTreeItem(EntryTreeNode treeNode){
        super(treeNode.getFullName());
        System.out.println(treeNode.getFullName());
        this.treeNode = treeNode;
        System.out.println(isDirectory());
    }
    @Override
    public ObservableList<TreeItem<String>> getChildren() {         // 展开时发生事件
        ObservableList<TreeItem<String>> children = super.getChildren();
        if (!isInitialized && isExpanded()) {                       // 未加载child
            System.out.println("getChildren");
            isInitialized = true;
//            assert(treeNode.isDirectory());
            for (EntryTreeNode de : treeNode.getChildList())     // 根据子目录项生成child
                if (treeNode.getEntry().getInfo().isDirectory())
                    children.add(new MyTreeItem(de));
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
}
