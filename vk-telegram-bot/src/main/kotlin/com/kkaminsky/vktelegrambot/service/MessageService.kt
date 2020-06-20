package com.kkaminsky.vktelegrambot.service

import com.kkaminsky.vktelegrambot.dto.MessageInDto
import com.kkaminsky.vktelegrambot.dto.MessageOutDto

interface MessageService {

    fun newMessageFromUser(dto: MessageInDto)

}