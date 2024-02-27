package com.codersgate.ticketraider.domain.member.dto

import com.codersgate.ticketraider.domain.member.entity.Member

data class MemberResponse(
    val id: Long,
    val email: String,
    val nickname: String
) {
    companion object {
        fun from(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id!!,
                email = member.email,
                nickname = member.nickname
            )
        }
    }
}
