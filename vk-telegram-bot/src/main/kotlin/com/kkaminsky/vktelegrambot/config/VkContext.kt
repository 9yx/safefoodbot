package com.kkaminsky.vktelegrambot.config

import com.vk.api.sdk.client.TransportClient
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * Класс конфигураций для бота
 */
@Configuration
@EnableConfigurationProperties(VkApiConfig::class)
class VkContext (
        private val props: VkApiConfig
){

    @Bean
    fun vk(): VkApiClient {
        val httpClient: TransportClient = HttpTransportClient()
        return VkApiClient(httpClient)
    }

    @Bean
    fun actor(): GroupActor? {
        return GroupActor(groupId(), props.accessToken)
    }

    @Bean
    fun waits(): Int {
        return props.wait
    }

    @Bean
    fun groupId(): Int {
        return props.groupId
    }

}