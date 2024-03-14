package com.codersgate.ticketraider.domain.member.controller

import com.codersgate.ticketraider.domain.member.dto.*
import com.codersgate.ticketraider.domain.member.entity.MemberRole
import com.codersgate.ticketraider.domain.member.service.MemberService
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal

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
        @Valid @RequestBody memberRequest: MemberRequest,
        @RequestParam role: MemberRole
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.signUp(memberRequest, role))
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
    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
    @PutMapping("/update")
    fun updateProfile(
        @Valid @RequestBody updateProfileRequest: UpdateProfileRequest,
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updateProfile(updateProfileRequest, user))
    }

    @Operation(summary = "회원 탈퇴")
    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
    @DeleteMapping("/members/unregister")
    fun unregister(
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.unregister(user))
    }
    @Operation(summary = "재가입")
    @PutMapping("/rejoin")
    fun rejoin(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.rejoin(loginRequest))
    }
}

