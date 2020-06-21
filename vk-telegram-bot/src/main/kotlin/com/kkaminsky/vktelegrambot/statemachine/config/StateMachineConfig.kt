package com.kkaminsky.vktelegrambot.statemachine.config

import com.kkaminsky.vktelegrambot.dto.MessageOutDto
import com.kkaminsky.vktelegrambot.entity.DataEntity
import com.kkaminsky.vktelegrambot.repository.DataRepository
import com.kkaminsky.vktelegrambot.service.DistanceCounter
import com.kkaminsky.vktelegrambot.service.MessageOutService
import com.kkaminsky.vktelegrambot.statemachine.event.BotEvent
import com.kkaminsky.vktelegrambot.statemachine.listener.BotStateMachineListener
import com.kkaminsky.vktelegrambot.statemachine.state.BotState
import com.vk.api.sdk.objects.messages.KeyboardButtonActionType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.StateMachineContext
import org.springframework.statemachine.StateMachinePersist
import org.springframework.statemachine.action.Action
import org.springframework.statemachine.config.EnableStateMachineFactory
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer
import org.springframework.statemachine.persist.DefaultStateMachinePersister
import org.springframework.statemachine.persist.StateMachinePersister
import java.util.*

/**
 * Класс для конфигурации конченого автомата. Здесь описаны все переходы между состояниями
 */
