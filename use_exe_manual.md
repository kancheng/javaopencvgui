# 將 Java 應用程序打包為 EXE 檔案

## 方法一：使用 jpackage（推薦，Java 14+）

`jpackage` 是 Java 自帶的工具，可以將 JAR 檔案打包成 Windows 可執行檔案。

### 步驟

1. **確認 JAR 檔案已生成**
   ```powershell
   dir target\opencv-image-viewer-1.0.0.jar
   ```

2. **使用 jpackage 創建 EXE**
   ```powershell
   jpackage --input target --name "OpenCV圖像檢視器" --main-jar opencv-image-viewer-1.0.0.jar --main-class com.imageviewer.Main --type exe --dest dist --win-shortcut --win-menu
   ```

   或者使用更詳細的配置：
   ```powershell
   jpackage --input target --name "OpenCV圖像檢視器" --main-jar opencv-image-viewer-1.0.0.jar --main-class com.imageviewer.Main --type exe --dest dist --win-shortcut --win-menu --app-version 1.0.0 --description "OpenCV 圖像檢視與處理應用程序" --vendor "Image Viewer"
   ```

3. **執行結果**
   - EXE 檔案會生成在 `dist` 目錄中
   - 檔案名為 `OpenCV圖像檢視器.exe`

### jpackage 參數說明

- `--input target`: 指定輸入目錄（包含 JAR 檔案）
- `--name "OpenCV圖像檢視器"`: 應用程序名稱
- `--main-jar opencv-image-viewer-1.0.0.jar`: 主 JAR 檔案
- `--main-class com.imageviewer.Main`: 主類別
- `--type exe`: 輸出類型為 EXE
- `--dest dist`: 輸出目錄
- `--win-shortcut`: 創建 Windows 快捷方式
- `--win-menu`: 添加到開始菜單

## 方法二：使用 Launch4j

Launch4j 是一個免費的工具，可以將 JAR 包裝成 Windows EXE。

### 安裝 Launch4j

1. 下載：https://launch4j.sourceforge.net/
2. 解壓並運行 Launch4j.exe

### 使用步驟

1. **基本設定**
   - Output file: `dist\OpenCV圖像檢視器.exe`
   - Jar: `target\opencv-image-viewer-1.0.0.jar`
   - Min JRE version: `11`

2. **JRE 設定**
   - 選擇 "Bundled JRE" 或 "Search registry"
   - 如果選擇 Bundled，需要指定 JRE 路徑

3. **生成 EXE**
   - 點擊 "Build wrapper" 按鈕

## 方法三：使用 GraalVM Native Image（進階）

GraalVM 可以將 Java 應用編譯為原生可執行檔案，無需 JVM。

### 步驟

1. 安裝 GraalVM
2. 使用 native-image 工具編譯

## 注意事項

1. **OpenCV 原生庫**
   - OpenCV 需要原生庫（.dll 檔案）
   - 確保這些庫與 EXE 一起分發，或在系統 PATH 中

2. **JRE 依賴**
   - 使用 jpackage 或 Launch4j 時，可以選擇是否包含 JRE
   - 包含 JRE 會增加檔案大小，但用戶無需安裝 Java

3. **檔案大小**
   - 包含所有依賴和 JRE 的 EXE 可能很大（100MB+）
   - 這是正常的，因為包含了 OpenCV 和 Java 運行時

## 快速執行腳本

我已經創建了 `create-exe.bat` 腳本，可以直接執行來生成 EXE 檔案。
