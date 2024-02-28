package com.codersgate.ticketraider.domain.member.entity

import com.codersgate.ticketraider.domain.member.dto.MemberRequest
import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Table(name = "members")
@Entity
@SQLDelete(sql = "UPDATE category SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
class Member(
    @Column(name = "email", unique = true)
    var email: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "nickname", unique = true)
    var nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: MemberRole

//    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
//    val tickets : List<ticket> = emptyList()

    ): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateProfile(memberRequest: MemberRequest): Member {
        this.email = memberRequest.email
        this.password = memberRequest.password
        this.nickname = memberRequest.nickname
        return this
    }
}