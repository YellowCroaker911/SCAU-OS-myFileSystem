package com.demo.myfilesystem.kernel.io;

import static com.demo.myfilesystem.utils.Constant.BYTES_NUM_OF_ENTRY;

public class Pointer {
    private int blockIndex;
    private int entryIndex;
    private int byteOffset;

    public Pointer(int blockIndex, int arg2, String mode) {
        this.blockIndex = blockIndex;
        if (mode.equals("byte")) {
            this.byteOffset = arg2;
        } else if (mode.equals("entry")) {
            this.entryIndex = arg2;
            this.byteOffset = arg2 * BYTES_NUM_OF_ENTRY;
        }
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
}
