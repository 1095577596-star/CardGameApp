# APK构建完整指南

## 方法一：使用Android Studio构建（推荐）

### 前置准备
1. **下载并安装Android Studio**
   - 访问：https://developer.android.com/studio
   - 下载并安装最新版本

### 步骤1：打开项目
1. 启动Android Studio
2. 点击 **「Open an Existing Project」**（打开已有项目）
3. 选择文件夹：`c:\Users\Administrator\Documents\trae_projects\CardGameApp`
4. 点击 **「OK」**

### 步骤2：等待Gradle同步
- 项目打开后，底部会显示Gradle同步进度
- 首次同步需要下载依赖，可能需要5-10分钟
- 同步完成后，左侧会显示项目结构

### 步骤3：构建Debug APK
1. 点击顶部菜单：**「Build」** → **「Build Bundle(s) / APK(s)」** → **「Build APK(s)」**
2. 等待构建完成（右下角会显示进度）
3. 构建完成后，会弹出通知：**「APK(s) generated successfully」**
4. 点击通知中的 **「locate」** 链接，即可找到APK文件

### APK文件位置
```
CardGameApp\app\build\outputs\apk\debug\app-debug.apk
```

### 步骤4：安装到手机
1. 用USB连接手机
2. 在手机上开启 **「USB调试」** 模式
3. 把 `app-debug.apk` 复制到手机
4. 在手机文件管理器中找到APK，点击安装

---

## 方法二：使用命令行构建（需要先配置环境）

如果你已经配置了Android SDK和Java环境：

```bash
cd CardGameApp
gradlew.bat assembleDebug
```

APK位置同上。

---

## 常见问题

### Q: Gradle同步失败怎么办？
A:
- 检查网络连接（需要下载依赖）
- 点击 **「File」** → **「Settings」** → **「Build, Execution, Deployment」** → **「Gradle」**
- 尝试切换Gradle JDK

### Q: 构建失败提示SDK缺失？
A:
- 打开 **「Tools」** → **「SDK Manager」**
- 安装 Android 13 (API 33) SDK Platform
- 安装 Android SDK Build-Tools 33.0.0

### Q: 手机安装提示"未知来源"？
A:
- 在手机设置中允许安装未知来源应用
- 或在安装时点击「设置」→ 允许

---

## 项目已包含的功能

✅ 5种游戏规则
✅ 1-10人设置
✅ 语音播报
✅ 音量/亮度调节
✅ 锁屏不掉线
✅ 设置保存

---

## 需要帮助？

如果构建过程中遇到问题，请告诉我具体的错误信息！
