package com.codersgate.ticketraider.domain.review.repository

import com.codersgate.ticketraider.domain.review.model.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


interface ReviewRepository : JpaRepository<Review, Long>, CustomReviewRepository{
}