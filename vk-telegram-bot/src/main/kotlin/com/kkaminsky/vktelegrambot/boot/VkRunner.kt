package com.kkaminsky.vktelegrambot.boot

import com.kkaminsky.vktelegrambot.service.VkBot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import java.util.*


/**
 * Класс для запуска вк бота
 */
@Component
class VkRunner(
        private val bot: VkBot
) : CommandLineRunner {



    private fun startVkBot() {
        try {
            bot.run()
        } catch (e: Exception) {
            println("Crushed: Error " + e.message)
        }
    }

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        val t = ThreadPoolTaskScheduler()
        t.poolSize = 10
        t.initialize()
        t.schedule({ startVkBot() }, Date())
    }
}
