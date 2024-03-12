package com.codersgate.ticketraider.domain.ticket.service

import com.codersgate.ticketraider.domain.category.model.Category
import com.codersgate.ticketraider.domain.category.repository.CategoryRepository
import com.codersgate.ticketraider.domain.event.dto.EventRequest
import com.codersgate.ticketraider.domain.event.repository.EventRepository
import com.codersgate.ticketraider.domain.event.repository.price.PriceRepository
import com.codersgate.ticketraider.domain.event.service.EventService
import com.codersgate.ticketraider.domain.member.entity.Member
import com.codersgate.ticketraider.domain.member.entity.MemberRole
import com.codersgate.ticketraider.domain.member.repository.MemberRepository
import com.codersgate.ticketraider.domain.place.model.Place
import com.codersgate.ticketraider.domain.place.repository.PlaceRepository
import com.codersgate.ticketraider.domain.ticket.dto.CreateTicketRequest
import com.codersgate.ticketraider.domain.ticket.dto.SeatInfo
import com.codersgate.ticketraider.domain.ticket.entity.TicketGrade
import com.codersgate.ticketraider.global.error.exception.ModelNotFoundException
import com.codersgate.ticketraider.global.infra.security.jwt.UserPrincipal
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
@ExtendWith(MockKExtension::class)
@ActiveProfiles("test")
class TicketServiceImplTest(
    @Autowired val memberRepository: MemberRepository,
    @Autowired val placeRepository: PlaceRepository,
    @Autowired val eventRepository: EventRepository,
    @Autowired val categoryRepository: CategoryRepository,
    @Autowired val ticketService: TicketService,
    @Autowired val eventService: EventService,
) {

    @Test
    @DisplayName("티켓 테스트")
    fun `ticket test`() {

        val place = Place(
            name = "문화회관",
            totalSeat = 300,
            seatR = 50,
            seatS = 100,
            seatA = 150,
            address = "서울시어쩌구"
        )
        placeRepository.save(place)
        val member = Member(
            email = "test@gmail.com",
            password = "test12",
            nickname = "testMember",
            role = MemberRole.ADMIN
        )
        memberRepository.save(member)
        val category = Category(
            title = "MUSICAL"
        )
        val getCate = categoryRepository.save(category)
        val createEventReq = EventRequest(
            title = "오페라의 유령",
            posterImage = "string",
            categoryId = getCate.id!!,
            eventInfo = "String",
            _startDate = "2024-03-10",
            _endDate = "2024-03-17",
            place = "문화회관",
            seatRPrice = 150000,
            seatSPrice = 100000,
            seatAPrice = 50000
        )
        eventService.createEvent(createEventReq)
        val getEvent = eventRepository.findByIdOrNull(1)


        val threadCount = 10
        val executorService = Executors.newFixedThreadPool(5)
        val countDownLatch = CountDownLatch(threadCount)

        var success = 0
        var fail = 0
        val createTicketReq = CreateTicketRequest(
            date = LocalDate.now(),
            eventId = getEvent!!.id!!,
        )
        //when 100개의 스레드로 동시에 티켓을 구매했을때
        repeat(threadCount) {
            executorService.submit {
                try {
                    println("리퀘스트 전!@#(@!*#)*(!@#&(*!@#&*(!@@#&*(@!#&(*!@#&*(&!@(*@#&*(!@&#*(")
                    createTicketReq.seatList.add(SeatInfo(TicketGrade.R, 1))
                    println("크리에이트티켓 전!@#(@!*#)*(!@#&(*!@#&*(!@@#&*(@!#&(*!@#&*(&!@(*@#&*(!@&#*(")
                    ticketService.createTicket(member.id!!, createTicketReq)
                    println("티켓 생성 후 !@#(@!*#)*(!@#&(*!@#&*(!@@#&*(@!#&(*!@#&*(&!@(*@#&*(!@&#*(")
                    success++
                    countDownLatch.countDown()
                } catch (e: ModelNotFoundException) {
                    fail++
                    countDownLatch.countDown()
                }

            }
            countDownLatch.await()

            // 시도한 횟수랑 = 실패 + 성공 횟수 같아야함.
            // 성공 횟수가 100이 아닌거는 동시성 문제해결.
            println("success : $success")
            println("fail : $fail")

            //then 성공50 실패50 이여야한다.
            Assertions.assertThat(success).isEqualTo(1)
            Assertions.assertThat(fail).isEqualTo(9)
        }
    }
}