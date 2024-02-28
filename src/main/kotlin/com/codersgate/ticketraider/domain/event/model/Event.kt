package com.codersgate.ticketraider.domain.event.model

import com.codersgate.ticketraider.domain.category.model.Category
import com.codersgate.ticketraider.domain.event.model.price.Price
import com.codersgate.ticketraider.domain.event.model.seat.Seat
import com.codersgate.ticketraider.domain.place.model.Place
import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLDelete(sql = "UPDATE event SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
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

    @Column(name = "eventInfo")
    var eventInfo: String,

    // ERD 변경으로 인해 미사용
//    @Column(name = "location")
//    var location: String,
    @ManyToOne
    @JoinColumn(name = "place_id")
    val place: Place,

    @ManyToOne
    @JoinColumn(name = "category_id")
    val category: Category,

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    val price: MutableList<Price> = mutableListOf(),

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    val seat: MutableList<Seat?> = mutableListOf()
) : BaseEntity(){
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}





