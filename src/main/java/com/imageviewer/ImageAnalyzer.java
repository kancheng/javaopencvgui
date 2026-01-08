package com.imageviewer;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 圖像分析器類
 */
public class ImageAnalyzer {
    private static final String[] SUPPORTED_FORMATS = {
        ".jpg", ".jpeg", ".png", ".bmp", ".gif", ".tiff", ".tif", ".webp"
    };

    /**
     * 檢查檔案是否為支援的圖像格式
     */
    public static boolean isImageFile(File file) {
        if (file == null || !file.isFile()) {
            return false;
        }
        String fileName = file.getName().toLowerCase();
        for (String format : SUPPORTED_FORMATS) {
            if (fileName.endsWith(format)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 獲取目錄中的所有圖像檔案
     */
    public static List<File> getImageFiles(String directoryPath) {
        List<File> imageFiles = new ArrayList<>();
        try {
            File directory = new File(directoryPath);
            if (!directory.exists() || !directory.isDirectory()) {
                return imageFiles;
            }

            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (isImageFile(file)) {
                        imageFiles.add(file);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFiles;
    }

    /**
     * 分析單個圖像檔案
     */
    public static ImageInfo analyzeImage(File imageFile) {
        if (imageFile == null || !imageFile.exists()) {
            return null;
        }

        try {
            Mat image = Imgcodecs.imread(imageFile.getAbsolutePath());
            if (image.empty()) {
                return null;
            }

            int width = image.cols();
            int height = image.rows();
            int channels = image.channels();
            String format = getImageFormat(imageFile.getName());
            long fileSize = Files.size(Paths.get(imageFile.getAbsolutePath()));

            image.release();

            return new ImageInfo(
                imageFile.getName(),
                imageFile.getAbsolutePath(),
                width,
                height,
                format,
                channels,
                fileSize
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分析目錄中的所有圖像
     */
    public static List<ImageInfo> analyzeDirectory(String directoryPath) {
        List<ImageInfo> imageInfos = new ArrayList<>();
        List<File> imageFiles = getImageFiles(directoryPath);

        for (File file : imageFiles) {
            ImageInfo info = analyzeImage(file);
            if (info != null) {
                imageInfos.add(info);
            }
        }

        return imageInfos;
    }

    /**
     * 統計目錄中所有圖像的資訊
     */
    public static DirectoryStatistics getDirectoryStatistics(String directoryPath) {
        List<ImageInfo> imageInfos = analyzeDirectory(directoryPath);
        return new DirectoryStatistics(imageInfos);
    }

    /**
     * 獲取圖像格式
     */
    private static String getImageFormat(String fileName) {
        String lowerName = fileName.toLowerCase();
        if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
            return "JPEG";
        } else if (lowerName.endsWith(".png")) {
            return "PNG";
        } else if (lowerName.endsWith(".bmp")) {
            return "BMP";
        } else if (lowerName.endsWith(".gif")) {
            return "GIF";
        } else if (lowerName.endsWith(".tiff") || lowerName.endsWith(".tif")) {
            return "TIFF";
        } else if (lowerName.endsWith(".webp")) {
            return "WEBP";
        }
        return "UNKNOWN";
    }

    /**
     * 目錄統計資訊類
     */
    public static class DirectoryStatistics {
        private int totalImages;
        private long totalFileSize;
        private java.util.Map<String, Integer> formatCount;
        private java.util.Map<Integer, Integer> channelCount;
        private int minWidth, maxWidth, minHeight, maxHeight;

        public DirectoryStatistics(List<ImageInfo> imageInfos) {
            this.totalImages = imageInfos.size();
            this.formatCount = new java.util.HashMap<>();
            this.channelCount = new java.util.HashMap<>();
            this.totalFileSize = 0;
            this.minWidth = Integer.MAX_VALUE;
            this.maxWidth = 0;
            this.minHeight = Integer.MAX_VALUE;
            this.maxHeight = 0;

            for (ImageInfo info : imageInfos) {
                totalFileSize += info.getFileSize();
                formatCount.put(info.getFormat(), formatCount.getOrDefault(info.getFormat(), 0) + 1);
                channelCount.put(info.getChannels(), channelCount.getOrDefault(info.getChannels(), 0) + 1);
                minWidth = Math.min(minWidth, info.getWidth());
                maxWidth = Math.max(maxWidth, info.getWidth());
                minHeight = Math.min(minHeight, info.getHeight());
                maxHeight = Math.max(maxHeight, info.getHeight());
            }
        }

        public int getTotalImages() {
            return totalImages;
        }

        public long getTotalFileSize() {
            return totalFileSize;
        }

        public java.util.Map<String, Integer> getFormatCount() {
            return formatCount;
        }

        public java.util.Map<Integer, Integer> getChannelCount() {
            return channelCount;
        }

        public int getMinWidth() {
            return minWidth == Integer.MAX_VALUE ? 0 : minWidth;
        }

        public int getMaxWidth() {
            return maxWidth;
        }

        public int getMinHeight() {
            return minHeight == Integer.MAX_VALUE ? 0 : minHeight;
        }

        public int getMaxHeight() {
            return maxHeight;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("總圖像數: ").append(totalImages).append("\n");
            sb.append("總檔案大小: ").append(totalFileSize).append(" bytes\n");
            sb.append("寬度範圍: ").append(getMinWidth()).append(" - ").append(getMaxWidth()).append("\n");
            sb.append("高度範圍: ").append(getMinHeight()).append(" - ").append(getMaxHeight()).append("\n");
            sb.append("格式統計: ").append(formatCount).append("\n");
            sb.append("通道數統計: ").append(channelCount).append("\n");
            return sb.toString();
        }
    }
}
