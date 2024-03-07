package com.codersgate.ticketraider.domain.oauth.entity

import jakarta.persistence.*

@Entity
class SocialMember(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_member_id")
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    val provider: OAuth2Provider,

    val providerId: String,
    val email: String,
    val nickname: String
) {
    companion object {
        fun ofKakao(id: String, nickname: String, email: String): SocialMember {
            return SocialMember(
                provider = OAuth2Provider.KAKAO,
                providerId = id,
                email = email,
                nickname = nickname
            )
        }
        fun ofNaver(id: String, nickname: String, email: String): SocialMember {
            return SocialMember(
                provider = OAuth2Provider.NAVER,
                providerId = id,
                email = email,
                nickname = nickname
            )
        }
    }
}

enum class OAuth2Provider {
    KAKAO, NAVER
}