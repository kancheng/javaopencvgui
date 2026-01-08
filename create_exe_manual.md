# 創建獨立執行的 EXE 檔案 - 完整指南

## 當前狀態

✅ **JAR 檔案已成功創建**
- 位置：`target\opencv-image-viewer-1.0.0.jar`
- 包含所有依賴（OpenCV、Gson 等）
- 可以直接執行：`java -jar target\opencv-image-viewer-1.0.0.jar`

## 方法一：使用 Launch4j（最簡單，推薦）

### 優點
- 不需要額外工具（除了 Launch4j）
- 簡單易用
- 支持中文應用程序名稱
- 可以選擇是否包含 JRE

### 步驟

1. **下載 Launch4j**
   - 訪問：https://launch4j.sourceforge.net/
   - 下載最新版本（例如：launch4j-3.50-win32.zip）
   - 解壓到任意目錄（例如：`C:\Tools\launch4j`）

2. **打開 Launch4j**
   - 運行 `Launch4j.exe`

3. **配置設定**
   
   **基本選項卡：**
   - **Output file**: `C:\Users\Foxconn\Downloads\javaopencvgui\dist\OpenCV圖像檢視器.exe`
   - **Jar**: `C:\Users\Foxconn\Downloads\javaopencvgui\target\opencv-image-viewer-1.0.0.jar`
   - **Min JRE version**: `11`
   - **Max JRE version**: 留空
   - **JRE path**: 留空（使用系統 JRE）

   **JRE 選項卡：**
   - **Bundled JRE**: 留空（不包含 JRE，用戶需要安裝 Java）
   - 或選擇 "Bundled JRE" 並指定 JRE 路徑（包含 JRE，檔案更大）

   **版本資訊選項卡（可選）：**
   - **File version**: `1.0.0.0`
   - **Product version**: `1.0.0`
   - **File description**: `OpenCV 圖像檢視與處理應用程序`
   - **Product name**: `OpenCV 圖像檢視器`
   - **Copyright**: `2025`

4. **生成 EXE**
   - 點擊 "Build wrapper" 按鈕
   - 如果成功，會顯示 "Wrapping completed successfully"
   - EXE 檔案會生成在 `dist` 目錄中

5. **測試**
   - 進入 `dist` 目錄
   - 雙擊 `OpenCV圖像檢視器.exe` 運行

### 使用配置文件（快速方法）

1. 打開 Launch4j
2. 點擊 "File" → "Load config"
3. 選擇 `launch4j-config.xml`
4. 確認路徑正確
5. 點擊 "Build wrapper"

## 方法二：使用 jpackage（需要 WiX）

### 前置需求

1. **安裝 WiX Toolset**
   - 下載：https://wixtoolset.org/
   - 安裝 WiX v3 或更高版本
   - 確保 `light.exe` 和 `candle.exe` 在系統 PATH 中

2. **執行命令**

```powershell
& "C:\Program Files\Java\jdk-24\bin\jpackage.exe" `
    --input target `
    --name "OpenCVImageViewer" `
    --main-jar opencv-image-viewer-1.0.0.jar `
    --main-class com.imageviewer.Main `
    --type exe `
    --dest dist `
    --win-shortcut `
    --win-menu `
    --app-version 1.0.0 `
    --description "OpenCV Image Viewer" `
    --vendor "Image Viewer"
```

## 方法三：使用 GraalVM Native Image（進階）

可以創建真正的原生可執行檔案，無需 JVM。

### 步驟

1. 安裝 GraalVM
2. 安裝 native-image 組件
3. 編譯為原生映像

## 推薦方案

**對於大多數用戶，推薦使用 Launch4j**，因為：
- 不需要安裝額外工具（除了 Launch4j 本身）
- 配置簡單
- 支持中文
- 可以選擇是否包含 JRE

## 檔案大小參考

- **JAR 檔案**: ~110 MB（包含 OpenCV 原生庫）
- **EXE（不包含 JRE）**: ~110-120 MB
- **EXE（包含 JRE）**: ~250-300 MB

## 注意事項

1. **OpenCV 原生庫**
   - OpenCV 需要原生 DLL 檔案
   - Launch4j 會自動處理這些依賴
   - 如果遇到 DLL 錯誤，可能需要手動複製 DLL 檔案

2. **Java 運行時**
   - 如果選擇不包含 JRE，用戶需要安裝 Java 11 或更高版本
   - 如果選擇包含 JRE，EXE 檔案會更大，但用戶無需安裝 Java

3. **測試**
   - 在創建 EXE 後，建議在不同電腦上測試
   - 確保所有依賴都正確包含

## 快速開始

1. 下載 Launch4j：https://launch4j.sourceforge.net/
2. 打開 Launch4j.exe
3. 載入 `launch4j-config.xml` 或手動配置
4. 點擊 "Build wrapper"
5. 完成！
