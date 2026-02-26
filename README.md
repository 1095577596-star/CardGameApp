# 牌类游戏助手 - CardGameApp

一个完整的安卓原生APP，支持多种牌类游戏分析和语音播报。

## 功能特性

### 支持的游戏
- **40张宝子**：对子 > 点数，同点庄家大
- **泸州大贰**：按顺序播报牌
- **金花**：豹子 > 顺金 > 金花 > 顺子 > 对子 > 单张
- **斗牛**：5张牌凑10倍数，剩余2张为牛
- **坎顺斗牛**：坎牌 > 顺牌 > 普通斗牛

### 核心功能
- 1-10人玩家设置
- 语音音量调节（0-100）
- 红外亮度调节（0-100）
- 多种打色模式
- 语音播报（中文）
- 锁屏不掉线（前台服务保活）
- 设置本地持久化
- 可扩展的游戏规则框架

### 播报格式
- 只报玩家位置：`3 1`
- 最大和次大牌相同时：`3 1 相同`

## 技术栈

- **纯Android原生API**，无第三方库依赖
- **最低SDK版本**: 21 (Android 5.0)
- **目标SDK版本**: 33 (Android 13)
- **开发语言**: Java

## 项目结构

```
CardGameApp/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/cardgame/
│   │       │   ├── MainActivity.java          # 主界面
│   │       │   ├── ForegroundService.java     # 前台保活服务
│   │       │   ├── GameAnalyzer.java          # 游戏分析引擎
│   │       │   ├── Card.java                  # 牌类
│   │       │   ├── PlayerHand.java            # 玩家手牌
│   │       │   ├── GameRule.java              # 游戏规则接口
│   │       │   ├── FortyCardRule.java         # 40张宝子规则
│   │       │   ├── LuZhouDaErRule.java        # 泸州大贰规则
│   │       │   ├── JinHuaRule.java             # 金花规则
│   │       │   ├── DouNiuRule.java            # 斗牛规则
│   │       │   └── KanShunDouNiuRule.java    # 坎顺斗牛规则
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml
│   │       │   └── values/
│   │       │       ├── strings.xml
│   │       │       ├── colors.xml
│   │       │       └── themes.xml
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md
```

## 使用说明

### 在Solo编程中运行
1. 将整个项目导入到Solo编程
2. 等待Gradle同步完成
3. 连接Android设备或启动模拟器
4. 点击运行按钮

### 基本操作
1. 选择游戏类型
2. 设置玩家人数（1-10人）
3. 调整语音音量和红外亮度
4. 选择打色模式
5. 点击"测试播报"按钮测试功能
6. 点击"开始分析"开始游戏分析

## 扩展新游戏

要添加新的游戏规则，只需：

1. 创建新的类实现 `GameRule` 接口
2. 在 `MainActivity.initGameRules()` 中添加新规则
3. 无需修改其他代码

```java
public class NewGameRule implements GameRule {
    @Override
    public String getName() { return "新游戏"; }

    @Override
    public int getCardsPerPlayer() { return 3; }

    @Override
    public Object calculateHandRank(List<Card> cards) {
        // 实现牌型计算逻辑
        return rank;
    }

    @Override
    public int compareHands(Object rank1, Object rank2) {
        // 实现牌型比较逻辑
        return comparison;
    }
}
```

## 权限说明

- `CAMERA`: 相机访问（可选）
- `RECORD_AUDIO`: 录音权限（可选）
- `VIBRATE`: 震动功能
- `FOREGROUND_SERVICE`: 前台服务保活
- `WAKE_LOCK`: 保持唤醒

## 注意事项

- 本项目使用模拟数据进行测试
- 实际使用时需要集成相机识别功能
- 语音播报依赖系统TTS引擎

## 开发者

本项目使用纯Android原生API开发，可在Solo编程中直接运行。
