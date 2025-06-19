# 📰 KMP Assessment — Android Version

This repository contains the Android implementation for the KMP Assessment project. The app is a clean and functional **Kotlin Multiplatform (KMP)** news reader that fetches stories from an API, caches them locally, and provides a seamless offline experience — all built using **Jetpack Compose**, **Ktor**, and **SQLDelight**.

---

## 🚀 Project Overview

- **Platform**: Android (KMP-ready shared module)
- **Purpose**: Demonstrates a multiplatform architecture with Android-specific UI
- **Focus Areas**: Offline support, modern UI patterns, clean architecture, reactive state management

---

## ✅ Features Implemented

- ✅ **KMP Architecture**: Android app with shared logic in a KMP module
- ✅ **Pull-to-Refresh**: `SwipeRefresh` with Jetpack Compose
- ✅ **Offline Caching**: Local data persistence via SQLDelight
- ✅ **Dark Mode**: Full support for system UI theme
- ✅ **Banner when offload**: Notifies when the application is offline
- ✅ **Search Highlighting**: Emphasizes matching terms in headlines
- ✅ **Robust MVVM**: Clean separation of concerns with ViewModel + Repository

---

## 🧱 Tech Stack

| Layer           | Technology                      |
|-----------------|---------------------------------|
| UI              | Jetpack Compose                 |
| State Mgmt      | Kotlin Flow + ViewModel         |
| Network         | Ktor                            |
| Caching         | SQLDelight                      |
| KMP Sharing     | Kotlin Multiplatform (KMP)      |
| Design          | Material You (Compose)          |
| UX Enhancers    | Accompanist (for shimmer, swipe)|

---

## 🛠️ How to Build & Run (Android)

1.  **Clone the repository**:
    ```bash
    git clone [https://github.com/Enosh185/KMPAssessment-Android.git](https://github.com/Enosh185/KMPAssessment-Android.git)
    ```
2.  Open the project in Android Studio Hedgehog or later.
3.  Select the `androidApp` module and run it on an emulator or device.
4.  **Ensure your system has JDK 17 installed**.

### 🧪 iOS Integration (Not Included in Submission)

While the shared module (`:shared`) is set up for iOS (targets: `iosX64`, `iosArm64`, `iosSimulatorArm64`), the XCFramework generation and CocoaPods setup are not completed in this version. This submission focuses solely on a fully functional Android app.

### ⚙️ Offline Mode Behavior

* Stories are cached automatically when fetched.
* If the network is unavailable, the app displays stories from local storage.

### 📁 Project Structure
KMPAssessment/
├── androidApp/          # Android-specific code & UI
├── shared/              # Shared KMP module (network, cache, business logic)
└── iosApp/              # iOS (scaffold only, not part of submission)


---

## 👤 Author

**Enosh Mosuganti**
Mobile Developer
📧 enosh185@gmail.com
