package com.codersgate.ticketraider.domain.event.model.seat

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "seat")
class Seat(
    @ManyToOne
    @JoinColumn(name = "event_id")
    val event: Event? = null,

    @Column(name = "date")
    var date: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(name = "bookable")
    var bookable: Bookable = Bookable.OPEN,

    @Column(name = "rSeat")
    var rSeat: Int,

    @Column(name = "sSeat")
    var sSeat: Int,

    @Column(name = "aSeat")
    var aSeat: Int,

    @Column(name = "totalSeat")
    var totalSeat: Int = rSeat+sSeat+aSeat


    ): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}