package com.kkaminsky.vktelegrambot.dto

import com.vk.api.sdk.objects.messages.KeyboardButtonActionType

data class MessageOutDto(
        val messageId: String,
        val userId: String,
        val text: String,
        val buttons: List<String>,
        val attachmnet: String = "",
        val superFlag: Boolean = false,
        val buttonsType: KeyboardButtonActionType = KeyboardButtonActionType.TEXT,
        val oneTimeButtons: List<String> = listOf()
)