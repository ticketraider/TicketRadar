package com.codersgate.ticketraider.domain.member.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User

data class OAuth2UserInfo(
    val id: String,
    val provider: String,
    val nickname: String,
    val email: String
): OAuth2User {
    override fun getName(): String {
        return "$provider:$id"
    }
    override fun getAttributes(): MutableMap<String, Any> {
        return mutableMapOf()
    }
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }
    companion object {
        fun of(provider: String, userRequest: OAuth2UserRequest, originUser: OAuth2User): OAuth2UserInfo {
            return when (provider) {
                "KAKAO", "kakao" -> ofKakao(provider, userRequest, originUser)
                "NAVER", "naver" -> ofNaver(provider, userRequest, originUser)
                "GOOGLE", "google" -> ofGoogle(provider, userRequest, originUser)
                else -> throw RuntimeException("지원하지 않는 OAuth Provider 입니다.")
            }
        }

        private fun ofKakao(provider: String, userRequest: OAuth2UserRequest, originUser: OAuth2User): OAuth2UserInfo {
            val profile = originUser.attributes["properties"] as Map<*, *>
            val userNameAttributeName = userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName
            val nickname = profile["nickname"] ?: ""
            val account = originUser.attributes["kakao_account"] as Map<*, *>
            val email = account["email"] ?: ""

            return OAuth2UserInfo(
                id = (originUser.attributes[userNameAttributeName] as Long).toString(),
                provider = provider.uppercase(),
                nickname = nickname as String,
                email = email as String
            )
        }
        private fun ofNaver(provider: String, userRequest: OAuth2UserRequest, originUser: OAuth2User): OAuth2UserInfo {
            val profile = originUser.attributes["response"] as Map<*, *>
            val nickname = profile["nickname"] ?: ""
            val email = profile["email"] ?: ""

            return OAuth2UserInfo(
                id = profile["id"].toString(),
                provider = provider.uppercase(),
                nickname = nickname as String,
                email = email as String
            )
        }
        private fun ofGoogle(provider: String, userRequest: OAuth2UserRequest, originUser: OAuth2User): OAuth2UserInfo {
            val userNameAttributeName = userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName
            val nickname = originUser.attributes["name"] ?: ""
            val email = originUser.attributes["email"] ?: ""

            return OAuth2UserInfo(
                id = originUser.attributes[userNameAttributeName].toString(),
                provider = provider.uppercase(),
                nickname = nickname as String,
                email = email as String
            )
        }
    }

}