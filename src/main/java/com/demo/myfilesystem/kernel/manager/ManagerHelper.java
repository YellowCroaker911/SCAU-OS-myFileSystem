package com.demo.myfilesystem.kernel.manager;

import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import com.demo.myfilesystem.kernel.filetable.FileNode;

import static com.demo.myfilesystem.kernel.io.IOtool.*;
import static com.demo.myfilesystem.utils.Constant.*;

public class ManagerHelper {

    public static int freeBlockIndex() {
        int blockIndex = 0;
        byte[] buffer = readBlock(blockIndex);
        for (int offset = BLOCKS_NUM_OF_FAT + 1; offset < BYTES_NUM_OF_BLOCK; offset++) {
            if (buffer[offset] == 0) {
                return offset;
            }
        }
        for (blockIndex = 1; blockIndex < BLOCKS_NUM_OF_FAT; blockIndex++) {
            buffer = readBlock(blockIndex);
            for (int offset = 0; offset < BYTES_NUM_OF_BLOCK; offset++) {
                if (buffer[offset] == 0) {
                    return offset + blockIndex * BYTES_NUM_OF_BLOCK;
                }
            }
        }
        return -1;
    }

    // todo：合并方法
    public static int openUpSpace(EntryTreeNode curNode) {
        int freeBlockIndex = freeBlockIndex();
        if (freeBlockIndex == -1) {
            return -1;
        }
        curNode.getEntry().linkBlock(freeBlockIndex);
        curNode.getEntry().initBlock(freeBlockIndex);
        return 1;
    }

    public static int openUpSpace(FileNode curNode) {
        int freeBlockIndex = freeBlockIndex();
        if (freeBlockIndex == -1) {
            return -1;
        }
        curNode.getEntry().linkBlock(freeBlockIndex);
        curNode.getEntry().initBlock(freeBlockIndex);
        return 1;
    }

    public static int freeBlockNum() {
        int cnt = 0;
        int blockIndex = 0;
        byte[] buffer = readBlock(blockIndex);
        for (int offset = BLOCKS_NUM_OF_FAT + 1; offset < BYTES_NUM_OF_BLOCK; offset++) {
            if (buffer[offset] == 0) {
                cnt++;
            }
        }
        for (blockIndex = 1; blockIndex < BLOCKS_NUM_OF_FAT; blockIndex++) {
            buffer = readBlock(blockIndex);
            for (int offset = 0; offset < BYTES_NUM_OF_BLOCK; offset++) {
                if (buffer[offset] == 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

}
