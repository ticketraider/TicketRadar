package com.codersgate.ticketraider.domain.ticket.service

import com.codersgate.ticketraider.domain.event.model.seat.Bookable
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.event.repository.seat.AvailableSeatRepository
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import com.codersgate.ticketraider.domain.ticket.dto.BookedTicketResponse
import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.TicketResponse
import com.codersgate.ticketraider.domain.ticket.entity.Ticket
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import com.codersgate.ticketraider.domain.ticket.entity.TicketStatus
import com.codersgate.ticketraider.domain.ticket.repository.TicketRepository
import com.codersgate.ticketraider.global.common.aop.redis.lock.PubSubLock
import com.codersgate.ticketraider.global.error.exception.InvalidCredentialException
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import com.codersgate.ticketraider.global.error.exception.TicketReservationFailedException
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal
import org.hibernate.Hibernate
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class TicketServiceImpl(
    private val ticketRepository: TicketRepository,
    private val memberRepository: MemberRepository,
    private val eventRepository: EventRepository,
    private val availableSeatRepository: AvailableSeatRepository,
) : TicketService {

    @PubSubLock
    override fun createTicket(memberId: Long, request: CreateTicketRequest) {
        val event = eventRepository.findByIdOrNull(request.eventId)
            ?: throw ModelNotFoundException("event", request.eventId)

        Hibernate.initialize(event.availableSeats)   // 컬렉션을 명시적으로 초기화 ( LAZY 모드 )

        // 예약 날짜 체크
        if (request.date < event.startDate || request.date > event.endDate || request.date < LocalDate.now()) {
            throw TicketReservationFailedException("예매일(${request.date})이 올바르지 않습니다.")
        }

        // 좌석 예약 가능 상태 확인
        val availableSeat = event.availableSeats.find {
            it.date == request.date && it.bookable == Bookable.OPEN
        } ?: let {
            throw TicketReservationFailedException("예매일(${request.date}) 의 예약이 불가능한 상태 입니다.")
        }

        // 예약 가능 좌석 선별
        request.seatList.map { seat ->

            //DB 체크
            val isReserved = ticketRepository.chkTicket(
                request.eventId,
                request.date,
                seat.ticketGrade,
                seat.seatNumber
            )
            if (isReserved != null) {
                throw TicketReservationFailedException("${seat.seatNumber} 번 좌석은 선택할 수 없습니다. ( 이미 예약된 좌석 in DB )")
            }
        }

        // 티켓 생성
        request.seatList.map { seat ->

            val member = memberRepository.findByIdOrNull(memberId)
                ?: throw ModelNotFoundException("member", memberId)

            val ticket = Ticket(
                date = request.date,
                grade = seat.ticketGrade,
                seatNo = seat.seatNumber,
                event = event,
                member = member,
                price = when (seat.ticketGrade) {
                    TicketGrade.R -> event.price!!.seatRPrice
                    TicketGrade.S -> event.price!!.seatSPrice
                    TicketGrade.A -> event.price!!.seatAPrice
                },
                place = event.place.name,
                address = event.place.address
            )

            ticketRepository.save(ticket)


            // 좌석 예약 수 수정
            availableSeat.increaseSeat(seat.ticketGrade)
            if (availableSeat.isFull()) {
                availableSeat.close()
            }
            availableSeatRepository.save(availableSeat)
        }
        eventRepository.save(event)
        //캐시 내 이벤트 항목 최신화 하지 않아도 됨. Response 에는 변동사항 없음
    }

    override fun getBookedTicket(eventId: Long, date: LocalDate): BookedTicketResponse {
        val ticketList = ticketRepository.findAllByEventIdAndDate(eventId, date)
        val bookedTicketArray: Array<String> = ticketList.map { "${it!!.grade}${it.seatNo}" }.toTypedArray()
        return BookedTicketResponse(bookedTicketArray)
    }

    override fun getAllTicketList(pageable: Pageable, memberId: Long?, eventId: Long?): Page<TicketResponse> {
        return ticketRepository.getAllTicketList(pageable, memberId, eventId).map {
            val event = eventRepository.findByIdOrNull(it.event.id!!)
            val member = memberRepository.findByIdOrNull(it.member.id!!)
            TicketResponse.from(it, event!!, member!!)
        }
    }

    override fun getTicketById(ticketId: Long): TicketResponse {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)
        val event = eventRepository.findByIdOrNull(ticket.event.id!!)
        val member = memberRepository.findByIdOrNull(ticket.member.id!!)
        return TicketResponse.from(ticket, event!!, member!!)
    }

    override fun getTicketListByUserId(pageable: Pageable, memberId: Long): Page<TicketResponse> {
        val ticketList = ticketRepository.getListByUserId(pageable, memberId)
        val ticketListResponse = ticketList.map {
            val event = eventRepository.findByIdOrNull(it.event.id!!)
            val member = memberRepository.findByIdOrNull(it.member.id!!)
            TicketResponse.from(it, event!!, member!!)
        }
        return ticketListResponse
//        return ticketRepository.getListByUserId(pageable, memberId).map{ TicketResponse.from(it, it.event, it.member) }
    }

    override fun chkExpiredTickets() {
        // TODO() findAll 보다 동적쿼리로 대상만 찾을지?
        ticketRepository.findAll().map {
            if (it.date < LocalDate.now())
                it.ticketStatus = TicketStatus.EXPIRED
            ticketRepository.save(it)
        }
    }

    override fun makePayment(
        userPrincipal: UserPrincipal,
        ticketId: Long
    ):TicketResponse? {

        val paidTicket : Ticket  = ticketRepository.findByIdOrNull(ticketId)
            ?: throw ModelNotFoundException("Ticket", ticketId)

        val event = eventRepository.findByIdOrNull(paidTicket.event.id!!)
        val member = memberRepository.findByIdOrNull(paidTicket.member.id!!)


        if (userPrincipal.id == paidTicket.member.id && paidTicket.ticketStatus == TicketStatus.UNPAID) {

            // TODO() 결제로직

            paidTicket.ticketStatus = TicketStatus.PAID
            ticketRepository.save(paidTicket)
        }

        return TicketResponse.from( paidTicket, event!!, member!!)
    }

    @Transactional
    override fun cancelTicket(ticketId: Long, userPrincipal: UserPrincipal) {
//        val ticket = ticketRepository.findByIdOrNull(ticketId)
//        ticketRepository.delete(ticket!!)
        if (userPrincipal.authorities.toString() != "ROLE_ADMIN") { // ADMIN 아닐 시
            ticketRepository.findByIdOrNull(ticketId)
                ?.let { ticket ->
                    if (ticket.member.id != userPrincipal.id)  // 본인 확인 // 사실 본인 티켓만 확인되니 없어도 됨.
                        throw InvalidCredentialException("")    // 인가 오류
                }
        }
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?.let { ticket ->
                ticket.event.availableSeats
                    .filter { seat ->
                        (seat.date == ticket.date) && (seat.event!!.id == ticket.event.id)  // 날짜, 이벤트 확인
                    }[0]
                    .let {
                        it.decreaseSeat(ticket.grade)           // 좌석 수 줄임
                        availableSeatRepository.save(it)
                    }
                ticketRepository.delete(ticket)
            }
            ?: throw ModelNotFoundException("Ticket", ticketId)
    }
}

