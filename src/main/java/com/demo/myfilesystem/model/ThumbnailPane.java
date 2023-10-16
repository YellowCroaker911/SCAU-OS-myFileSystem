package com.demo.myfilesystem.model;

import com.demo.myfilesystem.FileWindowMain;
import com.demo.myfilesystem.Main;
import com.demo.myfilesystem.kernel.entrytree.EntryTreeNode;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import static com.demo.myfilesystem.kernel.manager.Manager.closeFile;
import static com.demo.myfilesystem.utils.Constant.DIRECTORY_ICON;
import static com.demo.myfilesystem.utils.Constant.FILE_ICON;

// https://www.yiibai.com/javafx/javafx_borderpane.html

/**
 * 文件缩略图，窗口中间的 图标+文件名，每个文件一个实例
 */
public class ThumbnailPane extends BorderPane {
    private Text FileName;
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

        FileName = new Text(entry.getFullName().replace("$",""));  // 去掉$符号
        this.setBottom(FileName);
        BorderPane.setAlignment(FileName, Pos.CENTER); // 文字居中

        if(this.directory.getEntry().getInfo().isDirectory()){ // 根据文件类型设图像
            this.setCenter(new ImageView(DIRECTORY_ICON));
        }
        else{
            this.setCenter(new ImageView(FILE_ICON));
        }
//        this.setCenter(new ImageView(new Image(Main.class.getResource("")+"icon/file.png", 100, 100, true, true)));

    }

    /**
     * 打开文件/文件夹
     * @param mode  打开模式 (只读/读写)
     * @return  打开是否成功
     */
    public boolean openFile(String mode){
        assert !isDirectory(): "ThumbnailPane.openFile() directory cannot open as File";
        if(mode == null){
            if(getDirectory().getEntry().getInfo().isOnlyRead())mode = "r";
            else mode = "w";
        }
        if(mode.equals("r") || mode.equals("w")){
            // 本质就是开了一个新线程跑FileWindowMain
            String finalMode = mode;
            Platform.runLater(()->new FileWindowMain(directory, finalMode));
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
}
