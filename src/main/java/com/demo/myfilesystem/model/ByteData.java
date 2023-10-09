package com.demo.myfilesystem.model;

public class ByteData {
    private int index;
    private byte next;

    public ByteData(int index, byte next) {
        this.index = index;
        this.next = next;
    }

    public int getIndex() {
        return index;
    }

    public byte getNext() {
        return next;
    }
}

