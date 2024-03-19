package com.codersgate.ticketraider.global.infra.security


import com.codersgate.ticketraider.domain.member.service.OAuth2LoginSuccessHandler
import com.codersgate.ticketraider.domain.member.service.OAuth2UserService
import com.codersgate.ticketraider.global.infra.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler,
    private val oAuth2UserService: OAuth2UserService,
    private val oAuth2LoginSuccessHandler: OAuth2LoginSuccessHandler
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .cors { it.disable() }
            .headers { it.frameOptions { options -> options.sameOrigin() }}
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.authorizeHttpRequests {
                it.requestMatchers(
                    "/**",
                    "/members/login",
                    "/members/signUp",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/oauth2/**"
                ).permitAll()
                    .anyRequest().authenticated()
            }.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
//            .oauth2Login { oauthConfig ->
//                oauthConfig.authorizationEndpoint {
//                    it.baseUri("/oauth2/login")  //oauth2/login/kakao
//                }.redirectionEndpoint {
//                    it.baseUri("/oauth2/callback/*") // /oauth2/callback/kakao
//                }.userInfoEndpoint {
//                    it.userService(oAuth2UserService)
//                }.successHandler(oAuth2LoginSuccessHandler)
//            }
            .exceptionHandling {
//                it.authenticationEntryPoint(authenticationEntryPoint)
//                it.accessDeniedHandler(accessDeniedHandler)
            }
            .build()
    }
}