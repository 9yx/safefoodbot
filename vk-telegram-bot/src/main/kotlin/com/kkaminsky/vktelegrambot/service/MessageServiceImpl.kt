package com.kkaminsky.vktelegrambot.service

import com.kkaminsky.vktelegrambot.dto.MessageInDto
import com.kkaminsky.vktelegrambot.statemachine.event.BotEvent
import com.kkaminsky.vktelegrambot.statemachine.state.BotState
import org.springframework.statemachine.config.StateMachineFactory
import org.springframework.statemachine.persist.StateMachinePersister
import org.springframework.stereotype.Service


/**
 * Класс для обработки сообщений от пользователя, используется конечный автомат - Spring State Machine
 */
@Service
class MessageServiceImpl(
        private val persister: StateMachinePersister<BotState, BotEvent, String>,
        private val stateMachineFactory: StateMachineFactory<BotState,BotEvent>
):MessageService {

    override fun newMessageFromUser(dto: MessageInDto) {
        var flag = false
        val sm = stateMachineFactory.getStateMachine()

        sm.extendedState.variables["USER_ID"] = dto.userId;
        sm.extendedState.variables["MESSAGE_ID"] = dto.messageId
        sm.extendedState.variables["MESSAGE_TEXT"] = dto.messageText
        sm.extendedState.variables["MESSAGE_TIME"] = dto.messageTime

        try {
            persister.restore(sm, dto.userId)
        } catch (e: Exception) {
            e.printStackTrace()
            flag = true
        }
        if(flag){
            sm.sendEvent(BotEvent.GET_SOME_TEXT)
        }
        if(dto.messageText=="Назад"){
            sm.sendEvent(BotEvent.RETURNED)
        }
        if(dto.messageText=="Ок"){
            sm.sendEvent(BotEvent.BOT_OKED)
        }
        if(dto.messageText=="Не ок"){
            sm.sendEvent(BotEvent.BOT_DIDNT_OK)
        }
        if(dto.messageText in listOf("Адрес","Тип товара")){
            sm.sendEvent(BotEvent.GET_CRITIC)
        }
        if(dto.messageText == "Задать фильтр"){
            sm.sendEvent(BotEvent.START_TYPE_SEARCH)
        }
        if(dto.geoData!=null){
            sm.extendedState.variables["LAT"] = dto.geoData.coordinates.latitude.toString()
            sm.extendedState.variables["LNG"] = dto.geoData.coordinates.longitude.toString()
            sm.sendEvent(BotEvent.SENT_GEO)
        }
        if(dto.messageText == "Отобразить все на карте"){
            sm.sendEvent(BotEvent.NEEDED_MAP)
        }
        if(dto.messageText in listOf("Хлеб","Картошка","Чай")){
            when (dto.messageText) {
                "Хлеб" -> {
                    sm.extendedState.variables["SELECTED_TOPIC"] = "bread"
                }
                "Картошка" -> {
                    sm.extendedState.variables["SELECTED_TOPIC"] = "potato"
                }
                else -> {
                    sm.extendedState.variables["SELECTED_TOPIC"] = "tea"
                }
            }
            sm.sendEvent(BotEvent.SENT_TOPICS)
        }

        try {
            persister.persist(sm, dto.userId)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }


    }
}