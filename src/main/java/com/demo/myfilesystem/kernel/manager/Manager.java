package com.demo.myfilesystem.kernel.manager;

import com.demo.myfilesystem.kernel.entry.*;
import com.demo.myfilesystem.kernel.entrytree.*;
import com.demo.myfilesystem.kernel.filetable.*;


import static com.demo.myfilesystem.kernel.entrytree.EntryTreeHelper.*;
import static com.demo.myfilesystem.kernel.filetable.FileTableHelper.*;
import static com.demo.myfilesystem.kernel.io.IOtool.*;
import static com.demo.myfilesystem.kernel.manager.ManagerHelper.*;
import static com.demo.myfilesystem.utils.Constant.*;

import java.util.ArrayList;
import java.util.Stack;

public class Manager {

    private static Stack<EntryTreeNode> pathStack;

    public static void init() {
        EntryTreeHelper.init();
        pathStack = new Stack<>();
        pathStack.add(EntryTreeHelper.getRoot());
    }

    public static void pushPath(EntryTreeNode node) {
        pathStack.push(node);
    }

    public static void popPath() {
        pathStack.pop();
    }

    public static int createEntry(ArrayList<String> pathArray, String fullName, String attribute) {
        // 匹配路径，得到父目录
        EntryTreeNode curNode = EntryTreeHelper.match(pathArray);
        if (curNode == null) {
            return -1;
        }
        // 调用无路径检查的createEntry方法
        if (createEntry(fullName, attribute, curNode) == -1) {
            return -1;
        }
        return 1;
    }

    public static int createEntry(String fullName, String attribute, EntryTreeNode curNode) {
        if (curNode == null) {
            curNode = pathStack.lastElement();
        }
        // 若无可用空间创建目录项则分配新的block
        if (curNode.childNum() / ENTRIES_NUM_OF_BLOCK >= curNode.getEntry().getInfo().getLength()) {
            if (openUpSpace(curNode) == -1) {
                return -1;
            }
        }
        // 搜索空闲block作为新目录空间
        int freeBlockIndex = searchFreeBlock();
        if (freeBlockIndex == -1) {
            return -1;
        }
        writeFATByte(freeBlockIndex, -1);
        // 创建新目录项
        Entry targetEntry = curNode.getEntry().newEntry(fullName, attribute, freeBlockIndex);
        // 更新目录树
        curNode.addNode(new EntryTreeNode(targetEntry));
        return 1;
    }

    public static int deleteEntry(ArrayList<String> pathArray, String fullName) {
        // 匹配路径，得到父目录
        EntryTreeNode curNode = EntryTreeHelper.match(pathArray);
        if (curNode == null) {
            return -1;
        }
        // 调用无路径检查的deleteEntry方法
        if (deleteEntry(fullName, curNode) == -1) {
            return -1;
        }
        return 1;
    }

    public static int deleteEntry(String fullName, EntryTreeNode curNode) {
        if (curNode == null) {
            curNode = pathStack.lastElement();
        }
        // 根据目录树得到待删除目录项的节点
        EntryTreeNode targetNode = curNode.match(fullName);
        if (targetNode == null) {
            return -1;
        }
        // 调用直接传入targetNode的deleteEntry方法
        if (deleteEntry(targetNode) == -1) {
            return -1;
        }
        return 1;
    }

    public static int deleteEntry(EntryTreeNode targetNode) {
        // 清空目标目录及其子目录所占用空间
        ArrayList<EntryTreeNode> nodeList = traversal(targetNode);
        for (EntryTreeNode node : nodeList) {
            node.getEntry().clearBlocks();
            for (int blockIndex : node.getEntry().blocksIndex()) {
                writeFATByte(blockIndex, 0);
            }
        }
        // 删除目标目录项
        targetNode.getParentNode().getEntry().delEntry(targetNode.getFullName());
        // 更新目录树
        targetNode.getParentNode().delNode(targetNode);
        return 1;
    }

    // 注意list方法的执行对象是当前目录
    public static ArrayList<EntryTreeNode> listEntry(ArrayList<String> pathArray) {
        // 匹配路径，得到父目录
        EntryTreeNode curNode = EntryTreeHelper.match(pathArray);
        if (curNode == null) {
            return null;
        }
        // 调用无路径检查的listEntry方法
        return listEntry(curNode);
    }

    public static ArrayList<EntryTreeNode> listEntry(EntryTreeNode curNode) {
        if (curNode == null) {
            curNode = pathStack.lastElement();
        }
        return curNode.getChildList();
    }

    public static FileNode openFile(ArrayList<String> pathArray, String fullName, String mode) {
        // 匹配路径，得到父目录
        EntryTreeNode curNode = EntryTreeHelper.match(pathArray);
        if (curNode == null) {
            return null;
        }
        // 调用无路径检查的openFile方法
        FileNode fNode = openFile(fullName, curNode, mode);
        if (fNode == null) {
            return null;
        }
        return fNode;
    }

    public static FileNode openFile(String fullName, EntryTreeNode curNode, String mode) {
        // 根据目录树得到待打开文件目录项的节点
        EntryTreeNode targetNode = curNode.match(fullName);
        if (targetNode == null) {
            return null;
        }
        // 调用直接传入targetNode的openFile方法
        FileNode fNode = openFile(targetNode, mode);
        if (fNode == null) {
            return null;
        }
        return fNode;
    }

    public static FileNode openFile(EntryTreeNode targetNode, String mode) {
        // 打开文件
        FileNode fNode = open(targetNode, mode);
        if (fNode == null) {
            return null;
        }
        return fNode;
    }

    public static FileNode closeFile(ArrayList<String> pathArray, String fullName) {
        // 匹配路径，得到父目录
        EntryTreeNode curNode = EntryTreeHelper.match(pathArray);
        if (curNode == null) {
            return null;
        }
        // 调用无路径检查的closeFile方法
        FileNode fNode = closeFile(fullName, curNode);
        if (fNode == null) {
            return null;
        }
        return fNode;
    }

    public static FileNode closeFile(String fullName, EntryTreeNode curNode) {
        // 根据目录树得到待关闭文件目录项的节点
        EntryTreeNode targetNode = curNode.match(fullName);
        if (targetNode == null) {
            return null;
        }
        // 调用直接传入targetNode的closeFile方法
        FileNode fNode = closeFile(targetNode);
        if (fNode == null) {
            return null;
        }
        return fNode;
    }

    public static FileNode closeFile(EntryTreeNode targetNode) {
        // 关闭文件
        FileNode fNode = closeFile(targetNode);
        if (fNode == null) {
            return null;
        }
        return fNode;
    }
}
