package com.demo.myfilesystem.test;

import static com.demo.myfilesystem.kernel.io.IOtool.*;
import static com.demo.myfilesystem.utils.Constant.*;

public class DebugTool {

    public static void print(int col) {
        System.out.println("按字节打印前col个块：");
        System.out.printf("下标： ");
        for (int i = 0; i < BYTES_NUM_OF_BLOCK; i++) {
            System.out.printf("%4d",i);
        }
        System.out.println();
        for (int blockIndex = 0; blockIndex < col; blockIndex++) {
            byte[] buffer = readBlock(blockIndex);
            System.out.print("第" + blockIndex + "块：");
            for (int byteIndex = 0; byteIndex < BYTES_NUM_OF_BLOCK; byteIndex++) {
                System.out.printf("%4d", buffer[byteIndex]);
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------");
    }


}
