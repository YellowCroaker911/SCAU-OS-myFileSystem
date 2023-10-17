package com.demo.myfilesystem.model;

import com.demo.myfilesystem.kernel.entry.Entry;
import com.demo.myfilesystem.kernel.io.IOtool;
import com.demo.myfilesystem.kernel.manager.Manager;
import com.demo.myfilesystem.test.DebugTool;
import com.demo.myfilesystem.utils.Constant;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.demo.myfilesystem.utils.Constant.BLOCKS_NUM_OF_DISK;


/**
 * 右上侧磁盘各个块占用情况
 */
public class BlockTable extends GridPane {
    private final static int COLS = 8;
    private final static int ROWS = BLOCKS_NUM_OF_DISK / COLS; // 一行8个
    private final static int REC_WIDTH = 15;
    private final static int REC_HEIGHT = 10;
    private final static Color[] colors;

    static{
        String[] strs={"B0C4DE", "FF00FF", "1E90FF", "FA8072", "EEE8AA", "FF1493", "7B68EE",
                "FFC0CB", "696969", "556B2F", "CD853F", "000080", "32CD32", "7F007F",
                "B03060", "800000", "483D8B", "3CB371", "008B8B", "FF0000",
                "FF8C00", "FFD700", "00FF00", "9400D3", "00FA9A", "DC143C", "00FFFF",
                "00BFFF", "0000FF", "ADFF2F", "DA70D6"};
        colors=new Color[strs.length];
        for(int i=0;i<colors.length;i++)
        {
            colors[i]=Color.valueOf(strs[i]);
        }
    }


    private final Rectangle[][] table;
    public BlockTable(){
        super();
//        this.setPadding(new Insets(0,0,0,10));
        this.setAlignment(Pos.CENTER);  // 居中
        this.setVgap(5);   // 垂直距离
        this.setHgap(8);   // 水平距离
        table = new Rectangle[ROWS][COLS];
        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                table[i][j] = new Rectangle(REC_WIDTH, REC_HEIGHT);
                table[i][j].setFill(Color.WHITE);
                table[i][j].setStroke(Color.BLACK);
                table[i][j].setStrokeWidth(1);
                this.add(table[i][j], j, i);
            }
        }

        refresh();
    }

    public void refresh(){
        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                table[i][j].setFill(Color.GREEN);
            }
        }
        table[0][0].setFill(colors[0]);
        table[0][1].setFill(colors[1]);
        ArrayList<Pair<ArrayList<Integer>, Entry>> arr= Manager.listFATBlockLink();
        for(int i=0;i<arr.size();i++)
        {
            List<Integer> l=arr.get(i).getKey();
            for(int j=0;j<l.size();j++)
            {
                table[l.get(j)/COLS][l.get(j)%COLS].setFill(colors[(i+2)%colors.length]);
            }
        }
    }
}
