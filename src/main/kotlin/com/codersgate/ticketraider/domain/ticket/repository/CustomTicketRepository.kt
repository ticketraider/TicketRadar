package com.codersgate.ticketraider.domain.ticket.repository

import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomTicketRepository {

    fun findByUserId(pageable: Pageable, userId: Long): Page<Ticket>
}