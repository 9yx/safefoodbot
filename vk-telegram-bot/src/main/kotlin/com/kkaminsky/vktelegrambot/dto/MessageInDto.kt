package com.kkaminsky.vktelegrambot.dto

import com.vk.api.sdk.objects.base.Geo
import java.time.Instant


/**
 * Класс данных, принимаемых от пользователя
 */
data class MessageInDto(
        val userId: String,
        val messageId: String,
        val messageText: String,
        val messageTime: Instant,
        val geoData: Geo?
)