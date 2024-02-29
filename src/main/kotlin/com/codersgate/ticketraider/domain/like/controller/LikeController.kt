package com.codersgate.ticketraider.domain.like.controller

import com.codersgate.ticketraider.domain.like.dto.LikeResponse
import com.codersgate.ticketraider.domain.like.service.LikeService
import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.sun.java.accessibility.util.EventID
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
        @RequestParam(required = false) userId: Long,
        @RequestParam(required = false) eventId: Long,
    ) : ResponseEntity<Page<LikeResponse>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(likeService.getLikeList(pageable, userId, eventId))
    }

    @Operation(summary = "좋아요 단건 조회")
    @GetMapping("/{likeId}")
    fun getLike(
        @PathVariable likeId : Long
    ) : ResponseEntity<LikeResponse>
    {
        return ResponseEntity.status(HttpStatus.OK).body(likeService.getLike(likeId))
    }

    @Operation(summary = "좋아요 생성")
    @PostMapping()
    fun createLike(
        @RequestParam(required = true) memberId : Long,
        @RequestParam(required = true) eventId : Long,
    ): ResponseEntity<Unit> {
        likeService.createLike(memberId, eventId)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @Operation(summary = "좋아요 최신화")
    @PatchMapping("/update")
    fun updateLike() : ResponseEntity<Unit>
    {
        likeService.updateLike()
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @Operation(summary = "좋아요 취소")
    @DeleteMapping()
    fun deleteLike(
        @RequestParam(required = true) memberId : Long,
        @RequestParam(required = true) eventId : Long,
    ): ResponseEntity<Unit> {
        likeService.deleteLike(memberId, eventId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}