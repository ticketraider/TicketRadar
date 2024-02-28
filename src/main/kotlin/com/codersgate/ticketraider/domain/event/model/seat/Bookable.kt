package com.codersgate.ticketraider.domain.event.model.seat

enum class Bookable {
    OPEN,
    CLOSED;

    fun isBookable() : Boolean {
        return this == OPEN
    }
}