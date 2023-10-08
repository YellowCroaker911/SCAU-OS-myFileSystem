package com.demo.myfilesystem.kernel.entrytree;

import com.demo.myfilesystem.kernel.entry.Entry;

import java.util.ArrayList;

public class EntryTreeHelper {

    private static EntryTreeNode root;

    private static ArrayList<EntryTreeNode> tempNodeList;

    public static void init(){
        root = new EntryTreeNode(new Entry());
        loadTree(root);
        tempNodeList = new ArrayList<EntryTreeNode>();
    }

    public static EntryTreeNode getRoot() {
        return root;
    }

    public static ArrayList<EntryTreeNode> getTempNodeList() {
        return tempNodeList;
    }

    public static void loadTree(EntryTreeNode curNode) {
        for (Entry childEntry : curNode.getEntry().list()) {
            if (childEntry.getInfo().isNull()) {
                continue;
            }
            EntryTreeNode child = new EntryTreeNode(childEntry);
            curNode.getChildList().add(child);
            if (child.getEntry().getInfo().isDirectory()) {
                loadTree(child);
            }
        }
    }

    public static ArrayList<EntryTreeNode> traversal(EntryTreeNode curNode){
        tempNodeList.clear();
        tempNodeList.add(curNode);
        loadChild(curNode);
        return tempNodeList;
    }

    public static void loadChild(EntryTreeNode curNode) {
        for (EntryTreeNode child : curNode.getChildList()) {
            tempNodeList.add(child);
            if (child.getEntry().getInfo().isDirectory()) {
                loadChild(child);
            }
        }
    }

    public static EntryTreeNode match(ArrayList<String> pathArray) {
        EntryTreeNode curNode = root;
        EntryTreeNode nexNode;
        for (int i = 1; i < pathArray.size(); i++) {
            nexNode = curNode.match(pathArray.get(i));
            if (nexNode == null) {
                return null;
            }
            curNode = nexNode;
        }
        return curNode;
    }
}
