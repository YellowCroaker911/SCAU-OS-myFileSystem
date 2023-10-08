package com.demo.myfilesystem.utils;

public class Constant {

    // disk
    public static final int BLOCKS_NUM_OF_DISK = 128;
    public static final int BYTES_NUM_OF_BLOCK = 64;
    public static final int BLOCKS_NUM_OF_FAT = BLOCKS_NUM_OF_DISK / BYTES_NUM_OF_BLOCK;

    // entry
    public static final int BYTES_NUM_OF_ENTRY = 8;
    public static final int BITS_NUM_OF_BYTE = 8;
    public static final int ENTRIES_NUM_OF_BLOCK = BYTES_NUM_OF_BLOCK / BYTES_NUM_OF_ENTRY;
    public static final int NAME_OFFSET = 0;
    public static final int TYPE_NAME_OFFSET = 3;
    public static final int ATTRIBUTE_OFFSET = 5;
    public static final int INDEX_OF_START_BLOCK_OFFSET = 6;
    public static final int LENGTH_OFFSET = 7;

    // attribute
    public static final int IS_ONLY_READ = 0;
    public static final int IS_SYSTEM = 1;
    public static final int IS_COMMON = 2;
    public static final int IS_DIRECTORY = 3;
    // fileTable
    public static final int OPENED_FILE_TABLE_SIZE = 5;

    // mark
    public static final byte PLACEHOLDER_BYTE = (byte) '$';
    public static final byte FILE_END_MARK_BYTE = (byte) '#';
    public static final String ROOT_NAME = "roo";
    public static final String DIRECTORY_TYPE_NAME = "  ";
    public static final String DIRECTORY_ATTRIBUTE = "00010000";
    public static final byte DIRECTORY_ATTRIBUTE_BYTE = (byte) 0x08;
    public static final String FULL_NAME_SPLIT_CHAR = ".";
    public static final String FULL_NAME_SPLIT_REGEX = "\\.";

    // 虚拟根目录项"roo  "830
    public static final byte[] ROOT_ENTRY_BYTES = new byte[]{
            (byte) ROOT_NAME.charAt(0), (byte) ROOT_NAME.charAt(1), (byte) ROOT_NAME.charAt(2),
            (byte) DIRECTORY_TYPE_NAME.charAt(0), (byte) DIRECTORY_TYPE_NAME.charAt(1),
            (byte) DIRECTORY_ATTRIBUTE_BYTE, (byte) BLOCKS_NUM_OF_FAT, (byte) 0x00
    };
    public static final byte[] NULL_ENTRY_BYTES = new byte[]{FILE_END_MARK_BYTE, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00};
    // mode
    public static final String BYTE = "byte";
    public static final String ENTRY = "entry";
    public static final String READ = "r";
    public static final String WRITE = "w";

}
