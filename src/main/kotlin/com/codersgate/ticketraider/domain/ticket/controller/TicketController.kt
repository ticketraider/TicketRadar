package com.codersgate.ticketraider.domain.ticket.controller

import com.codersgate.ticketraider.domain.ticket.dto.BookedTicketResponse
import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.TicketResponse
import com.codersgate.ticketraider.domain.ticket.service.TicketService
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/tickets")
class TicketController(
    val ticketService: TicketService
) {
    @Operation(summary = "티켓 생성")
    @Transactional
    @PostMapping("/create")
    fun createTicket(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @Valid @RequestBody request: CreateTicketRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ticketService.createTicket(userPrincipal.id, request))
    }

    // 조건별 티켓 조회
    @Operation(summary = "티켓 단건 조회")
    @GetMapping("/{ticketId}")
    fun getTicketById(
        @PathVariable ticketId: Long
    ): ResponseEntity<TicketResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ticketService.getTicketById(ticketId))
    }


    @Operation(summary = "통합 조회(전체/유저ID/이벤트ID")
    @GetMapping("/allTicketList")
    fun getAllTicketList(
        @PageableDefault(size = 5, sort = ["id"]) pageable: Pageable,
        @RequestParam(required = false) memberId: Long?,
        @RequestParam(required = false) eventId: Long?,
    ): ResponseEntity<Page<TicketResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.getAllTicketList(pageable, memberId, eventId))
    }

    @Operation(summary = "예약된 티켓 조회(좌석배치용)")
    @GetMapping("/ticket-list/{eventId}")
    fun getBookedTicket(
        @PathVariable eventId: Long,
        @RequestParam date: LocalDate
    ): ResponseEntity<BookedTicketResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.getBookedTicket(eventId, date))
    }

    @Operation(summary = "멤버 티켓리스트 조회")
    @GetMapping("/ticket-list/user")
    fun getTicketListByUserId(
        @PageableDefault(
            size = 15, sort = ["id"]
        ) pageable: Pageable,
//        authentication: Authentication // vue 에서 jwt 토큰을 받아오도록 함
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Page<TicketResponse>> {
//        val userPrincipal = authentication.principal as UserPrincipal // jwt 토큰에서 principal 추출
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ticketService.getTicketListByUserId(pageable, userPrincipal.id))
    }


    @Scheduled(cron = "0 0 0 * * ?") // 매일 정오(12시)에 메서드가 실행
    @PatchMapping("/chkExpired")
    @Operation(summary = "만료 티켓 확인")
    fun chkExpiredTicket(): ResponseEntity<Unit> {
        ticketService.chkExpiredTickets()
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }

    @PatchMapping("/makePayment")
    @Operation(summary = "티켓 결제하기")
    fun makePayment(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam ticketIdList: MutableList<Long>,
    ): ResponseEntity<List<TicketResponse>> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ticketService.makePayment(userPrincipal, ticketIdList))
    }

    // 멤버용
    // 복사됨
    @Operation(summary = "티켓 취소")
    @DeleteMapping("/cancel/{ticketId}")
    fun cancelTicket(
        @PathVariable ticketId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ticketService.cancelTicket(ticketId, userPrincipal))
    }

}