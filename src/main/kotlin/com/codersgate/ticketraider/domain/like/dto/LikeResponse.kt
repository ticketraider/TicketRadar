package com.codersgate.ticketraider.domain.like.dto

import com.codersgate.ticketraider.domain.like.model.Like

data class LikeResponse(
    val likeId:Long,
    val memberId:Long,
    val eventId:Long,
    val isDeleted:Boolean,  //조회할땐 True면 자동으로 조회가 불가능한데 여기서 한번 더 isDeleted를 알려줄 필요가 있는지 의문
){
    companion object{
        fun from(like: Like) : LikeResponse{
            return LikeResponse(
                likeId = like.id!!,
                memberId = like.member.id!!,
                eventId= like.event.id!!,
                isDeleted= like.isDeleted
            )
        }

    }
}
