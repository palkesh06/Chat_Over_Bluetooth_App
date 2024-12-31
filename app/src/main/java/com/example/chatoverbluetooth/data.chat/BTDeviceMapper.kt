package com.example.chatoverbluetooth.data.chat

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.chatoverbluetooth.domain.chat.BTDevice

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBTDevice() = BTDevice(
    name = this.name,
    address = this.address
)