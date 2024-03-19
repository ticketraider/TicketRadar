package com.codersgate.ticketraider.global.infra.redis.cache

import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service


@Service
class RedisCacheService (
    private val redisTemplate: RedisTemplate<String, Any>,
    private val eventRepository: EventRepository,
    private val cacheManager: CacheManager,
){
    companion object{
        val logger = LoggerFactory.getLogger(RedisCacheService::class.java)
    }

    fun searchEvent(eventTitle: String): EventResponse {

        // 캐시에 있으면 바로 반환
        if (chkCache(CacheTarget.EVENT,eventTitle)){
            //캐싱 시간 초기화
            refreshCacheTtl(CacheTarget.EVENT, eventTitle)
            val value =  getCachedValue(CacheTarget.EVENT, eventTitle)
                ?.let{
                    return it
                }
        }

        // 없으면 찾아서 반환
        val eventResponse =  getEventByTitle(eventTitle)

        // 캐시에 등록.
        putCache(CacheTarget.EVENT, eventTitle, eventResponse)

        // 인기도 증가
//        setScoreForKeyword(CacheTarget.EVENT,  eventTitle, event)


//        val membersWithScores = redisTemplate.opsForZSet().rangeWithScores("event", 0, -1)
//        logger.info("캐시멤버와 스코어 :  $membersWithScores")

        return eventResponse
    }

    fun getEventByTitle(eventTitle : String) : EventResponse{
        return eventRepository.findByTitle(eventTitle)
            ?.let { EventResponse.from(it) }
            ?: throw ModelNotFoundException("Event", null)
    }

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

    fun putCache(target:CacheTarget, key: String, response : Any){
        logger.info("캐시에 등록 :  $key , $response")
        val cache = cacheManager.getCache(target.name)
        cache?.put(key, response) // 등록
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
        cache?.evict(key) // 캐시 삭제
        cache?.put(key, value) // 다시 등록
        logger.info("실제 재등록 : ${cache?.get(key)}")
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