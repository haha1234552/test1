# Android 计算器 App

一个兼容 **Android 10+ (API 29+)** 的简单计算器应用，支持加减乘除、小数、清空、删除和表达式计算。

## 环境要求

- JDK 17+
- Android SDK（建议安装 API 35 Platform + Build Tools）
- Gradle 8.14+

## 构建 APK

```bash
gradle assembleDebug
```

成功后 APK 路径：

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 功能

- 数字输入（0-9）
- 小数点输入
- 运算符：`+`、`-`、`×`、`÷`
- 清空（C）和退格（⌫）
- 一键求值（=）
- 除以 0 和非法输入显示 `Error`


## HTML 版本（快速预览）

已新增纯前端 HTML 版本：`web/index.html`。

本地启动：

```bash
python -m http.server 8000
```

浏览器打开：

```text
http://localhost:8000/web/index.html
```
