package com.example.chatoverbluetooth.domain.chat

data class BTMsg(
    val msg: String,
    val senderName: String,
    val isFromLocalUser: Boolean
)