package com.codersgate.ticketraider.domain.review.model

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.member.entity.Member
import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction


@Entity
@Table(name = "reviews")
@SQLDelete(sql = "UPDATE reviews SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
class Review (
    @Column(name = "title")
    var title : String,

    @Column(name = "content")
    var content : String,

    @Column(name = "rating")
    var rating : Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    val event : Event,

    @Transient // 데이터베이스에 매핑되지 않는 임시 필드
    var nickname: String? = null // 닉네임을 저장할 필드

    ) : BaseEntity()
    {
        @Id
        @GeneratedValue( strategy = GenerationType.IDENTITY)
        val id : Long? = null

        // 닉네임을 가져오는 메서드
        fun loadNickname() {
            this.nickname = member.nickname // Member 엔티티의 닉네임을 가져와서 필드에 할당
        }
}



