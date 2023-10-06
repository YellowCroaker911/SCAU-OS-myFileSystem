package com.demo.myfilesystem.utils;

import javafx.scene.control.TextField;

/**
 * 带正则表达式判断的文本框
 * 仅当输入的文本满足正则才会接收输入
 */
public class RegexTextField extends TextField {
    public RegexTextField(String regex){
        super();
        this.textProperty().addListener((o, oldValue, newValue)->{
            if(newValue.matches(regex))  this.setText(newValue);// 匹配就接收值
            else this.setText(oldValue);    // 不匹配就设回去
        });
    }
}
