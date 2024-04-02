package com.codersgate.ticketraider.global.infra.redis.cache

import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.event.dto.price.PriceResponse
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.event.service.EventService
import com.codersgate.ticketraider.domain.place.dto.PlaceResponse
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.data.domain.PageRequest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class RedisCacheService (
    private val redisTemplate: RedisTemplate<String, Any>,
    private val eventRepository: EventRepository,
    private val cacheManager: CacheManager,
    private val eventService: EventService,
){
    companion object{
        val logger = LoggerFactory.getLogger(RedisCacheService::class.java)
        val SEARCH_KEY = "POPKEYWORDS"
    }

    fun searchEvent(eventTitle: String): EventResponse {
        logger.info("eventTitle : $eventTitle")
        // 캐시에 있으면 바로 반환
        if (chkCache(CacheTarget.EVENT,eventTitle)){
            //캐싱 시간 초기화
            refreshCacheTtl(CacheTarget.EVENT, eventTitle)
            getCachedEvent(CacheTarget.EVENT, eventTitle)
                ?.let{
                    incrementSearchCount(eventTitle) // 검색어 등록 및 점수 추가
                    return it
                }
        }

        // 캐시에 없으면 찾아서 반환
        return getEventByTitle(eventTitle).also{// 없으면  throw, 종료
            incrementSearchCount(eventTitle)
            //최소 n점 부터 캐시에 저장
            putCache(CacheTarget.EVENT, eventTitle, it) // 캐시에 등록.
        }
    }

    fun getEventByTitle(eventTitle : String) : EventResponse{
        return eventRepository.findByTitle(eventTitle)
            ?.let { EventResponse.from(it) }
            ?: throw ModelNotFoundException("Event", null)
    }

    fun getCachedEvent(target: CacheTarget, key:String ): EventResponse? {
        logger.info("${target.name} 캐시 값 가져오기 시작")
        val cache = cacheManager.getCache(target.name)
        val value = cache?.get(key) // 캐시에서 가져온 값. LinkedHashMap 형태.

        return value?.get() as? EventResponse // as? 에 ? 없으면 에러남 java.lang.ClassCastException: class java.util.LinkedHashMap cannot be cast to class com.codersgate.ticketraider.domain.event.dto.EventResponse (java.util.LinkedHashMap is in module java.base of loader 'bootstrap'; com.codersgate.ticketraider.domain.event.dto.EventResponse is in unnamed module of loader 'app')
    }

    // 프론트 홈에서 호출할 때 사용
    fun getCachedEventList(key: String): List<EventResponse> {
        logger.info("${CacheTarget.EVENT} 캐시 값 가져오기 시작")
        val cache = cacheManager.getCache(CacheTarget.EVENT.name)
        val value = cache?.get(key) // 캐시에서 가져온 값. LinkedHashMap 형태.

        val list = value?.get() as? List<Map<String, Any>> // 각 요소가 맵 형태이므로 타입을 지정합니다.

        return list?.map { item ->
            EventResponse(
                id = (item["id"] as Int).toLong(),
                title = item["title"] as String,
                likeCount = item["likeCount"] as Int,
                startDate = LocalDate.parse(item["startDate"] as String), // 문자열을 LocalDate로 변환합니다.
                endDate = LocalDate.parse(item["endDate"] as String), // 문자열을 LocalDate로 변환합니다.
                eventInfo = item["eventInfo"] as String,
                averageRating = (item["averageRating"] as Double).toFloat(),
                posterImage = item["posterImage"] as String,
                place = PlaceResponse(
                    name = (item["place"] as Map<String, Any>)["name"] as String,
                    totalSeat = (item["place"] as Map<String, Any>)["totalSeat"] as Int,
                    seatR = (item["place"] as Map<String, Any>)["seatR"] as Int,
                    seatS = (item["place"] as Map<String, Any>)["seatS"] as Int,
                    seatA = (item["place"] as Map<String, Any>)["seatA"] as Int
                ),
                price = PriceResponse(
                    seatRPrice = (item["price"] as Map<String, Int>)["seatRPrice"] ?: 0,
                    seatSPrice = (item["price"] as Map<String, Int>)["seatSPrice"] ?: 0,
                    seatAPrice = (item["price"] as Map<String, Int>)["seatAPrice"] ?: 0
                ),
                reviewCount = item["reviewCount"] as Int
            )
        } ?: emptyList() // 만약 리스트가 null이면 빈 리스트를 반환합니다.
    }

    fun getPopularKeywords(limit: Long): List<String> {
        val popKeywords =
            redisTemplate.opsForZSet().reverseRangeWithScores(SEARCH_KEY, 0, limit - 1)
                ?.map{ it.value.toString()
                }?: emptyList()

        logger.info("$popKeywords")

        return popKeywords
    }

    fun getPopularScores(limit: Long): List<Int>? {
        val popScoreList =
            redisTemplate.opsForZSet().reverseRangeWithScores(SEARCH_KEY, 0, limit - 1)
                ?.map{ it.score!!.toInt()
                }?: emptyList()

        logger.info("$popScoreList")

        return popScoreList
    }

    fun getPopularEventList(limit: Long): List<EventResponse> { // 인기검색어 리스트 가져와서 캐시에 등록된 내용 가져오기. 없으면 캐시에 등록
        val popKeys = getPopularKeywords(limit)
        val popList =
            popKeys.map {keyword->
                getCachedEvent(CacheTarget.EVENT, keyword)
                    ?:let{
                        getEventByTitle(keyword).also{event ->
                            putCache(CacheTarget.EVENT, keyword, event) // 인기 키워드 캐시에 단건 등록.
                        }
                    }
            }
        putCache(CacheTarget.EVENT, "popularity", popList) // 캐시에 리스트 등록
        return popList
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

    @Scheduled(initialDelay = 0, fixedRate = 600000) // 서버 실행시, 10분 마다 실행됨
    fun updateCachedEventList() : List<List<EventResponse>>
    {
        val listByPopularity = getPopularEventList(5)
        putCache(CacheTarget.EVENT, "popularity", listByPopularity)

        val listByRating =eventService.getPaginatedEventList(PageRequest.of(0, 5),
            "rating", "title", null, "" )!!.toList()
        putCache(CacheTarget.EVENT, "rating", listByRating)

        val listByReviews =eventService.getPaginatedEventList(PageRequest.of(0, 5),
            "reviews", "title", null, "" )!!.toList()
        putCache(CacheTarget.EVENT, "reviews", listByReviews)

        val listByLikes =eventService.getPaginatedEventList(PageRequest.of(0, 5),
            "likes", "title", null, "" )!!.toList()
        putCache(CacheTarget.EVENT, "likes", listByLikes)

        return listOf(listByPopularity, listByRating, listByReviews, listByLikes)
    }

//  eventRepository.findByPageable(pageable, sortStatus, searchStatus, category, keyword)


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

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    fun decreaseScoresAndRemoveExpired() {
        val popularSearches = redisTemplate.opsForZSet().rangeWithScores(SEARCH_KEY, 0, -1)

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
