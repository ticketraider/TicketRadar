package com.codersgate.ticketraider.domain.member.entity

import com.codersgate.ticketraider.domain.like.model.Like
import com.codersgate.ticketraider.domain.review.model.Review
import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Table(name = "members")
@Entity
@SQLDelete(sql = "UPDATE members SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
class Member(
    @Column(name = "email", unique = true)
    var email: String,

    @Column(name = "password")
    var password: String? = null,

    @Column(name = "nickname", unique = true)
    var nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: MemberRole,

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true)
    val tickets: List<Ticket> = mutableListOf(),

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: List<Like> = mutableListOf(),


    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val reviews: List<Review> = mutableListOf(),

    ) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateProfile(password: String, nickname: String) {
        this.password = password
        this.nickname = nickname
    }
}

enum class MemberRole {
    ADMIN, MEMBER
}

enum class Provider {
    KAKAO, NAVER, GOOGLE, COMMON
}