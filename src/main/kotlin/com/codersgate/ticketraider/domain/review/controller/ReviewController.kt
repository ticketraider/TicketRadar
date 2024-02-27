package com.codersgate.ticketraider.domain.review.controller

import com.codersgate.ticketraider.domain.review.dto.CreateReviewRequest
import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.domain.review.dto.UpdateReviewRequest
import com.codersgate.ticketraider.domain.review.repository.ReviewRepository
import com.codersgate.ticketraider.domain.review.service.ReviewService
import io.swagger.v3.oas.annotations.Operation
import org.apache.catalina.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.parameters.P
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/reviews")

class ReviewController(
    private val reviewService: ReviewService,
    private val reviewRepository: ReviewRepository,
) {

    @Operation(summary = "리뷰 생성")
    @PostMapping()
    fun createReview(
        @RequestBody request: CreateReviewRequest
    ) : ResponseEntity<Unit>
    {
        reviewService.createReview(request)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @Operation(summary = "통합 조회(전체/유저ID/이벤트ID")
    @GetMapping()
    fun getReviewList_V2(
        @PageableDefault(size = 5, sort = ["id"]) pageable: Pageable,
        @RequestParam(required = false) userId: Long,
        @RequestParam(required = false) eventId: Long,
    ) : ResponseEntity<Page<ReviewResponse>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewList_V2(pageable, userId, eventId))
    }


    @Operation(summary = "전체 조회")
    @GetMapping()
    fun getReviewList(
        @PageableDefault(size = 5, sort = ["id"]) pageable: Pageable
    ) : ResponseEntity<Page<ReviewResponse>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewList(pageable))
    }

    @Operation(summary = "이벤트 ID 별 조회")
    @GetMapping("/{eventId}")
    fun getReviewListByEvent(
        @PageableDefault(size = 5, sort = ["id"]) pageable: Pageable,
        @PathVariable eventId : Long,
    ): ResponseEntity<Page<ReviewResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewListByEvent(pageable, eventId))
    }

    @Operation(summary = "유저 ID 별 조회")
    @GetMapping("/{userId}")
    fun getReviewListByUser(
        @PageableDefault(size = 5, sort = ["id"]) pageable: Pageable,
        @PathVariable userId : Long
    ): ResponseEntity<Page<ReviewResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewListByUser(pageable, userId))
    }


    @Operation(summary = "리뷰 ID 단건 조회")
    @GetMapping("/{reviewId}")
    fun getReview(
        @PathVariable reviewId : Long,
    ): ResponseEntity< ReviewResponse >{
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReview(reviewId))
    }

    @Operation(summary = "리뷰 수정")
    @PutMapping("/{reviewId}")
    fun updateReview(
        @PathVariable reviewId : Long,
        @RequestBody request : UpdateReviewRequest
    ) : ResponseEntity<Unit>{
        reviewService.updateReview(reviewId, request)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/{reviewId}")
   fun deleteReview(
        @PathVariable reviewId : Long,
   ) :  ResponseEntity<Unit>{
        reviewService.deleteReview(reviewId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }


}
