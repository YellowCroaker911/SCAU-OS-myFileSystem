package com.demo.filesystem.kernel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Disk {

    private RandomAccessFile simulatedFile;

    private final int blocksNumOfDisk = 128;
    private final int bytesNumOfBlock = 64;
    private final int bitsNumOfByte = 8;
    private final int blocksNumOfFAT = blocksNumOfDisk/bytesNumOfBlock;

    public byte[] readingBuffer = new byte[bytesNumOfBlock];
    public byte[] writingBuffer = new byte[bytesNumOfBlock];

    public Disk(String simulatedFilePath) {
        try {
            this.simulatedFile = new RandomAccessFile(simulatedFilePath, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void IOBlock(int indexOfBlock, String mode) {
        int base = indexOfBlock * blocksNumOfDisk;
        for (int offset = 0; offset < bytesNumOfBlock; offset++) {
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


    public void IOByte(int indexOfBlock, int indexOfByte, String mode) {
        int base1 = indexOfBlock * blocksNumOfDisk;
        int base2 = indexOfByte * bytesNumOfBlock;
        for (int offset = 0; offset < bitsNumOfByte; offset++) {
            try {
                this.simulatedFile.seek(base1 + base2 + offset);
                switch (mode) {
                    case "r" -> this.readingBuffer[offset] = this.simulatedFile.readByte();
                    case "w" -> this.simulatedFile.writeByte(this.writingBuffer[offset]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void format() {
        for (int offset = 0; offset < bytesNumOfBlock; offset++) {
            this.writingBuffer[offset] = 0;
        }
        for (int indexOfBlock = 0; indexOfBlock < blocksNumOfDisk; indexOfBlock++) {
            this.IOBlock(indexOfBlock, "w");
        }
        for (int offset = 0; offset < bitsNumOfByte; offset++) {
            this.writingBuffer[offset] = (byte) 0xFF;   // -1补码
        }
        for (int indexOfByte = 0; indexOfByte < 3; indexOfByte++) {
            this.IOByte(0, indexOfByte, "w");
        }
    }

    public void free(int indexOfBlock){
        for (int offset = 0; offset < bytesNumOfBlock; offset++) {
            this.writingBuffer[offset] = 0;
        }
        this.IOBlock(indexOfBlock,"w");
        int indexOfFAT = indexOfBlock/bytesNumOfBlock;
        int indexOfByte = indexOfBlock/blocksNumOfFAT;
        this.IOByte(indexOfFAT,indexOfByte,"w");
    }

    public void printDisk() {
        System.out.println("按字节打印前8个块：");
        for (int indexOfBlock = 0; indexOfBlock < 8; indexOfBlock++) {
            this.IOBlock(indexOfBlock, "r");
            System.out.print("第" + indexOfBlock + "块：");
            for (int indexOfByte = 0; indexOfByte < bytesNumOfBlock; indexOfByte++) {
                System.out.printf("%4d", this.readingBuffer[indexOfByte]);
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------");
    }

}
