package com.codersgate.ticketraider.domain.event.model.price

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLDelete(sql = "UPDATE price SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
@Table(name = "price")
class Price (
    @OneToOne()
    @JoinColumn(name = "event_id")
    var event: Event? = null,

    @Column(name = "r_price")
    var seatRPrice: Int,

    @Column(name = "s_price")
    var seatSPrice: Int,

    @Column(name = "a_price")
    var seatAPrice: Int,

): BaseEntity(){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}