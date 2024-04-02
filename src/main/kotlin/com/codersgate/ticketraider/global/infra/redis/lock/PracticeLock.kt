package com.codersgate.ticketraider.global.infra.redis.lock

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.TimeUnit

// 1
@Component
class RedissonLock(
    // 이렇게 하면 RedissonLock 의 Bean 을 만드는 과정에서 LockAdvice 의 Bean 을 주입받아
    // companion object 에 초기화해주는 로직이 만들어지고, 그 결과 companion object 내에서
    // LockAdvice 의 Bean 을 호출할 수 있게 된다.
    private val tmpAdvice: LockAdvice  // 2
) {

    init {
        advice = tmpAdvice
    }

    // 5. 밑 companion object 에서 받은 인자들로 레디스 락 얻음. 락을 범위로 묶어 사용하기 위해 키(이벤트 아이디)를 사용해 이벤트 참여자들끼리 묶음.
    @Component
    class LockAdvice(
        private val redissonClient: RedissonClient
    ) {
        operator fun <T> invoke(key: String, timeout: Duration, func: () -> T): T {
            val lock = redissonClient.getLock("LOCK:$key")
            //획득시도 시간, 락 점유 시간 (60초로 동일)
            if (!lock.tryLock(timeout.seconds, timeout.seconds, TimeUnit.SECONDS)) {
                throw RuntimeException("LOCK 획득 실패!")
            }
            // 예외에 걸리지 않고 func() 이 잘 실행되었다면 락이 해제됨
            return func().also { lock.unlock() }
        }
    }

    // 3
    companion object {
        // 4. RedissonLock 이 생성될때 advice 가 초기화 되는데 그때 참이면 key 값, 잡고있는 시간, 함수 호출 인자를 받아서 위 LockAdvice 호출. 거짓이면,companion object 내 invoke() 재호출.
        private lateinit var advice: LockAdvice

        // 자동호출 함수
        operator fun <T> invoke(key: String, timeout: Duration = Duration.ofSeconds(60), func: () -> T): T {
            return when (this::advice.isInitialized) {
                true -> advice(key, timeout, func) // LockAdvice 로 넘어감
                false -> func()  // 이 함수 재호출
            }
        }
    }
}