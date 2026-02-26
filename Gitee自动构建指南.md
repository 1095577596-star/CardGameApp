# Gitee 自动构建APK指南（国内推荐）

## 优势
✅ 国内访问速度快
✅ 免费使用
✅ 操作和GitHub类似

---

## 步骤1：注册/登录Gitee

1. 访问：https://gitee.com
2. 点击右上角 **「注册」** 或 **「登录」**
3. 用手机号注册（免费）

## 步骤2：创建仓库

1. 登录后，点击右上角的 **「+」** 号
2. 选择 **「新建仓库」**
3. 填写：
   - **仓库名称**：`CardGameApp`
   - **是否公开**：选择「公开」或「私有」都可以
   - 其他保持默认
4. 点击 **「创建」**

## 步骤3：上传文件

### 方法：网页直接上传（最简单）

1. 创建仓库后，点击 **「文件」** 标签
2. 点击 **「上传文件」** 按钮
3. 打开文件夹：`c:\Users\Administrator\Documents\trae_projects\CardGameApp`
4. **按 Ctrl+A 全选所有文件和文件夹**
5. **拖拽到Gitee的上传区域**
6. 等待上传完成
7. 在底部填写提交信息：`Initial commit`
8. 点击 **「提交」**

## 步骤4：开启Gitee Go构建服务

1. 在仓库页面，点击顶部的 **「服务」** → **「Gitee Go」**
2. 如果是首次使用，点击 **「立即开通」**（免费）
3. 点击 **「新建流水线」**
4. 选择 **「Java」** 模板，或者选择 **「空白模板」**
5. 我帮你配置好的工作流文件已经在 `.github/workflows/build.yml`，可以稍作修改用于Gitee

---

## 📋 或者，直接使用方案B（更简单）

## 方案B：安装Android Studio（推荐，一劳永逸）

### 下载地址
- 官方下载（可能慢）：https://developer.android.com/studio
- 国内镜像下载（推荐）：
  - 清华镜像：https://mirrors.tuna.tsinghua.edu.cn/AndroidStudio/
  - 或者搜索「Android Studio 国内下载」

### 安装步骤
1. 下载后双击安装包
2. 一路点击「Next」（下一步）
3. 安装完成后启动Android Studio
4. 首次启动会下载SDK，等待完成
5. 打开项目：`File` → `Open` → 选择 `CardGameApp` 文件夹
6. 构建APK：`Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`

---

## 💡 你想选哪个？

1. **Gitee方案** - 需要注册，但不用下载大文件
2. **Android Studio方案** - 下载约1GB，但以后开发更方便

或者，你电脑上有没有其他人帮忙安装过Android Studio？
