# cmp-gobang

English | [中文介绍](https://github.com/vitaviva/cmp-gobang/blob/main/README_zh.md)


基于 Compose Multiplatform 的联机五子棋，实现跨端对弈

<img src="/screenshot/screenshot.gif" width="800">

# How to run

1. 配置 host 地址

在 string.xml 的 `<server_host/>` 中添加桌面端的 IP 地址

2. 运行桌面端

打开 Gradle 面板，点击 `desktop/Tasks/compose desktop/run`

3. 运行移动端

点击 `android/Tasks/install/installDebug`，安装 android 应用

4. 建立通信

- 在桌面端点击 [Pair] ，等待出现 "waiting pair..." 文字提示,
- 手机端点击 [Pair] 等待桌面端提示变为 "paired successfully"

5. 连接成功，开始对战！