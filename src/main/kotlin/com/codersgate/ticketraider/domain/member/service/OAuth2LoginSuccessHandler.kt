package com.codersgate.ticketraider.domain.member.service

import com.codersgate.ticketraider.domain.member.dto.OAuth2UserInfo
import com.codersgate.ticketraider.global.infra.security.jwt.JwtPlugin
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2LoginSuccessHandler(
    private val jwtPlugin: JwtPlugin,
    private val memberService: MemberService
): AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val userInfo = authentication.principal as OAuth2UserInfo
        val accessToken = memberService.socialLogin(userInfo).token
        val cookie = Cookie("token", accessToken)
        cookie.path = "/"
        cookie.maxAge = 3600
        response.addCookie(cookie)
    }
}