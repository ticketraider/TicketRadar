package com.codersgate.ticketraider.global.infra.redis.lock

import com.codersgate.ticketraider.domain.ticket.service.TicketService
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component

@Component
class RedissonLockService(
    private val redissonClient: RedissonClient,
    private val ticketService: TicketService
) {
//    fun createTicket(memberId: Long, request: CreateTicketRequest) {
//        //key 로 Lock 객체 가져옴
//        val lockList = mutableListOf<RLock>()
//        try {
//            //획득시도 시간, 락 점유 시간
//            for (i in 0 until request.seatList.size) {
//                val key = generateKey(
//                    request.eventId,
//                    request.date,
//                    request.seatList[i].ticketGrade,
//                    request.seatList[i].seatNumber
//                )
//                val lock = redissonClient.getLock(key)
//                val available = lock.tryLock(10, 3, TimeUnit.SECONDS) //획득시도 시간, 락 점유 시간
//                lockList.add(lock)
//
//                if (!available) {
//                    println("lock 획득 실패")
//                    return
//                }
//            }
//            ticketService.createTicket(memberId, request)
////        } catch (e: TicketReservationFailedException) {
////            throw TicketReservationFailedException("")
//        } finally {
//            lockList.map { it.unlock() }
//        }
//    }
//
//    private fun generateKey(eventId: Long, date: LocalDate, grade: TicketGrade, seatNo: Int): String {
//        val key = "ID : ${eventId}, $date : ${grade}-${seatNo}"
//        return key
//    }
}