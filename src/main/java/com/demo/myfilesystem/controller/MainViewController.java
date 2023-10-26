package com.demo.myfilesystem.controller;

import com.demo.myfilesystem.kernel.entry.Entry;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import com.demo.myfilesystem.kernel.manager.Manager;
import com.demo.myfilesystem.model.BlockTable;
import com.demo.myfilesystem.model.FileFlowPane;
import com.demo.myfilesystem.model.MyTreeItem;
import com.sun.source.tree.Tree;
import com.demo.myfilesystem.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Stack;

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
    private AnchorPane FATTableAnchor;

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

    @FXML
    private BorderPane BlockInfoAnchor;

    @FXML
    private StackPane RootPane;
    @FXML
    private SplitPane RightSplitPane;

    private FileFlowPane flowPane;
    private BlockTable blockTable;
    private FATTable FatTable;
    private Stage MainStage;
    @FXML
    private void initialize(){
        initTreeView();     // 目录树
        initFlowPane();     // 中间文件详情页
        initDiskInfo();
    }
    public void setStage(Stage stage){
        MainStage = stage;
        autoAdapt();
    }

    private void initTreeView(){
        MyTreeItem root = new MyTreeItem(Manager.getTopPath());
        TreeViewFile.setRoot(root);

        // 双击目录项触发
        TreeViewFile.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            chooseDirectory(oldValue, newValue);
        });
    }

    private void initFlowPane(){
        flowPane = new FileFlowPane(this, PathText);
        FileAnchorPane.getChildren().add(flowPane);
    }
    private void autoAdapt(){
        // FlowPane大小始终布满中间
        flowPane.prefWidthProperty().bind(FileAnchorPane.widthProperty());
        flowPane.prefHeightProperty().bind(FileAnchorPane.heightProperty());
        FileAnchorPane.prefHeightProperty().bind(SplitPane.heightProperty());
        SplitPane.prefHeightProperty().bind(MainStage.heightProperty());


    }

    /**
     * 目录树双击某个文件夹，在中间展示所有文件
     */
    private void chooseDirectory(TreeItem<String> oldValue, TreeItem<String> newValue) {
        assert(newValue != null);
        assert(newValue instanceof MyTreeItem);
        MyTreeItem item = (MyTreeItem) newValue;
		System.out.println("click node = " + item.getEntryTreeNode().getFullName());
        flowPane.openDirectory(item.getEntryTreeNode(), true);    // 在文件夹内容窗口更新
    }

    public void refresh(EntryTreeNode entryTreeNode){
        for(int i = 0;;i++){
            MyTreeItem item = (MyTreeItem)TreeViewFile.getTreeItem(i);
            if(item==null)break;
            if(item.getEntryTreeNode() == entryTreeNode) {    // (判引用)这样是不是能降低开销
                (item).resetInitialize();
                if(item.isExpanded()){  // 通过关了再开实现孩子的更新
                    item.setExpanded(false);
                    item.setExpanded(true);
                }
                break;
            }
        }
        refreshDiskInfo();
    }
    public void refreshDiskInfo(){
        FatTable.refreshTable();
        blockTable.refresh();
    }

    /**********************顶部三个按钮相关***********************/
    private final Stack<EntryTreeNode> BackStack = new Stack<>();   // 栈顶为当前文件夹

    private final Stack<EntryTreeNode> ForeStack = new Stack<>();

    /**
     * 打开文件夹的同时更新按钮
     * @param entry 文件夹
     */
    public void updateToButton(EntryTreeNode entry){
        // 更新按钮
        if(!BackStack.empty() && BackStack.peek() == entry)
            return;
        BackStack.push(entry);
        ForeStack.clear();     // 自己点了文件就清空向前栈

        updateButtonState();
    }
    private void updateButtonState(){
        BackButton.setDisable(BackStack.size() < 2);
        ForwardButton.setDisable(ForeStack.size() == 0);
        FatherButton.setDisable(BackStack.peek().getParentNode() == null); // 当前文件有爹才可以点
    }
    @FXML
    private void goBackward(ActionEvent event) {
        assert BackStack.size() >= 2: "你不应该能按这东西";
        ForeStack.push(BackStack.peek());
        BackStack.pop();
        EntryTreeNode target = BackStack.peek();
        flowPane.openDirectory(target, false);
        updateButtonState();
    }

    @FXML
    private void goForeward(ActionEvent event) {
        assert !ForeStack.empty(): "你不应该能按这东西";
        EntryTreeNode target = ForeStack.peek();
        BackStack.push(target);
        ForeStack.pop();
        flowPane.openDirectory(target, false);
        updateButtonState();
    }

    @FXML
    private void goFather(ActionEvent event) {
        EntryTreeNode current = BackStack.peek();
        assert current.getParentNode() != null: "你不应该能按这东西";
        EntryTreeNode parent = current.getParentNode();
        // 和点击文件夹访问是一致的
        flowPane.openDirectory(parent, true);
    }

    /****************窗口右侧的信息展示有关************************/
    private void initDiskInfo() {
        blockTable = new BlockTable();
//        blockTable.prefHeightProperty()
        // 将blockTable的宽高和他爹绑定
        BlockInfoAnchor.setCenter(blockTable);
//        blockTable.prefHeightProperty().bind(BlockInfoAnchor.prefHeightProperty());
//        blockTable.prefWidthProperty().bind(BlockInfoAnchor.prefWidthProperty());

        FatTable = new FATTable();
        FatTable.prefHeightProperty().bind(FATTableAnchor.heightProperty());
        FatTable.prefWidthProperty().bind(FATTableAnchor.widthProperty());
        FATTableAnchor.getChildren().add(FatTable);

        RightSplitPane.setDividerPosition(0,0.5);
    }


    /*******************窗口右侧 End*****************************/

}
