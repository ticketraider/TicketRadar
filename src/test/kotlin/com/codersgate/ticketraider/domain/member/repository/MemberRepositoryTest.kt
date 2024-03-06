package com.codersgate.ticketraider.domain.member.repository

import com.codersgate.ticketraider.domain.member.entity.Member
import com.codersgate.ticketraider.domain.member.entity.MemberRole
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
//@Import(JpaBaseConfiguration::class)
@Rollback(value = false)
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    fun memberInsertTest() {
        // given
        val newMember = Member(
            nickname = "testMember",
            email = "test@test.com",
            password = "123456",
            role = MemberRole.MEMBER
        )

        // when
        val savedMember = memberRepository.save(newMember)

        // then
        assertThat(savedMember).isNotNull
    }
}