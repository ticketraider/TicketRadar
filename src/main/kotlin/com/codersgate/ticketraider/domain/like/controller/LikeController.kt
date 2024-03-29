package com.codersgate.ticketraider.domain.like.controller

import com.codersgate.ticketraider.domain.like.dto.LikeResponse
import com.codersgate.ticketraider.domain.like.service.LikeService
import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal
import com.sun.java.accessibility.util.EventID
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/likes")
class LikeController(
    private val likeService: LikeService
) {
    @Operation(summary = "좋아요 통합 조회(전체/유저ID/이벤트ID")
    @GetMapping()
    fun getLikeList(
        @PageableDefault(size = 5, sort = ["id"]) pageable: Pageable,
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @RequestParam(required = false) eventId: Long?,
    ) : ResponseEntity<Page<LikeResponse>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(likeService.getLikeList(pageable, userPrincipal?.id , eventId))
    }

    @Operation(summary = "좋아요 단건 조회")
    @GetMapping("/{likeId}")
    fun getLike(
        @PathVariable likeId : Long
    ) : ResponseEntity<LikeResponse>
    {
        return ResponseEntity.status(HttpStatus.OK).body(likeService.getLike(likeId))
    }

    @Operation(summary = "좋아요 체크")
    @PostMapping()
    fun chkLike(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam(required = true) eventId : Long,
    ): ResponseEntity<Unit> {
        likeService.chkLike(userPrincipal.id, eventId)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @Operation(summary = "좋아요 최신화")
    @Scheduled(cron = "0 0 12 * * MON-FRI") // 매주 월요일부터 금요일까지 매일 정오(12시)에 메서드가 실행
    @PatchMapping("/update")
    fun updateLikeCount() : ResponseEntity<Unit>
    {
        likeService.updateLikeCount()
        return ResponseEntity.status(HttpStatus.OK).build()
    }

//    @Operation(summary = "좋아요 취소(체크로 통합)")
//    @DeleteMapping()
//    fun deleteLike(
//        @RequestParam(required = true) memberId : Long,
//        @RequestParam(required = true) eventId : Long,
//    ): ResponseEntity<Unit> {
//        likeService.deleteLike(memberId, eventId)
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
//    }
}