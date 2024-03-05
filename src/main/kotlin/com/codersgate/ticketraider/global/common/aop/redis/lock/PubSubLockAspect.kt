package com.codersgate.ticketraider.global.common.aop.redis.lock

import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
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

        val lock = redissonClient.getLock(generateKey(createTicketRequest))
        try {
            //획득시도 시간, 락 점유 시간
            val available =lock.tryLock(5, 1, TimeUnit.SECONDS)

            if (!available) {
                println("lock 획득 실패")
                return
            }
            joinPoint.proceed()
//        } catch (e: InterruptedException) {
//            throw RuntimeException(e)
        } finally {
            lock.unlock()
        }
    }
    private fun generateKey(request: CreateTicketRequest): String {
        val key = "ID : ${request.eventId}, ${request.date} : ${request.grade}-${request.seatNo}"
        return key
    }
}