package com.codersgate.ticketraider.domain.review.controller

import com.codersgate.ticketraider.domain.review.dto.ReviewResponse
import com.codersgate.ticketraider.domain.review.repository.ReviewRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reviews")

class ReviewController(
    private val reviewRepository: ReviewRepository,
) {

    @PostMapping()
    fun createReview() : ResponseEntity<Unit>
    {

        return ResponseEntity.status(HttpStatus.OK).build()
    }


}