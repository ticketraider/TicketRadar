package com.codersgate.ticketraider.domain.review.repository

import com.codersgate.ticketraider.domain.review.model.Review
import org.springframework.data.jpa.repository.JpaRepository


interface ReviewRepository : JpaRepository<Review, Long>, CustomReviewRepository {
}