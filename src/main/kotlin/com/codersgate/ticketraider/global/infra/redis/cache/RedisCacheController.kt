package com.codersgate.ticketraider.global.infra.redis.cache

import com.codersgate.ticketraider.domain.event.dto.EventResponse
import io.lettuce.core.protocol.CommandKeyword
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RedisCacheController(
    private val redisCacheService: RedisCacheService
) {
    @PostMapping("/search")
    fun search(
        @RequestParam keyword: String,
        @RequestParam(required = false) eventId: Long?,
        @RequestParam(required = false) eventTitle: String?,
    ) : ResponseEntity<EventResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(redisCacheService.searchEvent(keyword, eventId, eventTitle))
    }
}