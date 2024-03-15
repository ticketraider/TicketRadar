package com.codersgate.ticketraider.global.infra.redis.cache

import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.event.service.EventService
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Service
import java.util.stream.Collectors


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

    fun searchEvent(eventTitle: String): EventResponse {

        // 캐시에 있으면 바로 반환
        val value : EventResponse? = chkCache(CacheTarget.EVENT,eventTitle)

        if (value != null){
            //캐싱 시간 초기화
            refreshCacheTtl(CacheTarget.EVENT, eventTitle)
            return value
        }

        // 없으면 찾아서 반환
        val event =  getEventByTitle(eventTitle)

        // 캐시에 등록.
        putCache(CacheTarget.EVENT, eventTitle, event)

        // 인기도 증가
        setScoreForKeyword(CacheTarget.EVENT,  eventTitle, event)


//        val membersWithScores = redisTemplate.opsForZSet().rangeWithScores("event", 0, -1)
//        logger.info("캐시멤버와 스코어 :  $membersWithScores")

        return event
    }

    fun getEventByTitle(eventTitle : String) : EventResponse{
        return eventRepository.findByTitle(eventTitle)
            ?.let { EventResponse.from(it) }
            ?: throw ModelNotFoundException("Event", null)
    }


    fun chkCache(target: CacheTarget, key:String ): EventResponse? {
        logger.info("${target.name} 캐시 확인 시작")
        val cache = cacheManager.getCache(target.name)
        val value = cache?.get(key)?.get()

        if (value != null) {    // 캐시에 일치하는 키 있을 때
            logger.info("캐시 적중 !! : $key")
            logger.info("값 : $value")

            // cachedValue를 EventResponse로 변환
            val eventResponse: EventResponse? = if (value is LinkedHashMap<*, *>) {
                EventResponse.mapToEventResponse(value)
            } else {
                null // 캐시에서 가져온 값이 LinkedHashMap이 아닌 경우에 대한 처리
            }
            return eventResponse

        } else {   // 캐시에 일치하는 키 없을 때
            logger.info("캐시 실패 !! : $key")
            return null
        }
    }

    fun putCache(target:CacheTarget, key: String, value : Any){
        logger.info("캐시에 등록 :  $key , $value")
        val cache = cacheManager.getCache(target.name)
        cache?.put(key, value) // 등록
    }

    fun refreshCacheTtl(target: CacheTarget, key: String) {
        logger.info("캐시에 재등록 :  $key ")
        val cache = cacheManager.getCache(target.name)
        cache?.get(key)?.let { value ->
            cache.evict(key) // 캐시 삭제
            cache.put(key, value) // 다시 등록
        }
    }

    // 인기검색어 리스트 1위~10위까지
//    fun SearchRankList(): List<SearchRankResponseDto> {
//
//        val score = 0.0
//        try {
//            // 검색을하면 해당검색어를 value에 저장하고, score를 1 준다
//            redisTemplate.opsForZSet().incrementScore("ranking", searchRequestDto.getKeyword().get(0), 1.0)
//        } catch (e: Exception) {
//            println(e.toString())
//        }
//
//        //score를 1씩 올려준다.
//        redisTemplate.opsForZSet().incrementScore("ranking", searchRequestDto.getKeyword().get(0), score)
//
//
//        val key = "ranking"
//        val ZSetOperations: ZSetOperations<String, String> = redisTemplate.opsForZSet()
//        val typedTuples = ZSetOperations.reverseRangeWithScores(key, 0, 9) //score순으로 10개 보여줌
//        return typedTuples!!.stream().map<Any>(SearchRankResponseDto::convertToResponseRankingDto)
//            .collect(Collectors.toList<Any>())
//    }

//    fun createSortedSet(redisTemplate: RedisTemplate<String, Any>, key: String, members: Map<Any, Double>) {
//        val zSetOperations = redisTemplate.opsForZSet()
//        zSetOperations.add(key, members)
//    }

    fun setScoreForKeyword(target:CacheTarget, key:String, value:Any) {
        logger.info("캐시 점수 수정 :  ${target.name}::${key} ")
        val r = redisTemplate.opsForZSet().incrementScore("${target.name}::${key}", value, 10.toDouble())
        logger.info("$r")
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