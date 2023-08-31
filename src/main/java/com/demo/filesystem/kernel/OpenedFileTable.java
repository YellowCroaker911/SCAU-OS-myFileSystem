package com.demo.filesystem.kernel;

public class OpenedFileTable {

    static class Pointer{
        private int indexOfBlock;
        private int offsetOfByte;
    }

    private String path;
    private String attribute;
    private int IndexOfStartBlock;
    private int length;
    private String opType;
    private Pointer readingPointer;
    private Pointer writingPointer;

}
