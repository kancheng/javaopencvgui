package com.imageviewer;

/**
 * 圖像資訊數據類
 */
public class ImageInfo {
    private String fileName;
    private String filePath;
    private int width;
    private int height;
    private String format;
    private int channels;
    private long fileSize;

    public ImageInfo() {
    }

    public ImageInfo(String fileName, String filePath, int width, int height, 
                     String format, int channels, long fileSize) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.width = width;
        this.height = height;
        this.format = format;
        this.channels = channels;
        this.fileSize = fileSize;
    }

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return String.format("檔案: %s, 大小: %dx%d, 格式: %s, 通道數: %d, 檔案大小: %d bytes",
                fileName, width, height, format, channels, fileSize);
    }
}
