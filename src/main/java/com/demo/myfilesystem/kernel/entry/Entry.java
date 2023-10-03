package com.demo.myfilesystem.kernel.entry;

import com.demo.myfilesystem.kernel.io.Pointer;

import java.util.ArrayList;

import static com.demo.myfilesystem.kernel.io.IOtool.*;
import static com.demo.myfilesystem.utils.Constant.*;

public class Entry {

    private EntryInfo info;
    private Pointer selfPointer;

    public Entry() {
        this.selfPointer = null;
        this.info = new EntryInfo();
    }

    public Entry(Pointer selfPointer) {
        this.selfPointer = selfPointer;
        this.info = new EntryInfo(loadEntryByte());
    }

    public Entry(EntryInfo info, Pointer selfPointer) {
        this.info = info;
        this.selfPointer = selfPointer;
    }

    public ArrayList<Integer> blocksIndex() {
        ArrayList<Integer> blocksIndexArray = new ArrayList<Integer>();
        for (int i = this.info.getStartBlockIndex(); i != -1; i = readFATByte(i)) {
            blocksIndexArray.add(i);
        }
        return blocksIndexArray;
    }

    public ArrayList<Entry> list() {
        ArrayList<Entry> entriesArray = new ArrayList<Entry>();
        for (int blockIndex : blocksIndex()) {
            for (int index = 0; index < ENTRIES_NUM_OF_BLOCK; index++) {
                Entry entry = new Entry(new Pointer(blockIndex, index, "entry"));
                entriesArray.add(entry);
            }
        }
        return entriesArray;
    }

    // 通过Pointer封装读写遍历
    public ArrayList<Entry> list2() throws CloneNotSupportedException {
        ArrayList<Entry> entriesArray = new ArrayList<Entry>();
        Pointer pointer = new Pointer(this.getInfo().getStartBlockIndex(), 0, "entry");
        while (true) {
            Entry entry = new Entry(pointer.clone());
            entriesArray.add(entry);
            if (!pointer.hasNext()) {
                break;
            }
            pointer.next();
        }
        return entriesArray;
    }

    public Entry newEntry(String fullName, String attribute, int freeBlockIndex) {
        EntryInfo info = new EntryInfo(fullName, attribute, freeBlockIndex);
        Pointer pointer = this.searchFreeEntry();
        Entry entry = new Entry(info, pointer);
        entry.updateEntryByte();
        entry.initBlock(entry.getInfo().getStartBlockIndex());
        return entry;
    }

    public void delEntry(String fullName) {
        ArrayList<Entry> entries = this.list();
        for (Entry entry : entries) {
            if (entry.getInfo().getFullName().equals(fullName)) {
                entry.getInfo().updateNull();
                entry.updateEntryByte();
            }
        }
    }

    /* 获取信息 */
    public Pointer searchFreeEntry() {
        byte[] buffer;
        for (int index : blocksIndex()) {
            buffer = readBlock(index);
            for (int i = 0; i < ENTRIES_NUM_OF_BLOCK * BYTES_NUM_OF_ENTRY; i += BYTES_NUM_OF_ENTRY) {
                if (buffer[i] == (byte) '$') {
                    return new Pointer(index, i / BYTES_NUM_OF_ENTRY, "entry");
                }
            }
        }
        return null;
    }

    // 通过Pointer封装读写遍历
    public Pointer searchFreeEntry2() throws CloneNotSupportedException {
        Pointer pointer = new Pointer(this.getInfo().getStartBlockIndex(), 0, "entry");
        while (true) {
            if (pointer.loadByte() == '$') {
                return pointer.clone();
            }
            if (!pointer.hasNext()) {
                break;
            }
            pointer.next();
        }
        return null;
    }

    public int endBlockIndex() {
        return this.blocksIndex().get(this.blocksIndex().size() - 1);
    }

    public EntryInfo getInfo() {
        return info;
    }

    public Pointer getSelfPointer() {
        return selfPointer;
    }

    /* IO */
    public byte[] loadEntryByte() {
        return this.selfPointer.loadEntry();
    }

    public void updateEntryByte() {
        this.selfPointer.putEntry(this.getInfo().getBytes());
    }

    public void linkBlock(int newBlockIndex) {
        writeFATByte(endBlockIndex(), newBlockIndex);
        writeFATByte(newBlockIndex, -1);
        this.getInfo().setLength(this.getInfo().getLength() + 1);
        this.getInfo().updateBytes();
        if (this.selfPointer == null) return; // selfPointer为null即为根目录，目录项信息无需写入硬盘
        this.updateEntryByte();
    }

    public void initBlock(int blockIndex) {
        if (this.getInfo().isDirectory()) {
            writeEmptyEntryBlock(blockIndex);
        } else {
            byte[] buffer = new byte[BYTES_NUM_OF_BLOCK];
            buffer[0] = '#';
            writeBlock(blockIndex, buffer);
        }
    }

    public void clearBlocks() {
        for (int index : blocksIndex()) {
            writeZeroBlock(index);
        }
    }

}
