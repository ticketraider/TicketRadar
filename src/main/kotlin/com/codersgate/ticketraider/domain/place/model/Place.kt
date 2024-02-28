package com.codersgate.ticketraider.domain.place.model

import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLDelete(sql = "UPDATE place SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
@Table(name = "place")
class Place(

    @Column(name = "name")
    var name: String,

    @Column(name = "totalSeat")
    var totalSeat: Int,

    @Column(name = "rSeat")
    var seatR: Int,

    @Column(name = "sSeat")
    var seatS: Int,

    @Column(name = "aSeat")
    var seatA: Int,

    ) : BaseEntity() {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}