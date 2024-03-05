package com.codersgate.ticketraider.domain.event.model.price

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLDelete(sql = "UPDATE prices SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
@Table(name = "prices")
class Price (
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    var event: Event,

    @Column(name = "seat_r_price")
    var seatRPrice: Int,

    @Column(name = "seat_s_price")
    var seatSPrice: Int,

    @Column(name = "seat_a_price")
    var seatAPrice: Int,

): BaseEntity(){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}