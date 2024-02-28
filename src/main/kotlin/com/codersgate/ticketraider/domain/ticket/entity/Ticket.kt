package com.codersgate.ticketraider.domain.ticket.entity

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.member.entity.Member
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.util.Date

@Table(name = "tickets")
@SQLDelete(sql = "UPDATE category SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
@Entity
class Ticket(
    @Column(name = "date")
    val date: Date,

    @Enumerated(EnumType.STRING)
    @Column(name = "grade", nullable = false)
    var grade: TicketGrade,

    @Column(name = "price", nullable = false)
    val price: Int,

    @Column(name = "seatNo", nullable = false)
    val seatNo: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    val event: Event,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}