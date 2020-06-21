package com.kkaminsky.vktelegrambot.service

import com.kkaminsky.vktelegrambot.dto.MessageOutDto

/**
 * Интерфейс для отправки сообщений пользователю
 */
interface MessageOutService {
    fun newMessageToUser(dto: MessageOutDto)
}