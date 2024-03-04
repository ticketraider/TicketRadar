package com.codersgate.ticketraider.domain.member.service

import com.codersgate.ticketraider.domain.member.dto.LoginRequest
import com.codersgate.ticketraider.domain.member.dto.LoginResponse
import com.codersgate.ticketraider.domain.member.dto.MemberResponse
import com.codersgate.ticketraider.domain.member.dto.MemberRequest
import com.codersgate.ticketraider.domain.member.entity.Member
import com.codersgate.ticketraider.domain.member.entity.MemberRole
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import com.codersgate.ticketraider.global.error.exception.EmailAlreadyExistException
import com.codersgate.ticketraider.global.error.exception.InvalidCredentialException
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import com.codersgate.ticketraider.global.infra.security.jwt.JwtPlugin
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
): MemberService {
    override fun signUp(memberRequest: MemberRequest,  role:MemberRole) {
        if (memberRepository.existsByEmail(memberRequest.email)) {
            throw EmailAlreadyExistException(memberRequest.email)
        }
        memberRepository.save(
            Member(
                email = memberRequest.email,
                nickname = memberRequest.nickname,
                password = passwordEncoder.encode(memberRequest.password),
                role = role
            )
        )
    }

    override fun login(loginRequest: LoginRequest): LoginResponse{

        val member = memberRepository.findByEmail(loginRequest.email)
            ?: throw InvalidCredentialException("")

        if (!passwordEncoder.matches(loginRequest.password, member.password))
            throw InvalidCredentialException("")

        return LoginResponse(
            token = jwtPlugin.generateAccessToken(
                subject = member.id.toString(),
                email = member.email,
                role = member.role.name
            )
        )
    }

    override fun getProfile(memberId: Long): MemberResponse {
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw ModelNotFoundException("Member", memberId)
        return MemberResponse.from(member)
    }

    @Transactional
    override fun updateProfile(memberRequest: MemberRequest, user: UserPrincipal) {
        val member = memberRepository.findByIdOrNull(user.id)
            ?: throw InvalidCredentialException("")
        val changedPassword = passwordEncoder.encode(memberRequest.password)
        member.updateProfile(memberRequest.email, changedPassword, memberRequest.nickname)
    }

    @Transactional
    override fun unregister(user: UserPrincipal) {
        val member = memberRepository.findByIdOrNull(user.id)
            ?: throw InvalidCredentialException("")
        memberRepository.delete(member)
    }


}