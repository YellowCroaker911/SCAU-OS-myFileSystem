package com.demo.myfilesystem.model;

import com.demo.myfilesystem.FileWindowMain;
import com.demo.myfilesystem.kernel.entry.Entry;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.File;
import java.util.Optional;
import com.demo.myfilesystem.Main;

// https://www.yiibai.com/javafx/javafx_borderpane.html

/**
 * 文件缩略图，窗口中间的 图标+文件名，每个文件一个实例
 */
public class ThumbnailPane extends BorderPane {
    private ImageView imageView;
    private double actualWidth;
    private double actualHeight;
    private File imageFile;
    private Text imageName;
    private boolean isSelect;
    private final EntryTreeNode directory;
    private static final double SIZE = 100;  // 缩略图大小 (小于加载图片的大小)
    private static final Insets INSETS = new Insets(5, 5, 0, 5);

    public ThumbnailPane(EntryTreeNode entry){
        this.setCache(false);
        this.setMaxSize(SIZE + 10, SIZE + 50);
        this.setMinSize(SIZE + 10, SIZE + 50);
        this.setPadding(INSETS);    //设置边距
        this.setPrefSize(SIZE+30, SIZE+50);
        this.directory = entry;

        imageName = new Text(entry.getFullName().replace("$",""));  // 去掉$符号
        this.setBottom(imageName);
        BorderPane.setAlignment(imageName, Pos.CENTER); // 文字居中
        String path;
        if(this.directory.getEntry().getInfo().isDirectory()){
            path = Main.class.getResource("")+"icon/direct.png";
        }
        else{
            path =  Main.class.getResource("")+"icon/file.png";
        }
        this.setCenter(new ImageView(new Image(path, 100, 100, true, true)));   // TODO:图片应该可以改成一次性加载

//        setOnMouseClicked(e->{
//            myFlowPane father = (myFlowPane) this.getParent();  // 获取他爹
//            // 左键单击
//            if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1){
//                if(e.isControlDown()) {     // 按下Ctrl
//                    if(getisSelect())father.unSelect(this);
//                    else father.Select(this);
//                }
//                else {
//                    father.unSelectAll();   //清空选择
//                    father.Select(this);
//                }
//                father.updateTipInfoLabel();
//            }
//            else if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2){
//                Platform.runLater(()->ImageMain.main(imageFile.getAbsolutePath(), mainController, false));
//            }
//        });

    }

    /**
     * 打开文件/文件夹
     * @param mode  打开模式 (只读/读写)
     * @return  打开是否成功
     */
    public boolean openFile(String mode){
        assert !isDirectory(): "ThumbnailPane.openFile() directory cannot open as File";
        if(mode.equals("r") || mode.equals("rw")){
            // 本质就是开了一个新线程跑FileWindowMain
            Platform.runLater(()->new FileWindowMain(directory, mode));
            return true;
        }
        else {
            assert false : "ThumbnailPane.openFile() mode error!! mode="+mode;
        }
        return false;
    }

    public boolean isDirectory(){return directory.getEntry().getInfo().isDirectory();}
    public EntryTreeNode getDirectory(){return directory;}

    public void Select(){   // 被选择后改变背景色
        this.setStyle("-fx-background-color: #cce8ff");
        isSelect = true;
    }
    public void unSelect(){ // 背景色改回去
        this.setStyle("-fx-background-color: transparent");
        isSelect = false;
    }
    public boolean getisSelect(){return isSelect;}
    public File getImageFile(){return imageFile;}
    public Image getImage(){return imageView.getImage();}
    public long length(){return getImageFile().length();}

    /**
     * 重命名 不做就删了
     */
    public void rename(){
        // TODO: (重命名功能) FlowPane调这个函数实现单个文件重命名
//        Dialog<String> dialog = GenerateDialog.NewOneRenameDialog(imageFile.getName());
//        Optional<String> option = dialog.showAndWait();
//        option.ifPresent(s -> {
//            renameOneFile(s, true);
//            mainController.refresh();
//        });
    }
    private int renameOneFile(String destName, boolean isInform){
        return 0;
//        if(destName.equals(imageFile.getName()))    // 名字没变直接返回
//            return 0;
//        File dest = new File(imageFile.getParentFile().getAbsolutePath() + "\\" + destName);
//        if(dest.exists()){  // 该名字已存在
//            if(isInform) {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setHeaderText("该文件名已存在");
//                alert.showAndWait();
//            }
//            return 1;
//        }
//        if(!imageFile.renameTo(dest)){
//            if(isInform){
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setHeaderText("重命名失败");
//                alert.showAndWait();
//            }
//            return 1;
//        }
//        if(isInform){
//            Notifications.create()
//                    .text("重命名成功")
//                    .owner(mainController.getStage())
//                    .position(Pos.TOP_CENTER)
//                    .hideAfter(Duration.seconds(3))
//                    .show();
//        }
//        return 0;
    }
    public double getActualWidth(){return actualWidth;}
    public double getActualHeight(){return actualHeight;}
}
