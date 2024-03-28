package com.codersgate.ticketraider.global.infra.redis.cache

import com.codersgate.ticketraider.domain.event.dto.EventResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RedisCacheController(
    private val redisCacheService: RedisCacheService
) {
    @PostMapping("/search")
    fun search(
        @RequestParam eventTitle: String,
    ) : ResponseEntity<EventResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(redisCacheService.searchEvent(eventTitle))
    }

    @GetMapping("/popularKeywords")
    fun getPopularKeywords(
    ) : ResponseEntity<List<String>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(redisCacheService.getPopularKeywords(5))
    }

    @GetMapping("/popularValues")
    fun getPopularValues(
        @RequestParam(required = false) limit: Long?,
    ) : ResponseEntity<List<EventResponse>>
    {
        val l = limit?:5
        return ResponseEntity.status(HttpStatus.OK).body(redisCacheService.getPopularEventList(l))
    }

    @GetMapping("/getCachedEventList")
    fun getCachedEventList(
        @RequestParam(required = true) key:String
    ) : ResponseEntity<List<EventResponse>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(redisCacheService.getCachedEventList(key))
    }

    @GetMapping("/updateCachedEventList")
    fun updateCachedEventList() : ResponseEntity< List<List<EventResponse>> >
    {
        return ResponseEntity.status(HttpStatus.OK).body(redisCacheService.updateCachedEventList())
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @PatchMapping("/decreaseScore")
    fun decreaseScoresAndRemoveExpired() {
        redisCacheService.decreaseScoresAndRemoveExpired()
    }
}