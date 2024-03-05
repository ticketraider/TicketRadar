package com.codersgate.ticketraider.global.infra.redis.lock

import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.service.TicketService
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedissonLockService(
    private val redissonClient: RedissonClient,
    private val ticketService: TicketService
) {
    fun createTicket(userid: Long, request: CreateTicketRequest) {
        //key 로 Lock 객체 가져옴
        val lock = redissonClient.getLock(generateKey(request))

        try {
            //획득시도 시간, 락 점유 시간
            val available =lock.tryLock(5, 1, TimeUnit.SECONDS)

            if (!available) {
                println("lock 획득 실패")
                return
            }
            ticketService.createTicket(request)
//        } catch (e: InterruptedException) {
//            throw RuntimeException(e)
        } finally {
            lock.unlock()
        }
    }

    fun testFairLock(userid: Long, request: CreateTicketRequest) {
        val lock = redissonClient.getFairLock(generateKey(request))

        try {
            //획득시도 시간, 락 점유 시간
            val available =lock.tryLock(5, 1, TimeUnit.SECONDS)

            if (!available) {
                println("lock 획득 실패")
                return
            }
            ticketService.createTicket(request)
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