package com.codersgate.ticketraider.domain.like.model

import com.codersgate.ticketraider.domain.event.model.Event
import com.codersgate.ticketraider.domain.member.entity.Member
import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLDelete(sql = "UPDATE likes SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
//@SQLRestriction("is_deleted = false")
@Table(name = "likes")
class Like (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    val event : Event,

    @Column(name = "event_title")
    val eventTitle: String


    ) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long? = null
}