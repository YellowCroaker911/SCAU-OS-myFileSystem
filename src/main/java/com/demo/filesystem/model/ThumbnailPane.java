package com.demo.filesystem.model;

import com.demo.filesystem.FileWindowMain;
import com.demo.filesystem.kernel.DirectoryEntry;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.util.Optional;

// https://www.yiibai.com/javafx/javafx_borderpane.html
public class ThumbnailPane extends BorderPane {
    private ImageView imageView;
    private double actualWidth;
    private double actualHeight;
    private File imageFile;
    private Text imageName;
    private boolean isSelect;
    private DirectoryEntry directory;
    private static final double SIZE = 100;  // 缩略图大小 (小于加载图片的大小)
    private static final Insets INSETS = new Insets(5, 5, 0, 5);

    public ThumbnailPane(DirectoryEntry entry){
        this.setCache(false);
        this.setMaxSize(SIZE + 10, SIZE + 50);
        this.setMinSize(SIZE + 10, SIZE + 50);
        this.setPadding(INSETS);    //设置边距
        this.setPrefSize(SIZE+30, SIZE+50);

        imageName = new Text(entry.getName());
        this.setBottom(imageName);
        BorderPane.setAlignment(imageName, Pos.CENTER); // 文字居中
        // TODO:改成相对路径
        String path = "D:\\coding\\OSClassP\\2\\demo\\src\\main\\resources\\com\\demo\\filesystem\\icon\\file.png";
        this.setCenter(new ImageView(new Image(path, 120, 120, true, true)));   // TODO:图片应该可以改成一次性加载

        this.directory = entry;
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

    public boolean isDirectory(){return directory.isDirectory();}
    public DirectoryEntry getDirectory(){return directory;}

    public void Select(){
        this.setStyle("-fx-background-color: #cce8ff");
        isSelect = true;
    }
    public void unSelect(){
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