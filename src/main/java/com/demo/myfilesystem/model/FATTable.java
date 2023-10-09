package com.demo.myfilesystem.model;

import com.demo.myfilesystem.kernel.io.IOtool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * TODO:右下角FAT
 */
public class FATTable extends TableView<ByteData> {
    private TableColumn<ByteData,Integer> c1;
    private TableColumn<ByteData,Byte> c2;
    public FATTable(){
        super();
        c1= new TableColumn("index");
        c2 = new TableColumn("next");

        c1.prefWidthProperty().bind(this.widthProperty().multiply(0.5));
        c2.prefWidthProperty().bind(this.widthProperty().multiply(0.5));

        c1.setResizable(false);
        c2.setResizable(false);

        c1.setSortable(false);
        c2.setSortable(false);
        c1.setCellValueFactory(new PropertyValueFactory<>("index"));
        c2.setCellValueFactory(new PropertyValueFactory<>("next"));

        this.getColumns().addAll(c1,c2);
        this.setEditable(false);

        re();
    }

    private void re()
    {
        byte[] fat= IOtool.getFattable();
        ObservableList<ByteData> data = FXCollections.observableArrayList();
        for (int i = 0; i < fat.length; i++) {
            data.add(new ByteData(i, fat[i]));
        }

        this.setItems(data);

    }
}
