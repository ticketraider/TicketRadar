package com.codersgate.ticketraider.global.infra.redis.cache

import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.event.service.EventService
import com.codersgate.ticketraider.domain.ticket.service.TicketServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisCacheService (
    private val redisTemplate: RedisTemplate<String, Any>,
    private val eventService: EventService,
    private val eventRepository: EventRepository,
    private val cacheManager: CacheManager,
){
    companion object{
        val logger = LoggerFactory.getLogger(RedisCacheService::class.java)
        enum class CacheTarget {
            EVENT, TICKET
        }
    }

    fun searchEvent(keyword: String, eventId: Long?, eventTitle: String?): EventResponse {

        val event = if (eventId != null)  getEventById(eventId)
                    else if (eventTitle != null)  getEventByTitle(eventTitle)
                    else  throw IllegalArgumentException("Id 혹은 Title 를 입력해주세요!")

        chkCache(CacheTarget.EVENT,eventId,eventTitle)

        return event
    }

    fun getEventById(eventId: Long) : EventResponse{
        return eventService.getEvent(eventId)
    }

    fun getEventByTitle(eventTitle : String) : EventResponse{
        return EventResponse.from( eventRepository.findByTitle(eventTitle) )
    }


    fun chkCache(target: CacheTarget, id:Long?, title:String? ): Boolean {

        val key = if ( id==null) "$title"
                  else "$id"

        logger.info("${target.name} 캐시 확인 시작")
        val cache = cacheManager.getCache(target.name)
        val value = cache?.get(key)

        if (value != null) {    // 캐시에 일치하는 키 있을 때
            logger.info("Cache hit for event : $key")
            logger.info("Cache in Cache : $value")
            return true
        } else {   // 캐시에 일치하는 키 없을 때
            logger.info("Cache miss for event: $key")
            return false
        }

    }

//    fun addSearchKeyword(keyword: String) {
//        redisTemplate.opsForZSet().incrementScore("popular_searches", keyword, 1.0)
//    }
//    fun save(searchTerm: SearchTerm) {
//        redisTemplate.opsForValue().set(searchTerm.keyword, searchTerm)
//    }
//
//    fun findTop10(): List<SearchTerm> {
//        return redisTemplate.opsForZSet().rangeByScore("popular-searches", 0.0, Double.MAX_VALUE, 0, 10)
//    }
//
//    fun getPopularSearches(key : String, limit: Long): Set<String> {
//        return redisTemplate.opsForZSet().reverseRange(key, 0, limit - 1)
//    }
}