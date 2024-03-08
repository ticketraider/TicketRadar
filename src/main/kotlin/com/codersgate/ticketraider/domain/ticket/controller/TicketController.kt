package com.codersgate.ticketraider.domain.ticket.controller

import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.TicketResponse
import com.codersgate.ticketraider.domain.ticket.entity.TicketStatus
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

@RestController
@RequestMapping("/tickets")
class TicketController(
    val ticketService: TicketService
) {
    @Operation(summary = "티켓 생성")
    @Transactional
    @PostMapping
    fun createTicket(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @Valid @RequestBody request: CreateTicketRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ticketService.createTicket(userPrincipal, request))
    }

    @Operation(summary = "티켓 단건 조회")
    @GetMapping("/{ticketId}")
    fun getTicketById(
        @PathVariable ticketId: Long
    ): ResponseEntity<TicketResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ticketService.getTicketById(ticketId))
    }

    @Operation(summary = "멤버 티켓리스트 조회")
    @GetMapping("/getList")
    fun getTicketListByUserId(
        @PageableDefault(
            size = 15, sort = ["id"]
        ) pageable: Pageable,
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<Page<TicketResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ticketService.getTicketListByUserId(user, pageable))
    }


    @Operation(summary = "티켓 상태 변경")
    @PutMapping
    fun updateTicket(
        @RequestParam ticketId: Long,
        @RequestParam ticketStatus: TicketStatus
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ticketService.updateTicket(ticketId, ticketStatus))
    }

    @Scheduled(cron = "0 0 12 * * MON-FRI") // 매주 월요일부터 금요일까지 매일 정오(12시)에 메서드가 실행
    @PatchMapping("/chkExpired")
    @Operation(summary = "만료 티켓 확인")
    fun chkExpiredTicket() : ResponseEntity<Unit>
    {
        ticketService.chkExpiredTickets()
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }

    @Operation(summary = "티켓 삭제")
    @DeleteMapping("/delete/{ticketId}")
    fun deleteTicket(
        @PathVariable ticketId: Long,
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ticketService.deleteTicket(ticketId, user))
    }



}