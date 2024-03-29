package com.codersgate.ticketraider.global.infra.redis.cache

import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.event.service.EventService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

//@Aspect
//@Component
//class SearchEventAspect(
//    private val redisCacheService: RedisCacheService,
//) {
//    @AfterReturning("@annotation(com.codersgate.ticketraider.global.infra.redis.cache.SearchEvent)")
//    fun afterEventOperation(joinPoint: ProceedingJoinPoint) { // return Any?
//        // 인기 검색어 목록 가져와서 전부 한번씩 검색해보고 결과 다시 저장함
//        val keywords = redisCacheService.getPopularKeywords(10)
//
//        keywords.map { keyword ->
//            redisCacheService.getEventByTitle(keyword).also {// 없으면  throw, 종료
//                redisCacheService.incrementSearchCount(keyword)
//                //최소 n점 부터 캐시에 저장
//                redisCacheService.putCache(CacheTarget.EVENT, keyword, it) // 캐시에 등록.
//            }
//        }
//    }
//}