package com.codersgate.ticketraider.domain.member.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@SQLDelete(sql = "UPDATE provider_map SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Entity
class ProviderMap(

    var isCommonMember: Boolean = false,
    var isKakaoMember: Boolean = false,
    var isNaverMember: Boolean = false,
    var isGoogleMember: Boolean = false,
    val memberId: Long,
    var isDeleted: Boolean = false,
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