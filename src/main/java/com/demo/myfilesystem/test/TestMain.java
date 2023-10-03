package com.demo.myfilesystem.test;

import com.demo.myfilesystem.kernel.io.IOtool;
import com.demo.myfilesystem.kernel.manager.Manager;

import java.util.ArrayList;

public class TestMain {
    public static void main(String[] args) {
        ArrayList<String> pathArray = new ArrayList<>();
        IOtool.format();
        Manager.init();
//        DebugTool.print();

        pathArray.add("roo");
        for (int i = 0; i < 10; i++) {
            Manager.createEntry(pathArray, Integer.toString(i)+"$$", "00010000");
        }
        for (int i = 10; i < 12; i++) {
            Manager.createEntry(pathArray, Integer.toString(i)+"$.t$", "00000000");
        }
//        DebugTool.print();

        pathArray.add("0$$");
        for (int i = 10; i < 30; i++) {
            Manager.createEntry(pathArray, Integer.toString(i)+"$", "00010000");
        }
//        DebugTool.print();

        pathArray.add("10$");
        Manager.createEntry(pathArray, "30$.t$", "00000000");
        for (int i = 31; i < 40; i++) {
            Manager.createEntry(pathArray, Integer.toString(i)+"$", "00010000");
        }
//        DebugTool.print(128);

        pathArray.remove(2);
        pathArray.remove(1);
        Manager.deleteEntry(pathArray,"0$$");
        DebugTool.print(128);

        for (int i = 40; i < 50; i++) {
            Manager.createEntry(pathArray, Integer.toString(i)+"$", "00010000");
        }
//        DebugTool.print(128);

    }
}
