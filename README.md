# Bluetooth Chat

A simple Android chat application that uses Bluetooth to connect with other devices and transfer chats. Built with Jetpack Compose, it follows the MVVM architecture for data flow.

## Screenshots


|                                                                                                                       |                                                                                                                           |
|-----------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------|
| ![Home Screen](https://github.com/user-attachments/assets/de1ff9a0-3e21-4937-8acb-19aed8bdc191) | ![Chatting Screen](https://github.com/user-attachments/assets/8a9ac5b1-638f-4891-a1a0-dfa7f3e445f5) |

## Features

- Scan for nearby Bluetooth devices.
- Pair with nearby devices.
- Start a chat with a paired device.

## How to Use

1. **Permissions:** Grant the required permissions when prompted at startup.
2. **Bluetooth Setup:** Open the app on both devices and ensure Bluetooth is turned on.
3. **Start Chat:**
   - On one device, tap **Start Server**.
   - On the other device, tap **Start Scan**:
     - If the device is already paired, it will appear under "Paired Devices". Tap it to start chatting.
     - If not paired, wait for the scan to complete. Once the device appears under "Scanned Devices," tap it to pair. After pairing, you'll be navigated to the chat screen.
4. **End Chat:** Tap the cross button in the top-right corner to close the chat on both devices.

## Built With

- [Jetpack Compose](https://developer.android.com/jetpack/compose): For building the UI.
- [Android Bluetooth API](https://developer.android.com/guide/topics/connectivity/bluetooth): For connecting devices and sharing data.
- [Kotlin Coroutines and Flow](https://developer.android.com/kotlin/flow): For asynchronous programming.
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android): For dependency injection.

## Architecture

- **Client-Server Architecture:** Used for chat data transfer between devices.
- **MVVM (Model-View-ViewModel):** Ensures separation of concerns and organized code structure.
- **UDF (Unidirectional Data Flow):** Provides predictable state management.

## Key Classes and Roles

- **[AndroidBTController](app/src/main/java/com/sdevprem/bluetoothchat/data/chat/AndroidBTController.kt):**
  - Retrieves paired devices.
  - Starts and stops scans for new Bluetooth devices.
  - Manages Bluetooth device connections and disconnections.
- **[BTDataTransferService](app/src/main/java/com/sdevprem/bluetoothchat/data/chat/BTDataTransferService.kt):**
  - Handles sending and receiving messages.
  - Listens for incoming messages.

---
