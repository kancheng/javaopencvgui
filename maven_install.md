# Maven 安裝指南

## 檢測結果

- Java 已安裝（版本 24.0.2）
- Maven 

## 安裝 Maven 的方法

- https://maven.apache.org/install.html

### 方法一：使用 Chocolatey（推薦，Windows）

如果您已安裝 Chocolatey，執行：

```powershell
choco install maven
```

### 方法二：手動安裝 Maven

1. **下載 Maven**
   - 訪問：https://maven.apache.org/download.cgi
   - 下載 `apache-maven-3.9.x-bin.zip`（最新版本）

2. **解壓縮**
   - 解壓到 `C:\Program Files\Apache\maven` 或您選擇的目錄

3. **設定環境變數**
   - 右鍵「此電腦」→「內容」→「進階系統設定」→「環境變數」
   - 在「系統變數」中新增：
     - 變數名：`MAVEN_HOME`
     - 變數值：`C:\Program Files\Apache\maven`（您的 Maven 安裝路徑）
   - 編輯 `Path` 變數，新增：`%MAVEN_HOME%\bin`

4. **驗證安裝**
   - 重新開啟命令提示字元或 PowerShell
   - 執行：`mvn -version`

### 方法三：使用 IDE 內建的 Maven

如果您使用 IntelliJ IDEA 或 Eclipse，這些 IDE 通常內建 Maven，可以直接使用。

## 安裝完成後

執行以下命令驗證：

```powershell
mvn -version
```

應該會顯示類似以下的輸出：

```
Apache Maven 3.9.x
Maven home: C:\Program Files\Apache\maven
Java version: 24.0.2
...
```

## 快速安裝腳本（PowerShell）

如果您有管理員權限，可以執行以下 PowerShell 腳本自動安裝：

```powershell
# 下載並安裝 Maven（需要管理員權限）
$mavenVersion = "3.9.6"
$mavenUrl = "https://dlcdn.apache.org/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$installPath = "C:\Program Files\Apache\maven"

# 下載
Invoke-WebRequest -Uri $mavenUrl -OutFile "$env:TEMP\maven.zip"

# 解壓
Expand-Archive -Path "$env:TEMP\maven.zip" -DestinationPath "$env:TEMP\maven" -Force
Move-Item -Path "$env:TEMP\maven\apache-maven-$mavenVersion" -Destination $installPath -Force

# 設定環境變數（需要管理員權限）
[System.Environment]::SetEnvironmentVariable("MAVEN_HOME", $installPath, [System.EnvironmentVariableTarget]::Machine)
$path = [System.Environment]::GetEnvironmentVariable("Path", [System.EnvironmentVariableTarget]::Machine)
[System.Environment]::SetEnvironmentVariable("Path", "$path;$installPath\bin", [System.EnvironmentVariableTarget]::Machine)
```

## 安裝完成後繼續

安裝 Maven 後，請重新開啟命令提示字元或 PowerShell，然後執行：

```powershell
cd C:\Users\Foxconn\Downloads\javaopencvgui
mvn clean compile
mvn exec:java -Dexec.mainClass="com.imageviewer.Main"
```
