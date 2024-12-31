package com.example.chatoverbluetooth.data.chat

import com.example.chatoverbluetooth.domain.chat.BTMsg

fun String.toBTMsg(isFromLocalUser: Boolean): BTMsg {
    val name = substringBeforeLast("#")
    val message = substringAfter("#")
    return BTMsg(
        senderName = name,
        msg = message,
        isFromLocalUser = isFromLocalUser
    )
}

fun BTMsg.toByteArray(): ByteArray {
    return "$senderName#$msg".encodeToByteArray()
}