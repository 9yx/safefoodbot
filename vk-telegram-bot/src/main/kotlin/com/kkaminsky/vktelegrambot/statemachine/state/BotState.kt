package com.kkaminsky.vktelegrambot.statemachine.state

enum class BotState(val text: String) {
    UNDEFINED(""),
    GREETING("Начать"),
    SENDING_GEO(""),
    CHOOSING_TOPICS(""),
    SHOWING_SEARCH(""),
    BAD_CHOICE(""),
    APPLY_BAD_CHOICE("")

}