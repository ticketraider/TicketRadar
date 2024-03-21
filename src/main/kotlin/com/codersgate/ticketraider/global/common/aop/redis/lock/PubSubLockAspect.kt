package com.codersgate.ticketraider.global.common.aop.redis.lock

import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
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
        val methodSignature = joinPoint.signature as MethodSignature
        val annotation = methodSignature.method.getAnnotation(PubSubLock::class.java)

        val lockList = mutableListOf<RLock>()
        try {
            //획득시도 시간, 락 점유 시간
            for (i in 0 until createTicketRequest.seatList.size) {
                val key = generateKey(
                    createTicketRequest.eventId,
                    createTicketRequest.date,
                    createTicketRequest.seatList[i].ticketGrade,
                    createTicketRequest.seatList[i].seatNumber
                )
                val lock = redissonClient.getLock(key)
                val available = lock.tryLock(10, 3, TimeUnit.SECONDS) //획득시도 시간, 락 점유 시간
                lockList.add(lock)

                if (!available) {
                    println("lock 획득 실패")
                    return
                }
            }
            joinPoint.proceed()
        } finally {
            lockList.map { it.unlock() }
        }
    }
    private fun generateKey(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int): String {
        val key = "ID : ${eventId}, $date : ${grade}-${seatNo}"
        return key
    }
}