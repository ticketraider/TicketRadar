package com.codersgate.ticketraider.domain.event.model

import com.codersgate.ticketraider.domain.category.model.Category
import com.codersgate.ticketraider.domain.event.model.price.Price
import com.codersgate.ticketraider.domain.event.model.seat.AvailableSeat
import com.codersgate.ticketraider.domain.place.model.Place
import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDate

@Entity
@SQLDelete(sql = "UPDATE events SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
@Table(
    name = "events", indexes = [
        Index(name = "idx_title", columnList = "title"), // 인덱스 설정
        Index(name = "idx_id", columnList = "id")
    ]
)
class Event(
    @Column(name = "title")
    var title: String,

    @Column(name = "poster_image")
    var posterImage: String,

    @Column(name = "like_count")
    var likeCount: Int = 0,

    @Column(name = "review_count")
    var reviewCount: Int = 0,

    @Column(name = "average_rating")
    var averageRating: Float = 0F,

    @Column(name = "start_date")
    var startDate: LocalDate,

    @Column(name = "end_date")
    var endDate: LocalDate,

    @Column(name = "event_info")
    var eventInfo: String,

    @ManyToOne
    @JoinColumn(name = "place_id")
    var place: Place,

    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: Category,

    @OneToOne(mappedBy = "event", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var price: Price? = null,

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, orphanRemoval = true)
    var tickets: MutableList<Ticket> = mutableListOf(),

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var availableSeats: MutableList<AvailableSeat> = mutableListOf()
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun addRating(rating: Int) {
        var totalRating = averageRating * reviewCount
        totalRating += rating
        reviewCount++

        averageRating = String.format("%.1f", (totalRating / reviewCount)).toFloat()
    }

    fun deleteRating(rating: Int) {
        var totalRating = averageRating * reviewCount
        totalRating -= rating
        reviewCount--
        if (totalRating == 0F) {
            averageRating = 0F
        } else {
            averageRating = String.format("%.1f", (totalRating / reviewCount)).toFloat()
        }
    }
}





