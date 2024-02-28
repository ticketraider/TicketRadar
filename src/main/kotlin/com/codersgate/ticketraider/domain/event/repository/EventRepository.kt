package com.codersgate.ticketraider.domain.event.repository

import com.codersgate.ticketraider.domain.event.model.Event
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository : JpaRepository<Event, Long> {
//    fun findByPageable(pageable: Pageable): Page<Event>

}