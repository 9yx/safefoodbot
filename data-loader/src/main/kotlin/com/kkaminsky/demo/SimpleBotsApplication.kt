package com.kkaminsky.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimpleBotsApplication

fun main(args: Array<String>) {
	runApplication<SimpleBotsApplication>(*args)
}
