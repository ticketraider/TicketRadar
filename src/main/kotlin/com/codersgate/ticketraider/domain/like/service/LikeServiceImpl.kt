package com.codersgate.ticketraider.domain.like.service

import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.like.dto.LikeResponse
import com.codersgate.ticketraider.domain.like.model.Like
import com.codersgate.ticketraider.domain.like.repository.LikeRepository
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import org.hibernate.annotations.NotFound
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class LikeServiceImpl(
    private var memberRepository: MemberRepository,
    private var eventRepository: EventRepository,
    private var likeRepository: LikeRepository,
) : LikeService{

    override fun getLikeList(pageable: Pageable, memberId: Long?, eventId: Long?) : Page<LikeResponse> {

        return if( memberId == null && eventId == null)
                throw NotFoundException()
            else
                likeRepository.getLikeList(pageable, memberId,eventId).map{ LikeResponse.from(it)}
    }

    override fun getLike(likeId: Long): LikeResponse {
        return LikeResponse.from(likeRepository.findByIdOrNull(likeId)
            ?: throw NotFoundException()
        )
    }

    override fun createLike(memberId: Long, eventId: Long) {
        val member = memberRepository.findByIdOrNull(memberId)
            ?:throw NotFoundException()

        val event = eventRepository.findByIdOrNull(eventId)
            ?:throw NotFoundException()

       likeRepository.findLikeByMemberIdAndEventId(memberId, eventId)
           ?.let{
               it.isDeleted=false
               likeRepository.save(it)
           }
           ?: likeRepository.save(Like(member,event))

    }

    override fun deleteLike(memberId: Long, eventId: Long) {
        val member = memberRepository.findByIdOrNull(memberId)
            ?:throw NotFoundException()

        val event = eventRepository.findByIdOrNull(eventId)
            ?:throw NotFoundException()

        likeRepository.findLikeByMemberIdAndEventId(memberId, eventId)
            ?.let{
                it.isDeleted = true
                likeRepository.save(it)
            }
            ?: throw NotFoundException()
    }
}