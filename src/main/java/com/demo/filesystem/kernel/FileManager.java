package com.demo.filesystem.kernel;

import static com.demo.filesystem.utils.Constant.*;

import java.io.IOException;

public class FileManager {

    // 简化文件管理器只对一个磁盘进行管理
    public static Disk disk = new Disk("src/main/resources/com/demo/filesystem/disk.txt");

    public static int searchFreeBlock() {
        disk.IOBlock(0, "r");
        for (int indexOfBlock = 0; indexOfBlock < blocksNumOfFAT; indexOfBlock++) {
            if ((indexOfBlock + 1) * bytesNumOfBlock < blocksNumOfFAT + 1) {
                continue;
            }
            disk.IOBlock(indexOfBlock, "r");
            for (int offset = 0; offset < bytesNumOfBlock; offset++) {
                if (offset + indexOfBlock * bytesNumOfBlock < blocksNumOfFAT + 1) {
                    offset = blocksNumOfFAT + 1 - indexOfBlock * bytesNumOfBlock;
                }
                // 下面正式从blocksNumOfFAT + 1处开始搜索
                if (disk.readingBuffer[offset] == 0) {
                    return offset + indexOfBlock * bytesNumOfBlock;
                }
            }
        }
        return -1;
    }

//    public static void deleteDirectoryEntry(int indexOfBlock, int indexOfEntry) {
//        disk.IOBlock(indexOfBlock, "r");
//        disk.writingBuffer = disk.readingBuffer;
//        if (indexOfEntry == entriesNumOfBlock - 1) {
//            disk.writingBuffer[indexOfEntry * bytesNumOfEntry] = (byte) 0x24;
//        } else {
//            for (int _indexOfEntry = indexOfEntry; _indexOfEntry < entriesNumOfBlock; _indexOfEntry++) {
//                for (int offset = 0; offset < bitsNumOfByte; offset++) {
//                    disk.writingBuffer[_indexOfEntry * bytesNumOfEntry + offset] =
//                            disk.writingBuffer[(_indexOfEntry + 1) * bytesNumOfEntry + offset];
//                }
//            }
//            disk.writingBuffer[(entriesNumOfBlock - 1) * bytesNumOfEntry] = (byte) 0x24;
//        }
//        disk.IOBlock(indexOfBlock, "w");
//    }


    public static int locateIndexOfStartBlock(String path) {
        String[] directoriesName = path.split("/");
        // 遍历根目录
        DirectoryEntry de = new DirectoryEntry();
        int indexOfBlock = de.getIndexOfStartBlock();
        // 遍历非根目录
        for (String dn : directoriesName) {
            int indexOfEntry = de.locateDirectoryEntry(dn);
            if (indexOfEntry == -1) {
                return -1;
            }
            de = new DirectoryEntry(indexOfBlock, indexOfEntry);
            if (!de.isDirectory()) {
                return -1;
            }
            indexOfBlock = de.getIndexOfStartBlock();
        }
        return indexOfBlock;
    }

    public static int createDirectory(String path, String name) {
        // 判断路径是否有误（是否存在创建目标目录的上级目录）
        int indexOfStartBlock = locateIndexOfStartBlock(path);
        if (indexOfStartBlock == -1) {
            return -1;
        }
        // 只关心当前目录是目标目录的上级目录所得到的虚拟目录项
        DirectoryEntry virtualDirectoryEntry = new DirectoryEntry(indexOfStartBlock);
        // 判断上级目录下是否有与目标目录名字相同的子目录
        if (virtualDirectoryEntry.locateDirectoryEntry(name) != -1) {
            return -1;
        }
        // 判断硬盘是否有未占用空间
        int freeBlock = searchFreeBlock();
        if (freeBlock == -1) {
            return -1;
        }
        // todo:更新FAT
        // 创建目标目录的目录项，并写入硬盘
        DirectoryEntry newDirectoryEntry = new DirectoryEntry(name, "  ", (byte) 0x08, freeBlock, 0);
        virtualDirectoryEntry.addDirectoryEntry(newDirectoryEntry);
        return 1;
    }

    /*
        if (name.length() == 0 || typeName.length() == 0) {
            return -1;
        }
        StringBuilder nameBuilder = new StringBuilder(name);
        nameBuilder.append("\0".repeat(Math.max(0, typeNameOffset - NameOffset - nameBuilder.length())));
        name = nameBuilder.toString();
        StringBuilder typeNameBuilder = new StringBuilder(typeName);
        nameBuilder.append("\0".repeat(Math.max(0, attributeeOffset - typeNameOffset - typeNameBuilder.length())));
        typeName = typeNameBuilder.toString();
    */
}
