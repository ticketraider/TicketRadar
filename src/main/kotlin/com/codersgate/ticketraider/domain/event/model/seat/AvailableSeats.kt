package com.codersgate.ticketraider.domain.event.model.seat

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDate

@Entity
@SQLDelete(sql = "UPDATE available_seats SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
@Table(name = "available_seats")
class AvailableSeat(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    val event: Event? = null,

    @Column(name = "date")
    var date: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(name = "bookable")
    var bookable: Bookable = Bookable.OPEN,

    @Column(name = "seat_r")
    var seatR: Int,

    @Column(name = "seat_s")
    var seatS: Int,

    @Column(name = "seat_a")
    var seatA: Int,

    @Column(name = "total_seat")
    var totalSeat: Int


) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    fun isFull(): Boolean {
        return (seatR <= 0 && seatS <= 0 && seatA <= 0)
    }
    fun isClosed(): Boolean {
        return bookable == Bookable.CLOSED
    }
    fun close() {
        bookable = Bookable.CLOSED
    }
    fun decreaseSeat(grade: TicketGrade, count: Int) {
        when(grade){
            TicketGrade.R -> seatR -= count
            TicketGrade.S -> seatS -= count
            TicketGrade.A -> seatA -= count
        }
    }
    fun increaseSeat(grade: TicketGrade, count: Int) {
        when(grade){
            TicketGrade.R -> seatR += count
            TicketGrade.S -> seatS += count
            TicketGrade.A -> seatA += count
        }
    }
}