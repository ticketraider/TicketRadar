package com.codersgate.ticketraider.domain.member.service

import com.codersgate.ticketraider.domain.member.dto.*
import com.codersgate.ticketraider.domain.member.entity.Member
import com.codersgate.ticketraider.domain.member.entity.MemberRole
import com.codersgate.ticketraider.domain.member.entity.Provider
import com.codersgate.ticketraider.domain.member.entity.ProviderMap
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import com.codersgate.ticketraider.domain.member.repository.ProviderMapRepository
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
    private val providerMapRepository: ProviderMapRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
) : MemberService {
    @Transactional
    override fun signUp(memberRequest: MemberRequest, role: MemberRole) {
        memberRepository.findByEmail(memberRequest.email)
            ?.let {
                providerMapRepository.findByMemberId(it.id!!)
                    .run {
                        check(!this.isCommonMember)
                        this.registerSocialInfo(Provider.COMMON)
                        it.password = passwordEncoder.encode(memberRequest.password)
                        it.nickname = memberRequest.nickname
                    }
            }
            ?: memberRepository.save(
                Member(
                    email = memberRequest.email,
                    nickname = memberRequest.nickname,
                    password = passwordEncoder.encode(memberRequest.password),
                    role = role,
                )
            ).let {
                providerMapRepository.save(
                    ProviderMap(
                        memberId = it.id!!
                    )
                ).registerSocialInfo(Provider.COMMON)
            }
    }

    override fun login(loginRequest: LoginRequest): LoginResponse {
        val member = memberRepository.findByEmail(loginRequest.email)
            ?: throw InvalidCredentialException("")
        check(providerMapRepository.findByMemberId(member.id!!).isCommonMember)

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

    override fun socialLogin(userInfo: OAuth2UserInfo): LoginResponse {
        val member = memberRepository.findByEmail(userInfo.email)
            ?: throw InvalidCredentialException("")
        return LoginResponse(
            jwtPlugin.generateAccessToken(
                subject = member.id.toString(),
                email = member.email,
                role = member.role.name
            )
        )
    }

    @Transactional
    override fun socialSignUpOrLogin(userInfo: OAuth2UserInfo): Any? {
        val provider = Provider.valueOf(userInfo.provider)
        return memberRepository.findByEmail(userInfo.email)
            ?.let { providerMapRepository.findByMemberId(it.id!!).registerSocialInfo(provider) }
            ?: memberRepository.save(socialSignUpAndRegister(userInfo.id, userInfo.nickname, userInfo.email, provider))
    }


    @Transactional
    override fun socialSignUpAndRegister(id: String, nickname: String, email: String, provider: Provider): Member {
        val member = memberRepository.save(
            Member(
                email = email,
                nickname = nickname,
                password = "",
                role = MemberRole.MEMBER
            )
        )
        providerMapRepository.save(
            ProviderMap(
                memberId = member.id!!
            )
        ).registerSocialInfo(provider)
        return member
    }


    override fun getProfile(memberId: Long): MemberResponse {
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw ModelNotFoundException("Member", memberId)
        return MemberResponse.from(member)
    }

    override fun verifyCurrentPassword(currentPassword: String, memberId: Long): VerifyCurrentPasswordResponse {
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw ModelNotFoundException("Member", memberId)

        if (!passwordEncoder.matches(currentPassword, member.password)) {
            throw InvalidCredentialException("")
        }
        return VerifyCurrentPasswordResponse(success = true, "비밀번호가 확인되었습니다.")

    }

    @Transactional
    override fun updateProfile(nickname: String, memberId: Long) {
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw InvalidCredentialException("")
//        val changedPassword = passwordEncoder.encode(updateProfileRequest.password)
        member.updateProfile(nickname)
    }

    override fun isSocial(userPrincipal: UserPrincipal): Boolean {
        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw InvalidCredentialException("")
        return member.password == ""
    }

    @Transactional
    override fun unregister(user: UserPrincipal) {
        val member = memberRepository.findByEmail(user.email)
            ?: throw InvalidCredentialException("")
        memberRepository.delete(member)
        providerMapRepository.deleteByMemberId(member.id!!)
    }

    @Transactional
    override fun rejoin(loginRequest: LoginRequest) {

        memberRepository.restoreMemberByEmail(loginRequest.email)
            ?.let {
                check(passwordEncoder.matches(loginRequest.password, it.password))
                it.isDeleted = false
                providerMapRepository.restoreProviderMapByMemberId(it.id!!)
                    ?.isDeleted = false
            }
            ?: throw InvalidCredentialException("")
    }

}