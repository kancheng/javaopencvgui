package com.imageviewer;

import javax.swing.SwingUtilities;

/**
 * 主程序入口
 */
public class Main {
    public static void main(String[] args) {
        // 在事件分發線程中啟動 GUI
        SwingUtilities.invokeLater(() -> {
            try {
                ImageViewerGUI gui = new ImageViewerGUI();
                gui.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("無法啟動應用程序: " + e.getMessage());
            }
        });
    }
}
