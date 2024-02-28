package com.codersgate.ticketraider.domain.event.model

enum class Bookable {
    OPEN,
    CLOSED;

    fun isBookable() : Boolean {
        return this == OPEN
    }
}