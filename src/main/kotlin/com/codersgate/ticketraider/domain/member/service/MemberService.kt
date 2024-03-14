package com.codersgate.ticketraider.domain.member.service

import com.codersgate.ticketraider.domain.member.dto.*
import com.codersgate.ticketraider.domain.member.entity.Member
import com.codersgate.ticketraider.domain.member.entity.MemberRole
import com.codersgate.ticketraider.domain.member.entity.Provider
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal

interface MemberService {
    fun signUp(memberRequest: MemberRequest, role:MemberRole)
    fun socialSignUpOrLogin(userInfo: OAuth2UserInfo): Any?
    fun socialSignUpAndRegister(id: String, nickname: String, email: String, provider: Provider): Member
    fun login(loginRequest: LoginRequest): LoginResponse
    fun socialLogin(userInfo: OAuth2UserInfo): LoginResponse
    fun getProfile(memberId: Long): MemberResponse

    fun updateProfile(updateProfileRequest: UpdateProfileRequest, user: UserPrincipal)

    fun unregister(user: UserPrincipal)
    fun rejoin(loginRequest: LoginRequest)
}