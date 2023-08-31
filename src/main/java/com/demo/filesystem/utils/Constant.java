package com.demo.filesystem.utils;

public class Constant {
    // Disk
    public static final int blocksNumOfDisk = 128;
    public static final int bytesNumOfBlock = 64;
    public static final int blocksNumOfFAT = blocksNumOfDisk / bytesNumOfBlock;
    // DirectoryEntry
    public static final int bytesNumOfEntry = 8;
    public static final int entriesNumOfBlock = bytesNumOfBlock / bytesNumOfEntry;
    public static final int NameOffset = 0;
    public static final int typeNameOffset = 3;
    public static final int attributeeOffset = 5;
    public static final int indexOfStartBlockOffset = 6;
    public static final int lengthOffset = 7;
    public static final int bitsNumOfByte = 8;
}
