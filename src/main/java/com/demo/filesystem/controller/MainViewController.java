package com.demo.filesystem.controller;

import com.demo.filesystem.kernel.DirectoryEntry;
import com.demo.filesystem.model.FileFlowPane;
import com.demo.filesystem.model.myTreeItem;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainViewController {

    @FXML
    private Button BackButton;

    @FXML
    private Button FatherButton;

    @FXML
    private AnchorPane FileAnchorPane;

    @FXML
    private ScrollPane FileScrollPane;

    @FXML
    private Button ForwardButton;

    @FXML
    private VBox MainVbox;

    @FXML
    private TextField PathText;

    @FXML
    private SplitPane SplitPane;

    @FXML
    private TreeView<String> TreeViewFile;

    @FXML
    private HBox UpLeftHbox;

    @FXML
    private AnchorPane UpperPane;

    private FileFlowPane flowPane;

    @FXML
    private void initialize(){
        initTreeView();     // 目录树
        initFlowPane();     // 中间文件详情页
        autoAdapt();
        // TODO: 右侧有一个块占用情况
        // TODO: 右侧的表
    }

    private void initTreeView(){
//        System.out.println("qweqwe");
        myTreeItem root = new myTreeItem(new DirectoryEntry());
//        root.addChildren(new DirectoryEntry());
//        var tmp = root.getDirectoryEntry().listDirectoryEntries();
//        System.out.println(tmp.length);
//        for(var c:tmp){
//            String s = c.getName();
//            for(int i = 0; i < s.length(); i++){
//                System.out.print((int)(s.charAt(i)));
//                System.out.print(" ");
//            }
//            System.out.print("  ");
//            s = c.getTypeName();
//            for(int i = 0; i < s.length(); i++){
//                System.out.print((int)(s.charAt(i)));
//                System.out.print(" ");
//            }
//            System.out.println("");
//        }
//        System.out.println(root.isLeaf());
        TreeViewFile.setRoot(root);

        // 双击目录项触发
        TreeViewFile.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            chooseDirectory(oldValue, newValue);
        });
    }

    private void initFlowPane(){
        flowPane = new FileFlowPane();
        FileAnchorPane.getChildren().add(flowPane);
    }
    private void autoAdapt(){
        // TODO:窗口自适应，FlowPane的大小没有布满中间
    }

    /**
     * 目录树双击某个文件夹，在中间展示所有文件
     */
    private void chooseDirectory(TreeItem<String> oldValue, TreeItem<String> newValue) {
        assert(newValue != null);
        assert(newValue instanceof myTreeItem);
        myTreeItem item = (myTreeItem) newValue;
        flowPane.openDirectory(item.getDirectoryEntry());
    }

    @FXML
    private void goBackward(ActionEvent event) {
        // TODO
    }

    @FXML
    private void goFather(ActionEvent event) {
        // TODO
    }

    @FXML
    private void goForeward(ActionEvent event) {
        // TODO
    }

    /****************TODO:窗口右侧的信息展示有关************************/
//    private void refresh(){
//
//    }



    /*******************窗口右侧 End*****************************/

}
