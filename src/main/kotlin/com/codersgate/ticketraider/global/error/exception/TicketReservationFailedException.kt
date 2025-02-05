package com.codersgate.ticketraider.global.error.exception

class TicketReservationFailedException(val msg: String) : RuntimeException(
    "Error : $msg"
)