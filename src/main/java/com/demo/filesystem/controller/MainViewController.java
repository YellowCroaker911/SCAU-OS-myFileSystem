package com.demo.filesystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
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
    private TreeView<?> TreeViewFile;

    @FXML
    private HBox UpLeftHbox;

    @FXML
    private AnchorPane UpperPane;

    @FXML
    void goBackward(ActionEvent event) {

    }

    @FXML
    void goFather(ActionEvent event) {

    }

    @FXML
    void goForeward(ActionEvent event) {

    }

}
