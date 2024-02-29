package com.codersgate.ticketraider.domain.like.repository

import com.codersgate.ticketraider.domain.like.model.Like
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


interface LikeRepository : JpaRepository<Like, Long> , CustomLikeRepository{
    fun findLikeByMemberIdAndEventId(memberId:Long, eventId:Long) : Like?

    fun findAllByMemberId(memberId: Long) : List<Like>

}