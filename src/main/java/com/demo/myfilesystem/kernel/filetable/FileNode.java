package com.demo.myfilesystem.kernel.filetable;

import com.demo.myfilesystem.kernel.entry.Entry;
import com.demo.myfilesystem.kernel.io.Pointer;

import java.util.ArrayList;

import static com.demo.myfilesystem.kernel.io.IOtool.*;
import static com.demo.myfilesystem.kernel.manager.ManagerHelper.*;
import static com.demo.myfilesystem.utils.Constant.*;

public class FileNode {

    private ArrayList<String> pathArray;

    private String fullName;
    private Entry entry;
    private String mode;
    private Pointer rPointer;
    private Pointer wPointer;

    public FileNode(ArrayList<String> pathArray, Entry entry, String mode) {
        this.pathArray = pathArray;
        this.fullName = entry.getInfo().getFullName();
        this.entry = entry;
        this.mode = mode;
        this.rPointer = new Pointer(this.entry.getInfo().getStartBlockIndex(), 0, "byte");
        if (mode.equals("w")) {
            this.wPointer = this.tailPointer();
        } else {
            this.wPointer = new Pointer(this.entry.getInfo().getStartBlockIndex(), 0, "byte");
        }
    }

    public Pointer tailPointer() {
        int endBlockIndex = this.entry.endBlockIndex();
        byte[] buffer = readBlock(endBlockIndex);
        for (int byteOffset = 0; byteOffset < BYTES_NUM_OF_BLOCK; byteOffset++) {
            if (buffer[byteOffset] == '#') {
                return new Pointer(endBlockIndex, byteOffset, "byte");
            }
        }
        return null;
    }

    public int closeUpdate() {
        if (this.mode.equals("w")) {
            if (this.appendEndMark() == -1) {
                return -1;
            }
        }
        return 1;
    }

    public int appendEndMark() {
        if (!this.wPointer.hasNext()) {
            if (openUpSpace(this) == -1) {
                return -1;
            }
        }
        this.wPointer.next();
        this.wPointer.putByte((byte) '#');
        return 1;
    }

    public ArrayList<String> getPathArray() {
        return pathArray;
    }

    public String getFullName() {
        return fullName;
    }

    public Entry getEntry() {
        return entry;
    }

    public String getMode() {
        return mode;
    }

    public Pointer getRPointer() {
        return rPointer;
    }

    public Pointer getWPointer() {
        return wPointer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        FileNode other = (FileNode) obj;
        return this.getPathArray().equals(other.getPathArray()) && this.getFullName().equals(other.getFullName());
    }
}
