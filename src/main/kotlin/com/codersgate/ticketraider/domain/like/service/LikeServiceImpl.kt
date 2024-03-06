package com.codersgate.ticketraider.domain.like.service

import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.like.dto.LikeResponse
import com.codersgate.ticketraider.domain.like.model.Like
import com.codersgate.ticketraider.domain.like.repository.LikeRepository
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import org.hibernate.annotations.NotFound
import org.slf4j.LoggerFactory
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

    override fun chkLike(memberId: Long, eventId: Long) {
        val member = memberRepository.findByIdOrNull(memberId)
            ?:throw NotFoundException()

        val event = eventRepository.findByIdOrNull(eventId)
            ?:throw NotFoundException()

       likeRepository.findLikeByMemberIdAndEventId(memberId, eventId)
           ?.let{
               it.isDeleted = !it.isDeleted
               event.likeCount += if(it.isDeleted) 1 else -1
               likeRepository.save(it)

           }
           ?:run{
               event.likeCount++
               likeRepository.save(Like(member,event))
           }

        eventRepository.save(event)
    }

    override fun updateLike() {
        // 이벤트 id 리스트를 Like 에서 가져와서
        likeRepository.getEventIdList().map{e_id ->
            // 각 id 마다 해당하는 like 가 몇개인지 확인하고   // 각 이벤트 객체의 count 를 저장
            val event = eventRepository.findByIdOrNull(e_id)
                ?.also {e ->
                    e.likeCount = likeRepository.countEventId(e.id!!).toInt()
                }
                ?: throw NotFoundException()
            eventRepository.save(event)
        }
    }

//    override fun deleteLike(memberId: Long, eventId: Long) {
//        val member = memberRepository.findByIdOrNull(memberId)
//            ?:throw NotFoundException()
//
//        val event = eventRepository.findByIdOrNull(eventId)
//            ?:throw NotFoundException()
//
//        likeRepository.findLikeByMemberIdAndEventId(memberId, eventId)
//            ?.let{
//                likeRepository.delete(it)  //직접 isDeleted를 true로 바꿔주는게 아닌 Delete 쿼리를 날리면 됨
//            }
//            ?: throw NotFoundException()
//    }
}