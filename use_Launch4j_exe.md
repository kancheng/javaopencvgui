# 使用 Launch4j 創建 EXE 檔案

## 步驟 1：下載 Launch4j

1. 訪問：https://launch4j.sourceforge.net/
2. 下載最新版本的 Launch4j
3. 解壓到任意目錄（例如：`C:\Tools\launch4j`）

## 步驟 2：使用 Launch4j

### 方法 A：使用圖形界面

1. **打開 Launch4j**
   - 運行 `Launch4j.exe`

2. **基本設定**
   - **Output file**: `C:\Users\Foxconn\Downloads\javaopencvgui\dist\OpenCV圖像檢視器.exe`
   - **Jar**: `C:\Users\Foxconn\Downloads\javaopencvgui\target\opencv-image-viewer-1.0.0.jar`
   - **Min JRE version**: `11`

3. **JRE 設定（選項卡）**
   - **Bundled JRE**: 留空（使用系統 JRE）
   - 或選擇 "Bundled JRE" 並指定 JRE 路徑（如果希望包含 JRE）

4. **版本資訊（選項卡）**
   - **File version**: `1.0.0.0`
   - **Product version**: `1.0.0`
   - **File description**: `OpenCV 圖像檢視與處理應用程序`
   - **Product name**: `OpenCV 圖像檢視器`

5. **生成 EXE**
   - 點擊 "Build wrapper" 按鈕
   - 如果成功，會顯示 "Wrapping completed successfully"

### 方法 B：使用命令行（如果 Launch4j 支持）

```powershell
cd C:\Tools\launch4j
.\launch4j.exe launch4j-config.xml
```

## 步驟 3：測試 EXE

1. 進入 `dist` 目錄
2. 雙擊 `OpenCV圖像檢視器.exe` 運行

## 注意事項

1. **JRE 要求**
   - 如果選擇不包含 JRE，用戶需要安裝 Java 11 或更高版本
   - 如果選擇包含 JRE，EXE 檔案會更大，但用戶無需安裝 Java

2. **OpenCV 原生庫**
   - OpenCV 需要原生 DLL 檔案
   - 這些檔案通常會自動從 Maven 依賴中提取
   - 如果遇到問題，可能需要手動複製 DLL 檔案到 EXE 同目錄

3. **檔案大小**
   - 不包含 JRE：約 50-100 MB
   - 包含 JRE：約 150-200 MB

## 替代方案：使用 jpackage（如果可用）

如果您的 JDK 包含 jpackage 工具，可以使用：

```powershell
# 找到 JDK 的 bin 目錄
$jdkPath = "C:\Program Files\Java\jdk-24"
& "$jdkPath\bin\jpackage.exe" `
    --input target `
    --name "OpenCV圖像檢視器" `
    --main-jar opencv-image-viewer-1.0.0.jar `
    --main-class com.imageviewer.Main `
    --type exe `
    --dest dist `
    --win-shortcut `
    --win-menu `
    --app-version 1.0.0
```
