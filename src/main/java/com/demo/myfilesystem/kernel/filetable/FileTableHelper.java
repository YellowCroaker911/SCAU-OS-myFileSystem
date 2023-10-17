package com.demo.myfilesystem.kernel.filetable;

import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;

import java.util.ArrayList;

import static com.demo.myfilesystem.utils.Constant.*;

public class FileTableHelper {

    private static ArrayList<FileNode> openedFileNodes = new ArrayList<>();

    public static void init() {
        openedFileNodes = new ArrayList<FileNode>();
    }

    public static FileNode open(EntryTreeNode tNode, String mode) {
        FileNode fNode = new FileNode(tNode.pathArray(), tNode.getEntry(), mode);
        // 只读文件不能写打开
        if (fNode.getEntry().getInfo().isOnlyRead() && mode.equals(WRITE)) {
            return null;
        }
        // 已打开文件表已满
        if (openedFileNodes.size() == OPENED_FILE_TABLE_SIZE) {
            return null;
        }
        // 文件已被打开
        if (openedFileNodes.contains(fNode)) {
            System.out.println(222);
            return null;
        }
        openedFileNodes.add(fNode);
        return fNode;
    }

    public static FileNode close(EntryTreeNode tNode) {
        for (FileNode fNode : openedFileNodes) {
            if (fNode.getPathArray().equals(tNode.pathArray()) && fNode.getFullName().equals(tNode.getFullName())) {
                openedFileNodes.remove(fNode);
                return fNode;
            }
        }
        return null;
    }
}
