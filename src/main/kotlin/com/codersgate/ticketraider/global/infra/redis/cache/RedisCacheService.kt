package com.codersgate.ticketraider.global.infra.redis.cache

import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
class RedisCacheService (
    private val redisTemplate: RedisTemplate<String, Any>,
    private val eventRepository: EventRepository,
    private val cacheManager: CacheManager,
){
    companion object{
        val logger = LoggerFactory.getLogger(RedisCacheService::class.java)
        val SEARCH_KEY = "Popularity:"
    }

    fun searchEvent(eventTitle: String): EventResponse {

        // 캐시에 있으면 바로 반환
        if (chkCache(CacheTarget.EVENT,eventTitle)){
            //캐싱 시간 초기화
            refreshCacheTtl(CacheTarget.EVENT, eventTitle)
            val value =  getCachedValue(CacheTarget.EVENT, eventTitle)
                ?.let{
                    incrementSearchCount(eventTitle)
                    return it
                }
        }

        // 없으면 찾아서 반환
        val eventResponse =  getEventByTitle(eventTitle)

        incrementSearchCount(eventTitle)

        // 캐시에 등록.
        putCache(CacheTarget.EVENT, eventTitle, eventResponse)

        return eventResponse
    }

    fun getEventByTitle(eventTitle : String) : EventResponse{
        return eventRepository.findByTitle(eventTitle)
            ?.let { EventResponse.from(it) }
            ?: throw ModelNotFoundException("Event", null)
    }

    //장소 검색
//    fun getPlaceByTitle(eventTitle : String) : EventResponse{
//        return eventRepository.findByTitle(eventTitle)
//            ?.let { EventResponse.from(it) }
//            ?: throw ModelNotFoundException("Event", null)
//    }

    fun getCachedValue(target: CacheTarget, key:String ): EventResponse? {
        logger.info("${target.name} 캐시 값 가져오기 시작")
        val cache = cacheManager.getCache(target.name)
        val value = cache?.get(key) // 캐시에서 가져온 값. LinkedHashMap 형태.

        // 값이 존재하고, ValueWrapper가 null이 아닌 경우에만 실제 값을 얻어옴
        val eventResponse: EventResponse? = value?.get() as? EventResponse

        return eventResponse
    }

    fun chkCache(target: CacheTarget, key:String ): Boolean {
        logger.info("${target.name} 캐시 확인 시작")
        val cache = cacheManager.getCache(target.name)
        val value = cache?.get(key)?.get()

        if (value != null) {
            logger.info("캐시 적중 !! : $key")
            logger.info("값 : $value")
            return true
        } else {
            logger.info("캐시 실패 !! : $key")
            return false
        }
    }

    fun putCache(target:CacheTarget, key: String, value : Any){
        logger.info("캐시에 등록 :  $key , $value")
        val cache = cacheManager.getCache(target.name)
        cache?.put(key, value) // 등록
        logger.info("실제 등록 :  $key , ${cache?.get(key)}")
    }

    fun delCache(target:CacheTarget, key: String){
        val cache = cacheManager.getCache(target.name)
        cache?.evict(key)
    }

    fun refreshCacheTtl(target: CacheTarget, key: String) {
        logger.info("캐시에 재등록 :  $key ")
        val cache = cacheManager.getCache(target.name)
        val value = cache?.get(key)?.get() // get 한번 더 안하면 [{}] ?
        cache?.put(key, value) // 다시 등록
        logger.info("실제 재등록 : ${cache?.get(key)}")
    }

    fun incrementSearchCount(keyword: String) {
        // Sorted Set 생성, 키, 값 추가. 중복 시 해당 키의 값 증가
        redisTemplate.opsForZSet().incrementScore(SEARCH_KEY, keyword, 1.0)
    }

    fun getPopularKeywords(limit: Long): List<String> {

        val popList =
            redisTemplate.opsForZSet().reverseRangeWithScores(SEARCH_KEY, 0, limit - 1)
                ?.map{ it.value.toString()
            }?: emptyList()

        logger.info("$popList")

        return popList
    }

     fun getPopularValues(limit: Long): List<EventResponse> {
        val popKeys = getPopularKeywords(5)
         val popValues =
             popKeys.map {
                 getEventByTitle(it)
             }
         return popValues
     }


    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    fun decreaseScoresAndRemoveExpired() {
        var popularSearches = redisTemplate.opsForZSet().rangeWithScores(SEARCH_KEY, 0, -1)

        if (popularSearches != null) {
            for (typedTuple in popularSearches) {
                val searchKeyword = typedTuple.value as String
                val currentScore = typedTuple.score!!.toInt()

                // 현재 점수가 1 이상인 경우에만 점수를 1씩 감소
                if (currentScore > 0) {
                    val newScore = currentScore - 1
                    redisTemplate.opsForZSet().incrementScore(SEARCH_KEY, searchKeyword, -1.0)

                    // 점수가 0이 되면 해당 검색어를 삭제
                    if (newScore == 0) {
                        redisTemplate.opsForZSet().remove(SEARCH_KEY, searchKeyword)
                    }
                }
            }
        }

        redisTemplate.opsForZSet().rangeWithScores(SEARCH_KEY, 0, -1)
            ?.map{
                logger.info("${it.value}, ${it.score}")
            }
    }
}