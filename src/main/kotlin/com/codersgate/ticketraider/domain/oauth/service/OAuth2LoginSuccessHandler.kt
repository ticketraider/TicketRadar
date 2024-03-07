package com.codersgate.ticketraider.domain.oauth.service

import com.codersgate.ticketraider.domain.member.entity.MemberRole
import com.codersgate.ticketraider.domain.oauth.dto.OAuth2UserInfo
import com.codersgate.ticketraider.global.infra.security.jwt.JwtPlugin
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2LoginSuccessHandler(
    private val jwtPlugin: JwtPlugin,
    private val socialMemberService: SocialMemberService
): AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val userInfo = authentication.principal as OAuth2UserInfo
        val accessToken = jwtPlugin.generateAccessToken(
            subject = userInfo.id,
            email = userInfo.email,
            role = MemberRole.MEMBER.name
        )
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(accessToken)
    }
}