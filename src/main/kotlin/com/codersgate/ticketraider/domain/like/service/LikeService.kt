package com.codersgate.ticketraider.domain.like.service

import com.codersgate.ticketraider.domain.like.dto.LikeResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LikeService {

    fun getLikeList(pageable: Pageable, memberId: Long?, eventId: Long?) : Page<LikeResponse>

    fun getLike(likeId:Long) : LikeResponse
    fun chkLike(memberId: Long, eventId : Long)

    fun updateLike()
//    fun deleteLike(memberId: Long, eventId : Long)
}