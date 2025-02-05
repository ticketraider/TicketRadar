package com.codersgate.ticketraider.global.common.aop.redis.lock

import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import jakarta.transaction.Transactional
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@Aspect
@Component
class PubSubLockAspect(
    private val redissonClient: RedissonClient,
) {

    @Around("@annotation(com.codersgate.ticketraider.global.common.aop.redis.lock.PubSubLock) && args(..,createTicketRequest)")
    fun runPubSubLock(joinPoint: ProceedingJoinPoint, createTicketRequest: CreateTicketRequest) {

        val lockList = mutableListOf<RLock>()
        try {
            //획득시도 시간, 락 점유 시간
            for (i in 0 until createTicketRequest.seatList.size) {
                val key = generateKey(
                    createTicketRequest.eventId,
                    createTicketRequest.date,
                    createTicketRequest.seatList[i].ticketGrade,
                    createTicketRequest.seatList[i].seatNumber
                )   // Redisson 분산 락 Key 설정
                val lock = redissonClient.getLock(key) // 해당 키의 Lock 객체 가져오기. 반환형 RLock
                val available = lock.tryLock(10, 10, TimeUnit.SECONDS) //획득시도 시간, 락 점유 시간

                if (!available) {
                            // 에러메세지 발생 필요
                    return  // Lock 획득 실패
                }
                lockList.add(lock)
            }
            joinPoint.proceed()
        } finally {
            lockList.forEach { it.unlock() }
        }
    }

    private fun generateKey(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int): String {
        val key = "ID : ${eventId}, $date : ${grade}-${seatNo}"
        return key
    }
}