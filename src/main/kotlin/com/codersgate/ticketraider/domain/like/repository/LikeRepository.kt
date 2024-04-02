package com.codersgate.ticketraider.domain.like.repository

import com.codersgate.ticketraider.domain.like.model.Like
import org.springframework.data.jpa.repository.JpaRepository


interface LikeRepository : JpaRepository<Like, Long>, CustomLikeRepository {
    fun findLikeByMemberIdAndEventId(memberId: Long, eventId: Long): Like?


}