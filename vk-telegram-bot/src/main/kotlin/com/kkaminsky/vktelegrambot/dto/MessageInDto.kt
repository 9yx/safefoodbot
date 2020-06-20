package com.kkaminsky.vktelegrambot.dto

import com.vk.api.sdk.objects.base.Geo
import java.time.Instant

data class MessageInDto(
        val userId: String,
        val messageId: String,
        val messageText: String,
        val messageTime: Instant,
        val geoData: Geo?
)