package com.kkaminsky.vktelegrambot.service

import com.kkaminsky.vktelegrambot.config.VkContext
import com.kkaminsky.vktelegrambot.dto.MessageOutDto
import com.vk.api.sdk.objects.messages.*
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


@Service
class MessageOutServiceImpl(
        private val vk: VkContext
): MessageOutService{

    override fun newMessageToUser(dto: MessageOutDto) {
        if(dto.buttons.count()>0){
            val buttons = mutableListOf<List<KeyboardButton>>()
            dto.buttons.forEach {
                val keyBoard = KeyboardButton()
                val keyBoardAction = KeyboardButtonAction()
                if(dto.buttonsType != KeyboardButtonActionType.LOCATION)
                    keyBoardAction.label = it
                keyBoardAction.type = dto.buttonsType
                keyBoard.action =  keyBoardAction
                buttons.add(listOf(keyBoard))
            }
            val dynamicKeyboard = Keyboard().apply {
                this.buttons = buttons
                this.inline = false
                this.oneTime = true
            }
            vk.vk().messages().send(vk.actor()).message(dto.text).keyboard(dynamicKeyboard)
                    .userId(Integer.parseInt(dto.userId))
                    .randomId(Random.nextInt(Int.MAX_VALUE))
                    .execute();

        } else if (dto.oneTimeButtons.count()>0){
            val buttons = mutableListOf<List<KeyboardButton>>()
            dto.oneTimeButtons.toList().forEach {
                val keyBoard = KeyboardButton()
                val keyBoardAction = KeyboardButtonAction()
                keyBoardAction.type = dto.buttonsType
                if(keyBoardAction.type == KeyboardButtonActionType.TEXT){
                    keyBoardAction.label = it
                }
                keyBoard.action =  keyBoardAction

                buttons.add(listOf(keyBoard).toList())
            }
            val dynamicKeyboard = Keyboard()
            dynamicKeyboard.buttons = buttons
            dynamicKeyboard.inline = true
            vk.vk().messages().send(vk.actor()).message(dto.text).keyboard(dynamicKeyboard)
                    .userId(Integer.parseInt(dto.userId))
                    .attachment(dto.attachmnet)
                    .randomId(Random.nextInt(Int.MAX_VALUE))
                    .execute();
        }
        else{
            vk.vk().messages().send(vk.actor()).message(dto.text)
                    .userId(Integer.parseInt(dto.userId))
                    .randomId(Random.nextInt(Int.MAX_VALUE))
                    .execute();
        }


    }
}