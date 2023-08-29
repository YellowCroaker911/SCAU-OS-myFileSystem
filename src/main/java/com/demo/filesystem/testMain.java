package com.demo.filesystem;

import com.demo.filesystem.kernel.Disk;

public class testMain {

    public static void main(String[] args) {
        Disk disk = new Disk("src/main/resources/com/demo/filesystem/disk.txt");
        disk.format();
        disk.printDisk();
    }
}
