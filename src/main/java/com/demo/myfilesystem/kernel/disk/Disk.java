package com.demo.myfilesystem.kernel.disk;

import com.demo.myfilesystem.utils.Constant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Disk {

    private RandomAccessFile simulatedFile;

    public byte[] readingBuffer = new byte[Constant.BYTES_NUM_OF_BLOCK];
    public byte[] writingBuffer = new byte[Constant.BYTES_NUM_OF_BLOCK];

    public Disk(String simulatedFilePath) {
        try {
            this.simulatedFile = new RandomAccessFile(simulatedFilePath, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void IOBlock(int indexOfBlock, String mode) {
        int base = indexOfBlock * Constant.BLOCKS_NUM_OF_DISK;
        for (int offset = 0; offset < Constant.BYTES_NUM_OF_BLOCK; offset++) {
            try {
                this.simulatedFile.seek(base + offset);
                switch (mode) {
                    case "r" -> this.readingBuffer[offset] = this.simulatedFile.readByte();
                    case "w" -> this.simulatedFile.writeByte(this.writingBuffer[offset]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
