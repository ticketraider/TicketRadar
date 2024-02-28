package com.codersgate.ticketraider.domain.event.model.seat

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDate

@Entity
@SQLDelete(sql = "UPDATE seat SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
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
    var seatR: Int,

    @Column(name = "sSeat")
    var seatS: Int,

    @Column(name = "aSeat")
    var seatA: Int,

    @Column(name = "totalSeat")
    var totalSeat: Int = seatR+seatS+seatA


    ): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}