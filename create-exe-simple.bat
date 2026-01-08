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

echo JAR 檔案已準備就緒: target\opencv-image-viewer-1.0.0.jar
echo.

REM 創建 dist 目錄
if not exist "dist" mkdir dist

echo ========================================
echo 請選擇創建 EXE 的方法：
echo ========================================
echo.
echo 方法 1: 使用 Launch4j（推薦）
echo   - 下載 Launch4j: https://launch4j.sourceforge.net/
echo   - 打開 Launch4j.exe
echo   - 載入 launch4j-config.xml 或手動配置
echo   - 點擊 "Build wrapper"
echo.
echo 方法 2: 使用 jpackage（如果可用）
echo   - 需要 JDK 14 或更高版本
echo   - 執行以下命令：
echo.
echo     jpackage --input target --name "OpenCV圖像檢視器" ^
echo       --main-jar opencv-image-viewer-1.0.0.jar ^
echo       --main-class com.imageviewer.Main ^
echo       --type exe --dest dist --win-shortcut --win-menu
echo.
echo ========================================
echo 詳細說明請查看: 使用Launch4j創建EXE.md
echo ========================================
echo.

REM 嘗試使用 jpackage（如果可用）
where jpackage >nul 2>&1
if %errorlevel% equ 0 (
    echo 檢測到 jpackage，嘗試創建 EXE...
    jpackage --input target --name "OpenCV圖像檢視器" --main-jar opencv-image-viewer-1.0.0.jar --main-class com.imageviewer.Main --type exe --dest dist --win-shortcut --win-menu --app-version 1.0.0
    if %errorlevel% equ 0 (
        echo.
        echo 成功！EXE 檔案已創建在 dist 目錄中
        dir dist\*.exe
    ) else (
        echo jpackage 執行失敗，請使用 Launch4j
    )
) else (
    echo jpackage 不可用，請使用 Launch4j
)

echo.
pause
