package com.demo.myfilesystem.kernel.manager;

import com.demo.myfilesystem.kernel.entry.*;
import com.demo.myfilesystem.kernel.entrytree.*;
import com.demo.myfilesystem.kernel.filetable.*;
import javafx.util.Pair;


import static com.demo.myfilesystem.kernel.entrytree.EntryTreeHelper.*;
import static com.demo.myfilesystem.kernel.filetable.FileTableHelper.*;
import static com.demo.myfilesystem.kernel.io.IOtool.*;
import static com.demo.myfilesystem.kernel.manager.ManagerHelper.*;
import static com.demo.myfilesystem.utils.Constant.*;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import static com.demo.myfilesystem.kernel.entrytree.EntryTreeHelper.traversal;
import static com.demo.myfilesystem.kernel.io.IOtool.writeFATByte;
import static com.demo.myfilesystem.kernel.manager.ManagerHelper.openUpSpace;
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

    public static Stack<EntryTreeNode> getPathStack() {
        return pathStack;
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
    public static int createEntry(ArrayList<String> pathArray, String fullName, String attribute) throws IOException {
        // 匹配路径，得到父目录
        EntryTreeNode curNode = EntryTreeHelper.match(pathArray);
        if (curNode == null) {
            throw new IOException("父目录不存在");
        }
        // 调用无路径检查的createEntry方法
//        if (createEntry(fullName, attribute, curNode) != 1) {
//            return -1;
//        }
        createEntry(fullName,attribute,curNode);
        return 1;
    }

    /**
     * 相对路径创建文件/文件夹
     * @param fullName  文件名.扩展名  文件名长度为3 创建文件夹只穿文件名
     * @param attribute 文件属性        创文件夹时传 "00010000"
     * @param curNode   父目录节点
     * @return  是否成功
     * xxx.xx       文件
     * xxx          文件夹
     */
    public static int createEntry(String fullName, String attribute, EntryTreeNode curNode)throws IOException {
        if (curNode == null) {
            curNode = pathStack.lastElement();
        }
        // 判断当前目录是否有同名目录项
        if (curNode.match(fullName) != null) {
            throw new IOException("文件名已存在");
        }
        // 若无可用空间创建目录项则分配新的block
        if (curNode.childNum() / ENTRIES_NUM_OF_BLOCK >= curNode.getEntry().getInfo().getLength()) {
            if (openUpSpace(curNode) == -1) {
                throw new IOException("没有空闲空间");
            }
        }
        // 搜索空闲block作为新目录空间
        int freeBlockIndex = freeBlockIndex();
        if (freeBlockIndex == -1) {
            throw new IOException("没有空闲块");
        }
        writeFATByte(freeBlockIndex, -1);
        // 创建新目录项
        Entry targetEntry = curNode.getEntry().newEntry(fullName, attribute, freeBlockIndex);
        // 更新目录树
        curNode.addNode(new EntryTreeNode(targetEntry));
        return 1;
    }

    public static int deleteEntry(ArrayList<String> pathArray, String fullName)throws IOException {
        // 匹配路径，得到父目录
        EntryTreeNode curNode = EntryTreeHelper.match(pathArray);
        if (curNode == null) {
            throw new IOException("父节点不存在");
        }
        // 调用无路径检查的deleteEntry方法
        if (deleteEntry(fullName, curNode) == -1) {
            throw new IOException("文件节点不存在");
        }
        return 1;
    }

    /**
     * 删除文件
     * @param fullName  文件全名
     * @param curNode   要删除文件所在的文件夹
     */
    public static int deleteEntry(String fullName, EntryTreeNode curNode)throws IOException {
        if (curNode == null) {
            curNode = pathStack.lastElement();
        }
        // 根据目录树得到待删除目录项的节点
        EntryTreeNode targetNode = curNode.match(fullName);
        if (targetNode == null) {
            throw new IOException("文件节点不存在");
        }
        // 调用直接传入targetNode的deleteEntry方法
//        if (deleteEntry(targetNode) == -1) {
//
//        }
        deleteEntry(targetNode);
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
    public static ArrayList<EntryTreeNode> listEntry(ArrayList<String> pathArray)throws IOException {
        // 匹配路径，得到父目录
        EntryTreeNode curNode = EntryTreeHelper.match(pathArray);
        if (curNode == null) {
//            return null;
            throw new IOException("父目录不存在");
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

    public static FileNode openFile(ArrayList<String> pathArray, String fullName, String mode) throws IOException {
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

    public static FileNode openFile(String fullName, EntryTreeNode curNode, String mode) throws IOException {
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

    public static FileNode openFile(EntryTreeNode targetNode, String mode) throws IOException {
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
        FileNode fNode = close(targetNode);
        if (fNode == null) {
            return null;
        }
        return fNode;
    }

    public static int writeFile(FileNode targetFNode, String str) {
        // 检查写入长度是否超出硬盘空间
        int requiredFreeSpaceNum = targetFNode.requiredFreeSpaceNum(str);
        if (requiredFreeSpaceNum > freeBlockNum()) {
            return -1;
        }
        // 开辟所需空间
        for (int i = 0; i < requiredFreeSpaceNum; i++) {
            openUpSpace(targetFNode);
        }
        // 写文件
        targetFNode.write(str);
        return 1;
    }

    public static String readFile(FileNode targetFNode, Integer len) {
        if (len == null) {
            len = targetFNode.bytesLength();
        }
        if (len > targetFNode.bytesLength()) {
            return null;
        }
        // 读文件
        return targetFNode.read(len);
    }

    public static ArrayList<Pair<ArrayList<Integer>,Entry>> listFATBlockLink() {
        /*
        ArrayList<Integer>:同一目录项所占用空间的块下标
        boolean:0为文件1为目录
        */
        ArrayList<Pair<ArrayList<Integer>,Entry>>  blockLinks = new ArrayList<Pair<ArrayList<Integer>,Entry>>();
        ArrayList<EntryTreeNode> treeNodes = traversal(EntryTreeHelper.getRoot());
        for (EntryTreeNode node : treeNodes) {
            blockLinks.add(new Pair(node.getEntry().blocksIndex(),node.getEntry()));
        }
        return blockLinks;
    }

}
