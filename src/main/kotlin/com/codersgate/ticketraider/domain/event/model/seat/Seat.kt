package com.codersgate.ticketraider.domain.event.model.seat

import jakarta.persistence.*

@Entity
@Table(name = "seat")
class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}