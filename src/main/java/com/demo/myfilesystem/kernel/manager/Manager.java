package com.demo.myfilesystem.kernel.manager;

import com.demo.myfilesystem.kernel.entry.Entry;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeHelper;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;

import java.util.ArrayList;
import java.util.Stack;

import static com.demo.myfilesystem.kernel.entrytree.EntryTreeHelper.traversal;
import static com.demo.myfilesystem.kernel.io.IOtool.writeFATByte;
import static com.demo.myfilesystem.kernel.manager.ManagerHelper.openUpSpace;
import static com.demo.myfilesystem.kernel.manager.ManagerHelper.searchFreeBlock;
import static com.demo.myfilesystem.utils.Constant.ENTRIES_NUM_OF_BLOCK;

/**
 * 文件相关操作
 */
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

    public static EntryTreeNode getTopPath(){return pathStack.peek();}

    /**
     * 绝对路径创建文件
     * @param pathArray 要创建文件的父目录路径
     * @param fullName  文件名
     * @param attribute 文件属性
     * @return  创建是否成功
     */
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

    /**
     * 相对路径创建文件
     * @param fullName  文件名
     * @param attribute 文件属性
     * @param curNode   父目录节点
     * @return  是否成功
     */
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
        // 清空目标目录及其子目录所占用空间
        ArrayList<EntryTreeNode> nodeList = traversal(targetNode);
        for (EntryTreeNode node : nodeList) {
            node.getEntry().clearBlocks();
            for (int blockIndex : node.getEntry().blocksIndex()) {
                writeFATByte(blockIndex, 0);
            }
        }
        // 删除目标目录项
        curNode.getEntry().delEntry(fullName);
        // 更新目录树
        curNode.delNode(targetNode);
        return 1;
    }

    public static ArrayList<EntryTreeNode> listEntry(ArrayList<String> pathArray) {
        // 匹配路径，得到父目录
        EntryTreeNode curNode = EntryTreeHelper.match(pathArray);
        if (curNode == null) {
            return null;
        }
        // 调用无路径检查的listEntry方法
        return listEntry(curNode);
    }

    /**
     * 获得该节点下的所有儿子
     */
    public static ArrayList<EntryTreeNode> listEntry(EntryTreeNode curNode) {
        if (curNode == null) {
            curNode = pathStack.lastElement();
        }
        return curNode.getChildList();
    }


}
