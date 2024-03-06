package com.codersgate.ticketraider.domain.ticket.service

import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.TicketResponse
import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.repository.TicketRepository
import com.codersgate.ticketraider.global.error.exception.InvalidCredentialException
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class TicketServiceImpl(
    private val ticketRepository: TicketRepository,
    private val memberRepository: MemberRepository,
    private val eventRepository: EventRepository,
    private val redissonClient: RedissonClient,
) : TicketService {
    override fun createTicket(userPrincipal: UserPrincipal, createTicketRequest: CreateTicketRequest) {
        val event = eventRepository.findByIdOrNull(createTicketRequest.eventId)
            ?: throw ModelNotFoundException("event", createTicketRequest.eventId)
        val member = memberRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("member", userPrincipal.id)

        val lockList = mutableListOf<RLock>()
        val count = createTicketRequest.seatList.size
        for (i in 0 until count) {
            val lock = redissonClient.getLock(generateKey(createTicketRequest))
            lockList.add(lock)
            //획득시도 시간, 락 점유 시간
            lock.tryLock(10, 1, TimeUnit.SECONDS)
        }
        for (i in 0 until count) {
            //키마다 캐시안에 넣는 작업 캐시안에 있는지 없는지
            //DB존재 유무파악
            ticketRepository.save(
                Ticket(
                    date = createTicketRequest.date,
                    grade = createTicketRequest.seatList[i].first,
                    seatNo = createTicketRequest.seatList[i].second,
                    event = event,
                    member = member
                )
            )
            lockList[i].unlock()
        }
    }

    override fun getTicketById(ticketId: Long): TicketResponse {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)
        return TicketResponse.from(ticket)
    }


    override fun deleteTicket(ticketId: Long, user: UserPrincipal) {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)
        if (ticket.member.id!! != user.id) {
            throw InvalidCredentialException("")
        }
        ticketRepository.delete(ticket)
    }

    override fun getTicketListByUserId(user: UserPrincipal, pageable: Pageable): Page<TicketResponse> {
        return ticketRepository.getListByUserId(pageable, user.id).map { TicketResponse.from(it) }
    }
}
private fun generateKey(request: CreateTicketRequest): String {
    val key = "ID : ${request.eventId}, ${request.date} : ${request.seatList[0].first}-${request.seatList[0].second}"
    return key
}