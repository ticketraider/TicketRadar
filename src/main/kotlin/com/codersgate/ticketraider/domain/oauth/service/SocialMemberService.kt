package com.codersgate.ticketraider.domain.oauth.service

import com.codersgate.ticketraider.domain.oauth.dto.OAuth2UserInfo
import com.codersgate.ticketraider.domain.oauth.entity.OAuth2Provider
import com.codersgate.ticketraider.domain.oauth.entity.SocialMember
import com.codersgate.ticketraider.domain.oauth.repository.SocialMemberRepository
import org.springframework.stereotype.Service

@Service
class SocialMemberService(
    private val socialMemberRepository: SocialMemberRepository
) {
    fun registerIfAbsent(userInfo: OAuth2UserInfo): SocialMember {
        val provider = OAuth2Provider.valueOf(userInfo.provider)
        return if (!socialMemberRepository.existsByProviderAndProviderId(provider, userInfo.id)) {
            socialMemberRepository.save( when (provider) {
                OAuth2Provider.KAKAO -> SocialMember.ofKakao(userInfo.id, userInfo.nickname, userInfo.email)
                OAuth2Provider.NAVER -> SocialMember.ofNaver(userInfo.id, userInfo.nickname, userInfo.email)
            })
        } else {
            socialMemberRepository.findByProviderAndProviderId(provider, userInfo.id)
        }
    }
}