package com.codersgate.ticketraider.domain.member.dto

import com.codersgate.ticketraider.domain.member.entity.Member
import com.codersgate.ticketraider.domain.member.entity.MemberRole

data class MemberResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val role: MemberRole
) {
    companion object {
        fun from(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id!!,
                email = member.email,
                nickname = member.nickname,
                role = member.role
            )
        }
    }
}
