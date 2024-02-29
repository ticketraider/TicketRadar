package com.codersgate.ticketraider.domain.place.model

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.event.model.price.Price
import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLDelete(sql = "UPDATE places SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
@Table(name = "places")
class Place(

    @Column(name = "name")
    var name: String,

    @Column(name = "totalSeat")
    var totalSeat: Int,

    @Column(name = "seat_r")
    var seatR: Int,

    @Column(name = "seat_s")
    var seatS: Int,

    @Column(name = "seat_a")
    var seatA: Int,

    @OneToMany(mappedBy = "place", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var eventList: MutableList<Event> = mutableListOf()

    ) : BaseEntity() {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}