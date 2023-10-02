package com.demo.myfilesystem.kernel.entry;

import static com.demo.myfilesystem.utils.Constant.*;

public class EntryInfo {
    private byte[] bytes;
    private String name;
    private String typeName;
    private String attribute;
    private int startBlockIndex;
    private int length;

    public EntryInfo() {
        // 虚拟根目录项"roo  "831
        this.bytes = new byte[]{(byte) 0x72, (byte) 0x6F, (byte) 0x6F, (byte) 0x24,
                (byte) 0x24, (byte) 0x08, (byte) 0x02, (byte) 0x01};
        this.name = "roo";
        this.typeName = "  ";
        this.attribute = "00010000";
        this.startBlockIndex = 2;
        this.length = 1;
    }

    public EntryInfo(byte[] bytes) {
        this.bytes = bytes;
        updateFromBytes();
    }

    public EntryInfo(String fullName, String attribute, int startBlockIndex) {
        if (attribute.equals("00010000")) {
            this.name = fullName;
            this.typeName = "  ";
        } else {
            String[] names = fullName.split("\\.");
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
        this.bytes = new byte[]{(byte) 0x24, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
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
        return this.name.charAt(0) == '$';
    }

    public String getFullName() {
        if (this.isDirectory()) {
            return this.name;
        } else {
            return this.name + "." + this.typeName;
        }
    }

}
