# S905X TV Launcher

A lightweight, remote-friendly Android TV home launcher for Android 7.0+ (API 24+) devices, including Amlogic S905X boxes.

## Features

- Set as the device's Home app
- TV-friendly app grid with D-pad focus navigation
- Launches installed apps
- Lightweight: no third-party runtime dependencies
- GitHub Actions builds a debug APK

## Build

Push to GitHub or run the **Android APK** workflow from the Actions tab. The workflow uploads `app-debug.apk` as an artifact.

For a local build with JDK 11 and Gradle 7.6.4:

```sh
gradle assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## Install

Install the APK with ADB:

```sh
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Then press Home and choose **S905X TV Launcher**.

## Compatibility

- Minimum Android version: Android 7.0 (API 24)
- Target SDK: 28
- Designed for Android TV / Amlogic S905X devices

This is an independent launcher, not Google's Google TV launcher and does not include Google TV services or content recommendations.
