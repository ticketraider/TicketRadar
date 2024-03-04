package com.codersgate.ticketraider.domain.event.controller


import com.codersgate.ticketraider.domain.event.dto.EventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.service.EventService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.data.domain.Pageable

@RestController
@RequestMapping("/events")
class EventController(
    private val eventService : EventService)
{

    @Operation(summary = " 이벤트 생성")
    @PostMapping
    fun createEvent(
        @RequestBody eventRequest: EventRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(eventService.createEvent(eventRequest))
    }

    @Operation(summary = " 이벤트 수정")
    @PutMapping("/{eventId}")
fun updateEvent(
        @PathVariable eventId: Long,
        @RequestBody eventRequest: EventRequest,
    ): ResponseEntity<Unit> {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(eventService.updateEvent(eventId, eventRequest))
}

    @Operation(summary = "이벤트 삭제")
    @DeleteMapping("/{eventId}")
    fun deleteEvent(@PathVariable eventId: Long) : ResponseEntity<Unit>{
        eventService.deleteEvent(eventId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(eventService.deleteEvent(eventId))
    }

    @Operation(summary = "이벤트 목록 조회")
    @GetMapping
    fun getEventList(
        @PageableDefault(
            size = 15,
            sort = ["id"]
        ) pageable : Pageable,
        @RequestParam(
            value = "status",
            required = false
        ) status : String?,
        @RequestParam categoryId: Long?
    ): ResponseEntity<Page<EventResponse>>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(eventService.getPaginatedEventList(pageable, status, categoryId))
    }

    @Operation(summary = "이벤트 조회")
    @GetMapping("/{eventId}")
    fun getEvent(
        @PathVariable eventId: Long
    ): ResponseEntity<EventResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(eventService.getEvent(eventId))
    }
}




