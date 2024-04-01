package com.codersgate.ticketraider

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@EnableJpaAuditing
@EnableScheduling
@EnableCaching
@SpringBootApplication
class TicketRaiderApplication

fun main(args: Array<String>) {
    runApplication<TicketRaiderApplication>(*args)
}
