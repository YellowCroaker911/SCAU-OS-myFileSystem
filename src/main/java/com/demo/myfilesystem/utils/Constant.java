package com.demo.myfilesystem.utils;

public class Constant {
    // disk
    public static final int BLOCKS_NUM_OF_DISK = 128;
    public static final int BYTES_NUM_OF_BLOCK= 64;
    public static final int BLOCKS_NUM_OF_FAT = BLOCKS_NUM_OF_DISK / BYTES_NUM_OF_BLOCK;
    // entry
    public static final int BYTES_NUM_OF_ENTRY = 8;
    public static final int BITS_NUM_OF_BYTE = 8;
    public static final int ENTRIES_NUM_OF_BLOCK=BYTES_NUM_OF_BLOCK/BYTES_NUM_OF_ENTRY;
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
}
