package com.codersgate.ticketraider.domain.event.model

import jakarta.persistence.*

@Entity
@Table(name = "event")
class Event (
    @Column(name = "title")
    var title: String,

    @Column(name = "posterImage")
    var posterImage: String,

    @Column(name = "likeCount")
    var likeCount: Int = 0,

    @Column(name = "averageRating")
    var averageRating: Float = 0F,

    @Column(name = "startDate")
    var startDate: String,

    @Column(name = "endDate")
    var endDate: String,

//    @Enumerated(EnumType.STRING)
//    @Column(name = "bookable")
//    var bookable: Bookable = Bookable.OPEN,

    @Column(name = "eventInfo")
    var eventInfo: String,

    // ERD 변경으로 인해 미사용
//    @Column(name = "location")
//    var location: String,
//    @Column(name = "price")
//    var price: Int,
)
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}





