package com.codersgate.ticketraider.domain.event.controller

import com.codersgate.ticketraider.domain.event.dto.CreateEventRequest
import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.dto.UpdateEventRequest
import com.codersgate.ticketraider.domain.event.service.EventService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events")
class EventController(
    private val eventService : EventService)
{

    @Operation(summary = " 이벤트 생성")
    @PostMapping
    fun createEvent(@RequestBody createEventRequest: CreateEventRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(eventService.createEvent(createEventRequest))
    }

    @Operation(summary = " 이벤트 수정")
    @PutMapping("/{eventId}")
fun updateEvent(
    @PathVariable eventId : Long,
    @RequestBody updateEventRequest: UpdateEventRequest
    ): ResponseEntity<Unit> {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(eventService.updateEvent(eventId,updateEventRequest))
}

    @Operation(summary = "이벤트 삭제")
    @DeleteMapping("/{eventId}")
    fun deleteEvent(@PathVariable eventId: Long) : ResponseEntity<Unit>{
        eventService.deleteEvent(eventId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(eventService.deleteEvent(eventId))
    }

//    @Operation(summary = "이벤트 목록 조회")
//    @GetMapping
//    fun getEventList(
//        @PageableDefault(
//            size = 15,
//            sort = ["id"]
//        ) pageable : Pageable,
//        // 여기 리퀘스트 파람 벨유값이랑 required 값 추후 확인해서 수정해야 함
//        @RequestParam(
//            value = "status",
//            required = false
//        ) status : String?
//    ): ResponseEntity<Page<EventResponse>>{
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .body(eventService.getPaginatedEventList(pageable, status))
//    }

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



