package com.kkaminsky.vktelegrambot.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kkaminsky.vktelegrambot.config.VkContext
import com.kkaminsky.vktelegrambot.dto.MessageInDto
import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll
import com.vk.api.sdk.objects.messages.Message
import org.springframework.stereotype.Service
import java.time.Instant


@Service
class VkBotImpl(
        private val vkContext: VkContext,
        private val messageService: MessageService
) : VkBot, CallbackApiLongPoll(vkContext.vk(), vkContext.actor(), vkContext.waits()) {

    override fun messageNew(groupId: Int?, message: Message?) {
        val gson: Gson = GsonBuilder().disableHtmlEscaping().enableComplexMapKeySerialization().create()

        println(message)
        messageService.newMessageFromUser(MessageInDto(
                message!!.getFromId().toString(),message.getId().toString(),message.getText(), Instant.now(), message.geo
        ))
    }
}