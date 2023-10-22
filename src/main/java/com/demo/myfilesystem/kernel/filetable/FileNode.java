package com.demo.myfilesystem.kernel.filetable;

import com.demo.myfilesystem.kernel.entry.Entry;
import com.demo.myfilesystem.kernel.io.Pointer;

import java.util.ArrayList;
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
        this.rPointer = this.getEntry().getInfo().startBytePointer();
        if (mode.equals(WRITE)) {
            this.wPointer = this.getEntry().tailPointer();
        } else {
            this.wPointer = this.getEntry().getInfo().startBytePointer();
        }
    }



    public void appendEndMark() {
        this.wPointer.putByte(FILE_END_MARK_BYTE);
    }

    public int requiredFreeSpaceNum(String str) {
        int remainingFreeByte = BYTES_NUM_OF_BLOCK - this.getEntry().tailPointer().getByteOffset();
        int len = str.length() + 1; // 第一个+1考虑预留文件结束符那一位
        if (len <= remainingFreeByte) return 0;
        else {
            return (len - remainingFreeByte + BYTES_NUM_OF_BLOCK - 1) / BYTES_NUM_OF_BLOCK;
        }
    }

    public void write(String str) {
        for (int i = 0; i < str.length(); i++) {
            this.wPointer.putByte((byte) str.charAt(i));
            assert this.wPointer.hasNext();
            this.wPointer.next();
        }
        this.appendEndMark();
    }

    public int bytesLength() {
        return (this.getEntry().blocksIndex().size() - 1) * BYTES_NUM_OF_BLOCK + this.getEntry().tailPointer().getByteOffset();
    }

    public String read(int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append((char) this.rPointer.loadByte());
            assert this.rPointer.hasNext();
            this.rPointer.next();
        }
        return sb.toString();
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
