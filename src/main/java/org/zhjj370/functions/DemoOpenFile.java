package org.zhjj370.functions;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class DemoOpenFile {

    public void openFile(String filePath) {
        try {
            File file = new File(filePath); // 创建文件对象
            Desktop.getDesktop().open(file); // 启动已在本机桌面上注册的关联应用程序，打开文件文件file。
        } catch (IOException | NullPointerException e) { // 异常处理
            System.err.println(e);
        }
    }

    public static void main(String[] args) {

        DemoOpenFile openFile = new DemoOpenFile(); // 实例化TestOpenFile类，对象为tof。
        openFile.openFile("output/out.md");

    }

}
