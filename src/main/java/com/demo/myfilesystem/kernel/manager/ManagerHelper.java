package com.demo.myfilesystem.kernel.manager;

import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;

import static com.demo.myfilesystem.kernel.io.IOtool.*;
import static com.demo.myfilesystem.utils.Constant.*;

public class ManagerHelper {

    public static int searchFreeBlock() {
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

    public static int openUpSpace(EntryTreeNode curNode) {
        int freeBlockIndex = searchFreeBlock();
        if (freeBlockIndex == -1) {
            return -1;
        }
        curNode.getEntry().linkBlock(freeBlockIndex);
        curNode.getEntry().initBlock(freeBlockIndex);
        return 1;
    }

}
