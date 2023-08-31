package com.demo.filesystem.kernel;

import java.util.Objects;

import static com.demo.filesystem.kernel.FileManager.disk;
import static com.demo.filesystem.kernel.IOTool.*;
import static com.demo.filesystem.utils.Constant.*;

public class DirectoryEntry {

    // 虚拟根目录项"roo  "830
    private byte[] bytes = {(byte) 0x72, (byte) 0x6F, (byte) 0x6F, (byte) 0x24, (byte) 0x24, (byte) 0x08, (byte) 0x03, (byte) 0x00};

    public DirectoryEntry() {
    }

    public DirectoryEntry(int indexOfStartBlock) {
        this.bytes[indexOfStartBlockOffset] = (byte) indexOfStartBlock;
    }

    public DirectoryEntry(byte[] bytes) {
        this.bytes = bytes;
    }

    public DirectoryEntry(int indexOfBlock, int indexOfEntry) {
        this.bytes = readDirectoryEntryByte(indexOfBlock, indexOfEntry);
    }

    public DirectoryEntry(String name, String typeName, byte attribute, int indexOfStartBlock, int length) {
        byte[] newBytes = new byte[bytesNumOfEntry];
        System.arraycopy(name.getBytes(), 0, newBytes, NameOffset, name.length());
        System.arraycopy(typeName.getBytes(), 0, newBytes, typeNameOffset, typeName.length());
        newBytes[attributeeOffset] = attribute;
        newBytes[indexOfStartBlockOffset] = (byte) indexOfStartBlock;
        newBytes[lengthOffset] = (byte) length;
        this.bytes = newBytes;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public String getName() {
        byte[] name = new byte[typeNameOffset - NameOffset];
        System.arraycopy(this.bytes, NameOffset, name, 0, name.length);
        return new String(name);
    }

    public String getTypeName() {
        byte[] typeName = new byte[attributeeOffset - typeNameOffset];
        System.arraycopy(this.bytes, typeNameOffset, typeName, 0, typeName.length);
        return new String(typeName);
    }

    public int getIndexOfStartBlock() {
        return (int) this.bytes[indexOfStartBlockOffset];
    }

    public boolean isDirectory() {
        String typeName = this.getTypeName();
        return Objects.equals(typeName, "  ");
    }

    public DirectoryEntry[] listDirectoryEntries() {
        assert this.isDirectory() : "File doesn't have sub-directory-entries";
        int indexOfStartBlock = this.bytes[indexOfStartBlockOffset];
        disk.IOBlock(indexOfStartBlock, "r");
        DirectoryEntry[] subDirectoryEntries = new DirectoryEntry[entriesNumOfBlock];
        for (int indexOfEntry = 0; indexOfEntry < entriesNumOfBlock; indexOfEntry++) {
            byte[] newBytes = new byte[bytesNumOfEntry];
            System.arraycopy(disk.readingBuffer, indexOfEntry * bytesNumOfEntry, newBytes, 0, newBytes.length);
            subDirectoryEntries[indexOfEntry] = new DirectoryEntry(newBytes);
        }
        return subDirectoryEntries;
    }


    public int locateDirectoryEntry(String name) {
        DirectoryEntry[] directoryEntries = this.listDirectoryEntries();
        for (int indexOfEntry = 0; indexOfEntry < entriesNumOfBlock; indexOfEntry++) {
            if (Objects.equals(name, directoryEntries[indexOfEntry].getName())) {
                return indexOfEntry;
            }
        }
        return -1;
    }

    public int getDirectoryLength() {
        DirectoryEntry[] directoryEntries = this.listDirectoryEntries();
        for (int indexOfEntry = 0; indexOfEntry < entriesNumOfBlock; indexOfEntry++) {
            String name = directoryEntries[indexOfEntry].getName();
            if (name.charAt(0) == '$') {
                return indexOfEntry;
            }
        }
        return entriesNumOfBlock;
    }

    public int addDirectoryEntry(DirectoryEntry newDirectoryEntry) {
        int length = this.getDirectoryLength();
        if (length == entriesNumOfBlock) {
            return -1;
        }
        writeDirectoryEntryByte(this.getIndexOfStartBlock(), length, newDirectoryEntry.getBytes());
        return 1;
    }


}
