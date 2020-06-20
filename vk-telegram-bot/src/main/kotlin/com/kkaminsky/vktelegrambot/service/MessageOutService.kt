package com.kkaminsky.vktelegrambot.service

import com.kkaminsky.vktelegrambot.dto.MessageOutDto

interface MessageOutService {
    fun newMessageToUser(dto: MessageOutDto)
}