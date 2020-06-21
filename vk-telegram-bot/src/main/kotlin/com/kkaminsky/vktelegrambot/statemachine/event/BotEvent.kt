package com.kkaminsky.vktelegrambot.statemachine.event

enum class BotEvent {
    START_TYPE_SEARCH,
    BOT_OKED,
    BOT_DIDNT_OK,
    SENT_GEO,
    SENT_TOPICS,
    UPDATE_TOPICS,
    GET_SOME_TEXT,
    GET_CRITIC,
    RETURNED,
    NEEDED_MAP
}