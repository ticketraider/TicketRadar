package com.codersgate.ticketraider.global.error.exception

data class TicketReservationFailedException(val msg:String) : RuntimeException(
    "Error : $msg"
)