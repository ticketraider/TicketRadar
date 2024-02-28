package com.codersgate.ticketraider.domain.like.repository

import com.codersgate.ticketraider.domain.like.model.Like
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomLikeRepository {
    fun getLikeList(pageable:Pageable, memberId: Long?, eventId: Long?) : Page<Like>
}