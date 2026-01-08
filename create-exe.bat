@echo off
chcp 65001 >nul
echo ========================================
echo 創建 OpenCV 圖像檢視器 EXE 檔案
echo ========================================
echo.

REM 檢查 JAR 檔案是否存在
if not exist "target\opencv-image-viewer-1.0.0.jar" (
    echo 錯誤: 找不到 JAR 檔案！
    echo 請先執行: mvn clean package
    pause
    exit /b 1
)

echo [1/3] 檢查 Java 版本...
java -version
if errorlevel 1 (
    echo 錯誤: 找不到 Java！
    pause
    exit /b 1
)

echo.
echo [2/3] 檢查 jpackage 工具...
jpackage --version >nul 2>&1
if errorlevel 1 (
    echo 警告: jpackage 不可用，將使用替代方案...
    echo.
    echo 請使用 Launch4j 或其他工具來創建 EXE
    echo 或確保使用 Java 14 或更高版本
    pause
    exit /b 1
)

echo.
echo [3/3] 使用 jpackage 創建 EXE...
if not exist "dist" mkdir dist

jpackage ^
    --input target ^
    --name "OpenCV圖像檢視器" ^
    --main-jar opencv-image-viewer-1.0.0.jar ^
    --main-class com.imageviewer.Main ^
    --type exe ^
    --dest dist ^
    --win-shortcut ^
    --win-menu ^
    --app-version 1.0.0 ^
    --description "OpenCV 圖像檢視與處理應用程序" ^
    --vendor "Image Viewer"

if errorlevel 1 (
    echo.
    echo 創建 EXE 失敗！
    echo 請檢查錯誤訊息
    pause
    exit /b 1
)

echo.
echo ========================================
echo 成功！EXE 檔案已創建在 dist 目錄中
echo ========================================
echo.
dir dist\*.exe
echo.
pause
