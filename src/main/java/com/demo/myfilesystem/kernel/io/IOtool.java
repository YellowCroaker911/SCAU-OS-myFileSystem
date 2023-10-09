package com.demo.myfilesystem.kernel.io;

import com.demo.myfilesystem.disk.Disk;

import java.util.Arrays;

import static com.demo.myfilesystem.utils.Constant.*;

public class IOtool {

    // 简化:只有一个磁盘的情况
    private static final Disk disk = new Disk("src/main/resources/com/demo/myfilesystem/disk.txt");

    public static byte[] readBlock(int blockIndex) {
        disk.IOBlock(blockIndex, "r");
        return disk.readingBuffer;
    }

    public static byte[] getFattable()
    {
        readBlock(0);
        byte[] fat= Arrays.copyOf(disk.readingBuffer,disk.readingBuffer.length*2);
        IOtool.readBlock(1);
        System.arraycopy(disk.readingBuffer,0,fat,disk.readingBuffer.length,disk.readingBuffer.length);
        return fat;
    }

    public static void writeBlock(int blockIndex, byte[] buffer) {
        disk.writingBuffer = buffer;
        disk.IOBlock(blockIndex, "w");
    }

    public static byte readByte(int blockIndex, int byteOffset) {
        disk.IOBlock(blockIndex, "r");
        return disk.readingBuffer[byteOffset];
    }

    public static void writeByte(int blockIndex, int byteOffset, byte b) {
        disk.IOBlock(blockIndex, "r");
        disk.writingBuffer = disk.readingBuffer;
        disk.writingBuffer[byteOffset] = b;
        disk.IOBlock(blockIndex, "w");
    }

    public static byte[] readEntryByte(int blockIndex, int entryIndex) {
        disk.IOBlock(blockIndex, "r");
        byte[] newBytes = new byte[BYTES_NUM_OF_ENTRY];
        System.arraycopy(disk.readingBuffer, entryIndex * BYTES_NUM_OF_ENTRY, newBytes, 0, newBytes.length);
        return newBytes;
    }

    public static void writeEntryByte(int blockIndex, int entryIndex, byte[] newBytes) {
        disk.IOBlock(blockIndex, "r");
        disk.writingBuffer = disk.readingBuffer;
        System.arraycopy(newBytes, 0, disk.writingBuffer, entryIndex * BYTES_NUM_OF_ENTRY, newBytes.length);
        disk.IOBlock(blockIndex, "w");
    }

    public static int readFATByte(int blockIndex) {
        int indexOfFATBlock = blockIndex / BYTES_NUM_OF_BLOCK;
        int offsetOfFATByte = blockIndex % BYTES_NUM_OF_BLOCK;
        return readByte(indexOfFATBlock, offsetOfFATByte);
    }

    public static void writeFATByte(int blockIndex, int b) {
        int indexOfFATBlock = blockIndex / BYTES_NUM_OF_BLOCK;
        int offsetOfFATByte = blockIndex % BYTES_NUM_OF_BLOCK;
        writeByte(indexOfFATBlock, offsetOfFATByte, (byte) b);
    }

    public static void writeZeroBlock(int blockIndex) {
        byte[] buffer = new byte[BYTES_NUM_OF_BLOCK];
        writeBlock(blockIndex, buffer);
    }

    public static void writeEmptyEntryBlock(int blockIndex) {
        byte[] buffer = new byte[BYTES_NUM_OF_BLOCK];
        for (int offset = 0; offset < BYTES_NUM_OF_BLOCK; offset += BYTES_NUM_OF_ENTRY) {
            buffer[offset] = '$';
        }
        writeBlock(blockIndex, buffer);
    }

    public static void format() {
        // 磁盘清0
        for (int blockIndex = 0; blockIndex < BLOCKS_NUM_OF_DISK; blockIndex++) {
            writeZeroBlock(blockIndex);
        }
        // 标记空目录项
        writeEmptyEntryBlock(BLOCKS_NUM_OF_FAT);
        // 初始化FAT
        byte[] buffer = new byte[BYTES_NUM_OF_BLOCK];
        for (int offset = 0; offset < BLOCKS_NUM_OF_FAT + 1; offset++) {
            buffer[offset] = (byte) -1;
        }
        writeBlock(0, buffer);
    }


}

