package com.codersgate.ticketraider.domain.member.controller

import com.codersgate.ticketraider.domain.member.dto.LoginRequest
import com.codersgate.ticketraider.domain.member.dto.LoginResponse
import com.codersgate.ticketraider.domain.member.dto.MemberResponse
import com.codersgate.ticketraider.domain.member.dto.MemberRequest
import com.codersgate.ticketraider.domain.member.service.MemberService
import com.codersgate.ticketraider.global.infra.security.UserPrincipal

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RequestMapping("/members")
@RestController
class MemberController(
    val memberService: MemberService
) {
    @Operation(summary = "회원가입")
    @PostMapping("/signUp")
    fun signUp(
        @Valid @RequestBody memberRequest: MemberRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.signUp(memberRequest))
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest,
    ): ResponseEntity<LoginResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.login(loginRequest))
    }

    @Operation(summary = "프로필 조회")
    @GetMapping("/members/{memberId}")
    fun getProfile(
       @PathVariable memberId: Long
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.getProfile(memberId))
    }

    @Operation(summary = "프로필 수정")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @PutMapping("/members/update")
    fun updateProfile(
        @RequestParam memberRequest: MemberRequest,
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updateProfile(memberRequest, user))
    }

    @Operation(summary = "회원 탈퇴")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @DeleteMapping("/members/unregister")
    fun unregister(
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.unregister(user))
    }

    // 로그아웃 구현?
//    @Operation(summary = "로그아웃")
//    @PreAuthorize("hasAnyRole('MEMBER')")
//    @DeleteMapping("/members/logout")
//    fun logout(
//        @AuthenticationPrincipal user: UserPrincipal
//    ): ResponseEntity<Unit> {
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .body(memberService.logout(user))
//    }
}