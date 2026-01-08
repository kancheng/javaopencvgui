# Maven 編譯和執行教學

## 前置需求

在開始之前，請確保您的系統已安裝：
- **Java 11 或更高版本**
- **Maven 3.6 或更高版本**

### 檢查安裝

開啟命令提示字元（Windows）或終端機（Mac/Linux），執行以下命令檢查：

```bash
# 檢查 Java 版本
java -version

# 檢查 Maven 版本
mvn -version
```

如果命令無法執行，請先安裝 Java 和 Maven。

---

## Maven 基本命令

### 1. 清理專案

清除之前編譯產生的檔案（target 目錄）：

```bash
mvn clean
```

### 2. 編譯專案

編譯 Java 原始碼：

```bash
mvn compile
```

**說明：**
- 此命令會下載所需的依賴（OpenCV、Gson 等）
- 首次執行可能需要較長時間下載依賴
- 編譯後的 .class 檔案會存放在 `target/classes` 目錄

### 3. 打包專案

將專案打包成 JAR 檔案：

```bash
mvn package
```

**說明：**
- 會自動執行 `compile` 和 `test`（如果有測試）
- 生成的 JAR 檔案位於 `target/opencv-image-viewer-1.0.0.jar`
- 此 JAR 檔案包含所有依賴，可直接執行

### 4. 清理並編譯

同時執行清理和編譯：

```bash
mvn clean compile
```

### 5. 清理並打包

同時執行清理和打包：

```bash
mvn clean package
```

---

## 執行應用程序

### 方法一：使用 Maven 直接執行（推薦）

無需打包，直接執行：

```bash
mvn exec:java -Dexec.mainClass="com.imageviewer.Main"
```

**優點：**
- 不需要先打包
- 適合開發階段
- 自動處理依賴

### 方法二：執行打包後的 JAR 檔案

先打包，再執行：

```bash
# 步驟 1：打包
mvn clean package

# 步驟 2：執行 JAR
java -jar target/opencv-image-viewer-1.0.0.jar
```

**注意：**
- 如果使用此方法，需要確保所有依賴都已正確打包
- 可能需要使用 Maven Shade Plugin 或 Spring Boot 來創建包含所有依賴的 JAR

### 方法三：使用 java 命令執行編譯後的類別

```bash
# 步驟 1：編譯
mvn compile

# 步驟 2：執行（需要設定 classpath）
java -cp "target/classes;target/dependency/*" com.imageviewer.Main
```

---

## 完整執行流程範例

### 首次執行（完整流程）

```bash
# 1. 進入專案目錄
cd javaopencvgui

# 2. 清理舊檔案（可選）
mvn clean

# 3. 編譯專案（會自動下載依賴）
mvn compile

# 4. 執行應用程序
mvn exec:java -Dexec.mainClass="com.imageviewer.Main"
```

### 後續執行（快速流程）

如果已經編譯過，可以直接執行：

```bash
mvn exec:java -Dexec.mainClass="com.imageviewer.Main"
```

---

## 常見問題排除

### 問題 1：找不到 Maven 命令

**解決方案：**
- 確認 Maven 已正確安裝並加入系統 PATH
- Windows：檢查環境變數設定
- 重新開啟命令提示字元

### 問題 2：下載依賴失敗

**解決方案：**
- 檢查網路連線
- 確認可以訪問 Maven Central Repository
- 可能需要設定代理伺服器

### 問題 3：編譯錯誤

**解決方案：**
- 確認 Java 版本為 11 或更高
- 執行 `mvn clean` 後重新編譯
- 檢查 `pom.xml` 是否正確

### 問題 4：OpenCV 載入失敗

**解決方案：**
- 首次執行時，OpenCV 庫會自動下載
- 確保有足夠的磁碟空間
- 檢查網路連線是否正常

---

## Maven 生命週期階段

Maven 有以下主要生命週期階段：

1. **validate** - 驗證專案是否正確
2. **compile** - 編譯原始碼
3. **test** - 執行測試
4. **package** - 打包編譯後的程式碼
5. **install** - 安裝到本地 Maven 倉庫
6. **deploy** - 部署到遠端倉庫

### 常用組合命令

```bash
# 清理、編譯、測試、打包
mvn clean package

# 清理、編譯、測試、打包、安裝到本地倉庫
mvn clean install

# 跳過測試執行打包
mvn clean package -DskipTests
```

---

## 專案結構說明

編譯後，專案結構會變成：

```
javaopencvgui/
├── pom.xml
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── imageviewer/
│                   └── *.java
└── target/                    # 編譯後生成（執行 mvn compile 後出現）
    ├── classes/               # 編譯後的 .class 檔案
    │   └── com/
    │       └── imageviewer/
    │           └── *.class
    └── opencv-image-viewer-1.0.0.jar  # 打包後的 JAR（執行 mvn package 後出現）
```

---

## 下一步

編譯和執行成功後，您應該會看到：
- 應用程序視窗開啟
- 可以點擊「瀏覽目錄」選擇圖像目錄
- 開始使用圖像檢視和處理功能

祝使用愉快！
