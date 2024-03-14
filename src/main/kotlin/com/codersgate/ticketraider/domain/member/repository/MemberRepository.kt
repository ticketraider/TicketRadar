package com.codersgate.ticketraider.domain.member.repository

import com.codersgate.ticketraider.domain.member.entity.Member
import com.codersgate.ticketraider.domain.oauth.entity.SocialMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): Member?

}