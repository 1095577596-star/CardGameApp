# GitHub Actions 自动构建APK指南

## 前提条件
你需要一个GitHub账号（免费注册）

## 步骤1：创建GitHub仓库

1. 访问 https://github.com
2. 登录或注册账号
3. 点击右上角的 **「+」** → **「New repository」**
4. 填写仓库名称（如：`CardGameApp`）
5. 选择 **「Public」** 或 **「Private」**
6. 点击 **「Create repository」**

## 步骤2：上传代码到GitHub

由于你的电脑没有Git，你可以：

### 方法A：直接在GitHub网页上传（最简单）

1. 在你的新仓库页面，点击 **「uploading an existing file」**
2. 打开文件夹：`c:\Users\Administrator\Documents\trae_projects\CardGameApp`
3. **全选所有文件和文件夹**（按 Ctrl+A）
4. 拖拽到GitHub网页的上传区域
5. 等待上传完成
6. 在底部填写提交信息（如：Initial commit）
7. 点击 **「Commit changes」**

### 方法B：下载GitHub Desktop（推荐用于长期使用）

1. 下载：https://desktop.github.com/
2. 安装并登录
3. 添加本地仓库
4. 提交并推送

## 步骤3：触发自动构建

代码上传后，GitHub Actions会自动开始构建！

1. 在仓库页面，点击顶部的 **「Actions」** 标签
2. 你会看到正在运行的工作流：**「Build Android APK」**
3. 点击进去查看构建进度
4. 等待约5-10分钟，构建完成！

## 步骤4：下载APK

构建成功后：

1. 在Actions工作流页面，滚动到底部
2. 在 **「Artifacts」** 区域，你会看到：
   - `app-debug-apk`（调试版，推荐安装）
   - `app-release-apk`（发布版，需要签名）
3. 点击 `app-debug-apk` 下载ZIP文件
4. 解压ZIP，得到 `app-debug.apk`
5. 复制到手机安装！

---

## 常见问题

### Q: 构建失败怎么办？
A:
- 在Actions页面查看详细日志
- 把错误信息发给我，我帮你修复

### Q: 构建太慢？
A:
- 首次构建需要下载依赖，较慢
- 后续构建会快很多（约3-5分钟）

### Q: 可以每次推送都自动构建吗？
A:
- 是的！每次push代码都会自动触发构建

---

## 其他方法

如果GitHub不方便，还有这些选择：

1. **安装Android Studio**（最稳定，约1GB下载）
2. **使用在线构建服务**（如AppVeyor、CircleCI）
3. **找朋友帮忙构建**

你想试试哪种？
