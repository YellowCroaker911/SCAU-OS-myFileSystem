package com.demo.myfilesystem.kernel.io;

import static com.demo.myfilesystem.kernel.io.IOtool.*;
import static com.demo.myfilesystem.utils.Constant.*;

// 封装遍历读写操作
public class Pointer implements Cloneable {
    private int blockIndex;
    private int entryIndex;
    private int byteOffset;
    private String mode;

    public Pointer(int blockIndex, int arg2, String mode) {
        this.blockIndex = blockIndex;
        if (mode.equals("byte")) {
            this.byteOffset = arg2;
        } else if (mode.equals("entry")) {
            this.entryIndex = arg2;
            this.byteOffset = arg2 * BYTES_NUM_OF_ENTRY;
        }
        this.mode = mode;
    }

    public boolean hasNext() {
        if (this.mode.equals("entry")) {
            if (this.entryIndex < ENTRIES_NUM_OF_BLOCK - 1) {
                return true;
            }
        } else if (this.mode.equals("byte")) {
            if (this.byteOffset < BYTES_NUM_OF_BLOCK - 1) {
                return true;
            }
        }
        int nextBlock = readFATByte(this.blockIndex);
        return nextBlock != -1;
    }

    public void next() {
        if (this.mode.equals("entry")) {
            if (this.entryIndex < ENTRIES_NUM_OF_BLOCK - 1) {
                this.entryIndex++;
                this.byteOffset += BYTES_NUM_OF_ENTRY;
                return;
            }
        } else if (this.mode.equals("byte")) {
            if (this.byteOffset < BYTES_NUM_OF_BLOCK - 1) {
                this.byteOffset++;
                return;
            }
        }
        this.blockIndex = readFATByte(this.blockIndex);
        this.entryIndex = 0;
        this.byteOffset = 0;
    }

    public void setBlockIndex(int blockIndex) {
        this.blockIndex = blockIndex;
    }

    public void setEntryIndex(int entryIndex) {
        this.entryIndex = entryIndex;
    }

    public void setByteOffset(int byteOffset) {
        this.byteOffset = byteOffset;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public int getEntryIndex() {
        return entryIndex;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public void putByte(byte b) {
        writeByte(blockIndex, byteOffset, b);
    }

    public byte loadByte() {
        return readByte(blockIndex, byteOffset);
    }

    public void putEntry(byte[] bs) {
        writeEntryByte(blockIndex, entryIndex, bs);
    }

    public byte[] loadEntry() {
        return readEntryByte(blockIndex, entryIndex);
    }

    @Override
    public Pointer clone() {
        try {
            return (Pointer) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
