package com.demo.filesystem.kernel;

import static com.demo.filesystem.kernel.FileManager.disk;
import static com.demo.filesystem.utils.Constant.bytesNumOfEntry;

public class IOTool {

    public static byte readByte(int indexOfBlock,int offsetOfByte){
        return ' ';
    }

    public static void writeByte(int indexOfBlock, int offsetOfByte, byte b) {
        disk.IOBlock(indexOfBlock, "r");
        disk.writingBuffer = disk.readingBuffer;
        disk.writingBuffer[offsetOfByte] = b;
        disk.IOBlock(indexOfBlock, "w");
    }

    public static byte[] readDirectoryEntryByte(int indexOfBlock, int indexOfEntry) {
        disk.IOBlock(indexOfBlock, "r");
        byte[] newBytes = new byte[bytesNumOfEntry];
        System.arraycopy(disk.readingBuffer, indexOfEntry * bytesNumOfEntry, newBytes, 0, newBytes.length);
        return newBytes;
    }

    public static void writeDirectoryEntryByte(int indexOfBlock, int indexOfEntry,byte[] newBytes) {
        disk.IOBlock(indexOfBlock, "r");
        disk.writingBuffer = disk.readingBuffer;
        System.arraycopy(newBytes, 0, disk.writingBuffer, indexOfEntry * bytesNumOfEntry, newBytes.length);
        disk.IOBlock(indexOfBlock,"w");
    }

    public static void writeFATByte(int indexOfBlock,int b){

    }

}
