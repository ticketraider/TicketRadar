package com.codersgate.ticketraider.domain.member.service

import com.codersgate.ticketraider.domain.member.dto.LoginRequest
import com.codersgate.ticketraider.domain.member.dto.LoginResponse
import com.codersgate.ticketraider.domain.member.dto.MemberResponse
import com.codersgate.ticketraider.domain.member.dto.MemberRequest
import com.codersgate.ticketraider.domain.member.entity.MemberRole
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal

interface MemberService {
    fun signUp(memberRequest: MemberRequest, role:MemberRole)

    fun login(loginRequest: LoginRequest): LoginResponse

    fun getProfile(memberId: Long): MemberResponse

    fun updateProfile(updateProfileRequest: UpdateProfileRequest, user: UserPrincipal)

    fun unregister(user: UserPrincipal)
}