@Configuration
@EnableStateMachineFactory
class StateMachineConfig(
        private val vkMessageServiceImpl: MessageOutService,
        private val dataRepository: DataRepository,
        private val distanceCounter: DistanceCounter
) : EnumStateMachineConfigurerAdapter<BotState, BotEvent>() {


    override fun configure(config: StateMachineConfigurationConfigurer<BotState, BotEvent>) {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(BotStateMachineListener())
    }



    override fun configure(states: StateMachineStateConfigurer<BotState, BotEvent>) {
        states.withStates()
                .initial(BotState.UNDEFINED)
                .states(EnumSet.allOf(BotState::class.java))
    }

    @Throws(Exception::class)
    override fun configure(transitions: StateMachineTransitionConfigurer<BotState, BotEvent>) {
        transitions
                .withExternal()
                .source(BotState.UNDEFINED)
                .target(BotState.GREETING)
                .event(BotEvent.GET_SOME_TEXT)
                .action(showZadatButton())//Получили первое сообщение
                .and()
                .withExternal()
                .source(BotState.SHOWING_SEARCH)
                .target(BotState.SENDING_GEO)
                .event(BotEvent.BOT_OKED)//Получили ок
                .action(actionForOk())
                .action(youReturnedNotification())
                .action(answerForGeo())
                .and()
                .withExternal()
                .source(BotState.SHOWING_SEARCH)
                .event(BotEvent.BOT_DIDNT_OK)
                .target(BotState.BAD_CHOICE)//Получили Не ок
                .action(actionForDidntOk())
                .and()
                .withExternal()
                .source(BotState.BAD_CHOICE)
                .target(BotState.SENDING_GEO)
                .event(BotEvent.GET_CRITIC)
                .action(showApplyAnswer())
                .action(youReturnedNotification())
                .action(answerForGeo())
                .and()
                .withExternal()
                .source(BotState.GREETING)
                .target(BotState.SENDING_GEO)
                .event(BotEvent.START_TYPE_SEARCH)
                .action(answerForGeo())//Посылаем сообщение о необходимсоти ввода гео метки
                .and()
                .withExternal()
                .source(BotState.SENDING_GEO)
                .target(BotState.CHOOSING_TOPICS)
                .event(BotEvent.SENT_GEO)
                .action(answerForTopic())//Даем на выбор темы поиска
                .and()
                .withExternal()
                .source(BotState.CHOOSING_TOPICS)
                .target(BotState.SHOWING_SEARCH)
                .event(BotEvent.SENT_TOPICS)
                .action(showRecultOfSearch())//Показываем результаты поиска
                .and()
                .withExternal()
                .source(BotState.SHOWING_SEARCH)
                .target(BotState.SENDING_GEO)
                .event(BotEvent.NEEDED_MAP)
                .action(showMap())
                .action(youReturnedNotification())
                .action(answerForGeo())
                .and()
                .withExternal()
                .source(BotState.SHOWING_SEARCH)
                .target(BotState.SENDING_GEO)
                .event(BotEvent.RETURNED)
                .action(youReturnedNotification())
                .action(answerForGeo())
    }

    @Bean
    fun answerForGeo(): Action<BotState, BotEvent>{
        return Action {
            val userId = it.extendedState.get("USER_ID",String::class.java)
            print(userId)
            vkMessageServiceImpl.newMessageToUser(MessageOutDto(
                    userId = userId,
                    messageId = "sdfs",
                    text = "Отправьте геоточку",
                    buttons = listOf("Метка"),
                    buttonsType = KeyboardButtonActionType.LOCATION
            ))
        }
    }

    @Bean
    fun actionForOk(): Action<BotState, BotEvent>{
        return Action {
            val userId = it.extendedState.get("USER_ID",String::class.java)
            vkMessageServiceImpl.newMessageToUser(MessageOutDto(
                    userId = userId,
                    messageId = "sdfs",
                    text = "Отправьте геоточку",
                    buttons = listOf("Метка")
            ))
        }
    }

    @Bean
    fun showZadatButton(): Action<BotState, BotEvent>{
        return Action {
            val userId = it.extendedState.get("USER_ID",String::class.java)
            vkMessageServiceImpl.newMessageToUser(MessageOutDto(
                    userId = userId,
                    messageId = "sdfs",
                    text = "Задайте фильтры",
                    buttons = listOf("Задать фильтр")
            ))
        }
    }

    @Bean
    fun showApplyAnswer(): Action<BotState,BotEvent>{
        return Action {
            val userId = it.extendedState.get("USER_ID",String::class.java)
            vkMessageServiceImpl.newMessageToUser(MessageOutDto(
                    userId = userId,
                    messageId = "sdfs",
                    text = "Принято",
                    buttons = listOf()
            ))
        }
    }
    @Bean
    fun answerForTopic(): Action<BotState, BotEvent>{
        return Action {
            val userId = it.extendedState.get("USER_ID",String::class.java)
            vkMessageServiceImpl.newMessageToUser(MessageOutDto(
                    userId = userId,
                    messageId = "sdfs",
                    text = "Выберите тему",
                    buttons = listOf("Хлеб","Картошка","Чай")
            ))
        }
    }

    @Bean
    fun actionForDidntOk(): Action<BotState,BotEvent>{
        return Action {
            val userId = it.extendedState.get("USER_ID",String::class.java)
            vkMessageServiceImpl.newMessageToUser(MessageOutDto(
                    userId = userId,
                    messageId = "sdfs",
                    text = "Что не так?",
                    buttons = listOf("Адрес","Тип товара")
            ))
        }
    }

    @Bean
    fun showMap(): Action<BotState,BotEvent>{
        return Action {
            val userId = it.extendedState.get("USER_ID",String::class.java)
            val mapUrl = it.extendedState.get("MAP_PATH",String::class.java)
            vkMessageServiceImpl.newMessageToUser(MessageOutDto(
                    userId = userId,
                    messageId = "sdfs",
                    text = mapUrl,
                    buttons = listOf()
            ))
        }
    }

    @Bean
    fun showRecultOfSearch(): Action<BotState,BotEvent>{

        return Action {
            val topic = it.extendedState.get("SELECTED_TOPIC",String::class.java)
            val userId = it.extendedState.get("USER_ID",String::class.java)
            val lat = it.extendedState.get("LAT",String::class.java)
            val lng = it.extendedState.get("LNG",String::class.java)
            var dataEntites = mutableListOf<DataEntity>()
            if(topic=="bread") {
                dataEntites = dataRepository.getTopBreadsPost().toMutableList()
            }
            else if (topic=="potato"){
                dataEntites = dataRepository.getTopPotatosPost().toMutableList()
            }
            else {
                dataEntites = dataRepository.getTopByTea().toMutableList()
            }

            val dataEntities = dataEntites.filter { it.lat!=null && it.lng!=null }.map {
                it to distanceCounter.getDistanceBeetwenTwoPoints(lat.toFloat(),it.lat!!.toFloat(),lng.toFloat(),it.lng!!.toFloat(),0.0.toFloat(),0.0.toFloat())
            }.filter { it.second < 40000 }

            dataEntities.sortedBy { it.second }.take(5)

            var str: String = ""
            var firstCoords = ""
            for (e in dataEntities.map { it.first }){
                vkMessageServiceImpl.newMessageToUser(MessageOutDto(
                        userId = userId,
                        messageId = "sdfs",
                        text = (e.location?:"") + "\n" + "https://yandex.ru/maps/?ll=${e.lng},${e.lat}&z=6.49",
                        buttons = listOf(),
                        attachmnet = "wall"+e.groupId+"_"+e.postId,
                        oneTimeButtons = listOf("Ок","Не ок")
                ))
                str += e.lng+"," + e.lat + "~"
                firstCoords = e.lng + "," + e.lat
            }
            if (str!=""){
                str.substring(0, str.count()- 1);
            }
            it.extendedState.variables["MAP_PATH"] = "https://yandex.ru/maps/?ll=$firstCoords&pt=$str&z=12&l=map"
            vkMessageServiceImpl.newMessageToUser(MessageOutDto(
                    userId = userId,
                    messageId = "sdfs",
                    text = "Дополнительные опции",
                    buttons = listOf("Назад","Отобразить все на карте")
                    ))

        }
    }

    @Bean
    fun youReturnedNotification(): Action<BotState,BotEvent>{
        return Action {
            val userId = it.extendedState.get("USER_ID",String::class.java)
            vkMessageServiceImpl.newMessageToUser(MessageOutDto(
                    userId = userId,
                    messageId = "sdfs",
                    text = "Вы вернулись в начало",
                    buttons = listOf()
            ))
        }
    }

    class BotStateMachinePersister: StateMachinePersist<BotState, BotEvent, String> {
        private val contexts: HashMap<String, StateMachineContext<BotState, BotEvent>> = HashMap()

        override fun write(context: StateMachineContext<BotState, BotEvent>, contextObj: String) {
            contexts.put(contextObj, context);
        }

        override fun read(contextObj: String): StateMachineContext<BotState, BotEvent> {
            return contexts.get(contextObj)!!;
        }
    }

    @Bean
    fun persister(): StateMachinePersister<BotState, BotEvent, String> {
        return DefaultStateMachinePersister(BotStateMachinePersister())
    }


}