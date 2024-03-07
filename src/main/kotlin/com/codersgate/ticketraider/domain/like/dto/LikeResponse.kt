package com.codersgate.ticketraider.domain.like.dto

import com.codersgate.ticketraider.domain.like.model.Like

data class LikeResponse(
    val likeId:Long,
    val memberId:Long,
    val eventId:Long,
){
    companion object{
        fun from(like: Like) : LikeResponse{
            return LikeResponse(
                likeId = like.id!!,
                memberId = like.member.id!!,
                eventId= like.event.id!!,
            )
        }

    }
}
