package com.demo.myfilesystem.kernel.entry;

import com.demo.myfilesystem.kernel.io.Pointer;

import java.util.Arrays;

import static com.demo.myfilesystem.utils.Constant.*;

public class EntryInfo {
    private byte[] bytes;
    private String name;
    private String typeName;
    private String attribute;
    private int startBlockIndex;
    private int length;

    public EntryInfo() {
        this.bytes = ROOT_ENTRY_BYTES;
        this.name = ROOT_NAME;
        this.typeName = DIRECTORY_TYPE_NAME;
        this.attribute = DIRECTORY_ATTRIBUTE;
        this.startBlockIndex = BLOCKS_NUM_OF_FAT;
        this.length = 1;
    }

    public EntryInfo(byte[] bytes) {
        this.bytes = bytes;
        updateFromBytes();
    }

    public EntryInfo(String fullName, String attribute, int startBlockIndex) {
        if (attribute.equals(DIRECTORY_ATTRIBUTE)) {
            this.name = fullName;
            this.typeName = DIRECTORY_TYPE_NAME;
        } else {
            String[] names = fullName.split(FULL_NAME_SPLIT_REGEX);
            this.name = names[0];
            this.typeName = names[1];
        }
        this.attribute = attribute;
        this.startBlockIndex = startBlockIndex;
        this.length = 1;
        updateBytes();
    }

    public EntryInfo(String name, String typeName, String attribute, int startBlockIndex, int length) {
        this.name = name;
        this.typeName = typeName;
        this.attribute = attribute;
        this.startBlockIndex = startBlockIndex;
        this.length = length;
        this.updateBytes();
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setStartBlockIndex(int startBlockIndex) {
        this.startBlockIndex = startBlockIndex;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getAttribute() {
        return attribute;
    }

    public int getStartBlockIndex() {
        return startBlockIndex;
    }

    public int getLength() {
        return length;
    }

    public String updateName() {
        byte[] name = new byte[TYPE_NAME_OFFSET - NAME_OFFSET];
        System.arraycopy(this.getBytes(), NAME_OFFSET, name, 0, name.length);
        return new String(name);
    }

    public String updateTypeName() {
        byte[] typeName = new byte[ATTRIBUTE_OFFSET - TYPE_NAME_OFFSET];
        System.arraycopy(this.getBytes(), TYPE_NAME_OFFSET, typeName, 0, typeName.length);
        return new String(typeName);
    }

    public String updateAttribute() {
        return new StringBuffer(String.format("%8s", Integer.toBinaryString(this.getBytes()[ATTRIBUTE_OFFSET])).replace(' ', '0')).reverse().toString();
    }

    public int updateIndexOfStartBlock() {
        return this.getBytes()[INDEX_OF_START_BLOCK_OFFSET];
    }

    public int updateLength() {
        return this.getBytes()[LENGTH_OFFSET];
    }

    public void updateFromBytes() {
        this.setName(this.updateName());
        this.setTypeName(this.updateTypeName());
        this.setAttribute(this.updateAttribute());
        this.setStartBlockIndex(this.updateIndexOfStartBlock());
        this.setLength(updateLength());
    }

    public void updateBytes() {
        byte[] bytes = new byte[BYTES_NUM_OF_ENTRY];
        System.arraycopy(this.name.getBytes(), 0, bytes, NAME_OFFSET, this.name.length());
        System.arraycopy(this.typeName.getBytes(), 0, bytes, TYPE_NAME_OFFSET, this.typeName.length());
        bytes[ATTRIBUTE_OFFSET] = Byte.parseByte(new StringBuffer(this.attribute).reverse().toString(), 2);
        bytes[INDEX_OF_START_BLOCK_OFFSET] = (byte) this.startBlockIndex;
        bytes[LENGTH_OFFSET] = (byte) this.length;
        this.setBytes(bytes);
    }

    public void updateNull() {
        this.bytes = NULL_ENTRY_BYTES;
        this.updateFromBytes();
    }

    /* attribute */
    public boolean isOnlyRead() {
        return this.getAttribute().charAt(IS_ONLY_READ) == '1';
    }

    public boolean isSystem() {
        return this.getAttribute().charAt(IS_SYSTEM) == '1';
    }

    public boolean isCommon() {
        return this.getAttribute().charAt(IS_COMMON) == '1';
    }

    public boolean isDirectory() {
        return this.getAttribute().charAt(IS_DIRECTORY) == '1';
    }

    public boolean isNull() {
        return this.name.charAt(0) == PLACEHOLDER_BYTE;
    }

    public String getFullName() {
        if (this.isDirectory()) {
            return this.name;
        } else {
            return this.name + FULL_NAME_SPLIT_CHAR + this.typeName;
        }
    }

    public Pointer startBytePointer() {
        return new Pointer(this.startBlockIndex, 0, BYTE);
    }

    public Pointer startEntryPointer() {
        return new Pointer(this.startBlockIndex, 0, ENTRY);
    }

}
