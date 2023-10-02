package com.demo.myfilesystem.model;

import com.demo.myfilesystem.kernel.entry.Entry;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class myTreeItem extends TreeItem<String> {
    EntryTreeNode treeNode;
    private boolean isInitialized = false;                          // child是否已加载

    public myTreeItem(EntryTreeNode treeNode){
        super(treeNode.getFullName());
//        System.out.println(directoryEntry.getName());
        this.treeNode = treeNode;
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
                    children.add(new myTreeItem(de));
        }
        return children;
    }

    // 判断是否可以点击展开
    @Override
    public boolean isLeaf() {
        return treeNode.isLeaf();
    }

    public boolean isDirectory(){return this.treeNode.getEntry().getInfo().isDirectory();}

    public void addChildren(EntryTreeNode node){
        this.treeNode.addNode(node);
    }

    public EntryTreeNode getEntryTreeNode(){
        return this.treeNode;
    }
}
