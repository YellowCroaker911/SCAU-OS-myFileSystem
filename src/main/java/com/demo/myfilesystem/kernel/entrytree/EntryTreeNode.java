package com.demo.myfilesystem.kernel.entrytree;

import com.demo.myfilesystem.kernel.entry.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件在树型结构下的节点
 * 目录树记录该节点
 */
public class EntryTreeNode {

    protected String fullName;
    protected Entry entry;  // 存文件相关信息
    protected EntryTreeNode parentNode;
    protected ArrayList<EntryTreeNode> childList;

    public EntryTreeNode(Entry file) {
        this.fullName = file.getInfo().getFullName();
        this.entry = file;
        childList = new ArrayList<>();
    }

    public boolean isLeaf() {
        return childList.isEmpty();
    }

    public void addNode(EntryTreeNode node) {
        node.setParentNode(this);
        this.childList.add(node);
    }

    public void delNode(EntryTreeNode node) {
        this.childList.remove(node);
    }

    public EntryTreeNode match(String fullName) {
        for (EntryTreeNode child : childList) {
            if (child.getFullName().equals(fullName)) {
                return child;
            }
        }
        return null;
    }

    public String getFullName() {
        return fullName;
    }

    public Entry getEntry() {
        return entry;
    }

    public ArrayList<EntryTreeNode> getChildList() {
        return childList;
    }

    public void setParentNode(EntryTreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public int childNum() {
        return this.childList.size();
    }

}

