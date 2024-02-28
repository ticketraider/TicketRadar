package com.codersgate.ticketraider.domain.ticket.repository

import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import org.springframework.data.jpa.repository.JpaRepository

interface TicketRepository: JpaRepository<Ticket, Long>, CustomTicketRepository {
}