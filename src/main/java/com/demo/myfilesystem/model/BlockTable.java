package com.demo.myfilesystem.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.demo.myfilesystem.utils.Constant.BLOCKS_NUM_OF_DISK;


/**
 * 右上侧磁盘各个块占用情况
 */
public class BlockTable extends GridPane {
    private final static int COLS = 8;
    private final static int ROWS = BLOCKS_NUM_OF_DISK / COLS; // 一行8个
    private final static int REC_WIDTH = 15;
    private final static int REC_HEIGHT = 10;

    private final Rectangle[][] table;
    public BlockTable(){
        super();
        this.setPadding(new Insets(0,0,0,10));
        this.setAlignment(Pos.CENTER_LEFT);  // 居中
        this.setVgap(5);   // 垂直距离
        this.setHgap(8);   // 水平距离
        table = new Rectangle[ROWS][COLS];
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
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
        // TODO: 从磁盘读取各个块使用情况
    }
}
