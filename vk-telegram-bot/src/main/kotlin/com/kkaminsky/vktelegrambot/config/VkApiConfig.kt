package com.kkaminsky.vktelegrambot.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * Класс конфигураций для бота
 */
@ConfigurationProperties(prefix = "app.vk")
class VkApiConfig {

    var accessToken = ""

    var groupId = 0

    var wait = 15

}