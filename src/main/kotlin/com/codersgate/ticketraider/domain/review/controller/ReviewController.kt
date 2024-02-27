package com.codersgate.ticketraider.domain.review.controller

import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.domain.review.repository.ReviewRepository
import com.codersgate.ticketraider.domain.review.service.ReviewService
import org.apache.catalina.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.parameters.P
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reviews")

class ReviewController(
    private val reviewService: ReviewService,
    private val reviewRepository: ReviewRepository,
) {

    @PostMapping()
    fun createReview() : ResponseEntity<Unit>
    {

        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @GetMapping()
    fun getReviewList(
        @PageableDefault(size = 5, sort = ["id"]) pageable: Pageable
    ) : ResponseEntity<Page<ReviewResponse>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewList(pageable))
    }

    @GetMapping("/{eventId}")
    fun getReviewListByEvent(
        @PageableDefault(size = 5, sort = ["id"]) pageable: Pageable,
        @PathVariable eventId : Long,
    ): ResponseEntity<Page<ReviewResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewListByEvent(pageable, eventId))
    }

    @GetMapping("/{userId}")
    fun getReviewListByUser(
        @PageableDefault(size = 5, sort = ["id"]) pageable: Pageable,
        @PathVariable userId : Long
    ): ResponseEntity<Page<ReviewResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewListByUser(pageable, userId))
    }


    @GetMapping("/{reviewId}")
    fun getReview(
        @PathVariable reviewId : Long,
    ): ResponseEntity< ReviewResponse >{
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReview(reviewId))
    }


    @PostMapping("/{reviewId}")
    fun updateReview(
        @PathVariable reviewId : Long,
    ) : ResponseEntity<Unit>{
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @DeleteMapping("/{reviewId}")
   fun deleteReview(
        @PathVariable reviewId : Long,
   ) :  ResponseEntity<Unit>{
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }


}
