package com.codersgate.ticketraider.domain.like.dto

import com.codersgate.ticketraider.domain.like.model.Like

data class LikeResponse(
    val id:Long,
    val memberId:Long,
    val eventId:Long,
    val eventTitle: String
){
    companion object{
        fun from(like: Like) : LikeResponse{
            return LikeResponse(
                id = like.id!!,
                memberId = like.member.id!!,
                eventId= like.event.id!!,
                eventTitle = like.eventTitle
            )
        }

    }
}
