package com.codersgate.ticketraider.domain.member.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class ProviderMap(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var isCommonMember: Boolean = false,
    var isKakaoMember: Boolean = false,
    var isNaverMember: Boolean = false,
    var isGoogleMember: Boolean = false,
    val memberId: Long
) {

    fun registerSocialInfo(socialInfo: Provider) {
        when (socialInfo) {
            Provider.COMMON -> isCommonMember = true
            Provider.KAKAO -> isKakaoMember = true
            Provider.NAVER -> isNaverMember = true
            Provider.GOOGLE -> isGoogleMember = true
        }
    }
}