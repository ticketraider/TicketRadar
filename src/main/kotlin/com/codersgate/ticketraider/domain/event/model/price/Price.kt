package com.codersgate.ticketraider.domain.event.model.price

import com.codersgate.ticketraider.domain.event.model.Event
import jakarta.persistence.*

@Entity
@Table(name = "price")
class Price (
    @OneToOne(mappedBy = "price", cascade = [CascadeType.ALL], orphanRemoval = true)
    @Column(name = "event_id")
    var event: Event? = null,

    @Column(name = "likeCount")
    var rPrice: Int,

    @Column(name = "averageRating")
    var sPrice: Int,

    @Column(name = "startDate")
    var aPrice: Int,

)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}