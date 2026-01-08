# OpenCV 圖像檢視器

一個使用 Java Swing 和 OpenCV 開發的圖像檢視與處理應用程序，提供類似相簿的圖像瀏覽功能和 OpenCV 圖像處理預覽功能。

## 功能特性

1. **目錄瀏覽**：可以選擇指定目錄，自動掃描並列出所有支援的圖像檔案
2. **圖像預覽**：可以逐張檢視目錄中的圖像
3. **圖像分析**：分析目錄中每張圖像的詳細資訊（大小、格式、通道數等）
4. **統計資訊**：統計整個目錄中所有圖像的詳細資訊
5. **OpenCV 處理**：提供 GUI 操作介面調整 OpenCV 處理參數，即時預覽處理效果
6. **參數導出**：將 OpenCV 處理參數導出為 JSON 格式檔案

## 支援的圖像格式

- JPEG (.jpg, .jpeg)
- PNG (.png)
- BMP (.bmp)
- GIF (.gif)
- TIFF (.tiff, .tif)
- WebP (.webp)

## OpenCV 處理功能

- **灰階轉換**：將彩色圖像轉換為灰階
- **亮度/對比度調整**：調整圖像的亮度和對比度
- **高斯模糊**：應用高斯模糊濾波
- **雙邊濾波**：應用雙邊濾波以保持邊緣
- **形態學操作**：腐蝕、膨脹、開運算、閉運算
- **閾值處理**：多種閾值處理模式
- **Canny 邊緣檢測**：檢測圖像邊緣

## 系統需求

- Java 11 或更高版本
- Maven 3.6 或更高版本
- OpenCV 4.8.0（會透過 Maven 自動下載）

## 編譯與執行

### 編譯專案

```bash
mvn clean compile
```

### 打包專案

```bash
mvn clean package
```

### 執行應用程序

```bash
mvn exec:java -Dexec.mainClass="com.imageviewer.Main"
```

或者執行打包後的 JAR 檔案：

```bash
java -jar target/opencv-image-viewer-1.0.0.jar
```

## 使用說明

1. **選擇目錄**：點擊「瀏覽目錄」按鈕，選擇包含圖像的目錄
2. **檢視圖像**：在左側列表中選擇圖像，或使用「上一張」「下一張」按鈕瀏覽
3. **查看資訊**：在「圖像資訊」標籤中查看當前圖像的詳細資訊
4. **查看統計**：在「目錄統計」標籤中查看整個目錄的統計資訊
5. **調整參數**：在「OpenCV 參數」標籤中調整各種處理參數，即時預覽效果
6. **切換顯示**：使用「顯示原圖」和「顯示處理後」按鈕切換顯示模式
7. **導出參數**：點擊「導出 JSON 參數」按鈕，將當前參數設定儲存為 JSON 檔案

## 專案結構

```
javaopencvgui/
├── pom.xml                          # Maven 專案配置
├── README.md                        # 說明文件
└── src/
    └── main/
        └── java/
            └── com/
                └── imageviewer/
                    ├── Main.java              # 主程序入口
                    ├── ImageViewerGUI.java    # GUI 主界面
                    ├── ImageInfo.java         # 圖像資訊數據類
                    ├── OpenCVParams.java      # OpenCV 參數類
                    ├── ImageAnalyzer.java     # 圖像分析器
                    └── OpenCVProcessor.java   # OpenCV 處理器
```

## JSON 參數格式

導出的 JSON 檔案包含所有 OpenCV 處理參數，格式如下：

```json
{
  "applyGaussianBlur": false,
  "gaussianBlurKernelSize": 5,
  "gaussianBlurSigmaX": 1.0,
  "applyCanny": false,
  "cannyThreshold1": 50.0,
  "cannyThreshold2": 150.0,
  "applyBilateralFilter": false,
  "bilateralFilterD": 9,
  "bilateralFilterSigmaColor": 75.0,
  "bilateralFilterSigmaSpace": 75.0,
  "applyThreshold": false,
  "thresholdValue": 127.0,
  "thresholdType": 0,
  "applyMorphology": false,
  "morphologyType": 0,
  "morphologyKernelSize": 5,
  "applyBrightnessContrast": false,
  "brightness": 0.0,
  "contrast": 1.0,
  "applyGrayscale": false
}
```

## 注意事項

- 首次執行時，OpenCV 庫會自動下載，可能需要一些時間
- 處理大型圖像時，預覽更新可能需要一些時間
- 某些 OpenCV 操作（如 Canny 邊緣檢測）會將圖像轉換為單通道，後續的彩色處理將無法應用

## 授權

本專案僅供學習和參考使用。
