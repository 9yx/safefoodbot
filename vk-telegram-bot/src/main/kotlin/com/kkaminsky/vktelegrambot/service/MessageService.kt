package com.kkaminsky.vktelegrambot.service

import com.kkaminsky.vktelegrambot.dto.MessageInDto
import com.kkaminsky.vktelegrambot.dto.MessageOutDto

/**
 * Интерфейс для получения входящих сообщения от пользователей
 */
interface MessageService {

    fun newMessageFromUser(dto: MessageInDto)

}