package com.codersgate.ticketraider.global.infra.redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    private lateinit var redisHost: String

    @Value("\${spring.data.redis.port}")
    private var redisPort: Int = 6379

    lateinit var redissonClient: RedissonClient

    @Bean(destroyMethod = "shutdown")
    fun redissonClient(): RedissonClient {
        val config: Config = Config()
        config.useSingleServer()
            .setAddress("redis://$redisHost:$redisPort")
            .setDnsMonitoringInterval(-1)
        return Redisson.create(config)
    }

    @Bean
    fun redisConnectionFactory(redissonClient: RedissonClient): RedisConnectionFactory {
        return RedissonConnectionFactory(redissonClient)
    }

    @Bean
    fun redisTemplate(
        redisConnectionFactory: RedisConnectionFactory
    ): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = redisConnectionFactory

        // Key는 String으로 사용
        template.keySerializer = StringRedisSerializer()

        // Value는 JSON 직렬화
        val serializer: RedisSerializer<Any> = GenericJackson2JsonRedisSerializer(objectMapper())
        template.valueSerializer = serializer

        //Hash 자료구조를 위한 Serializer
        template.hashKeySerializer = StringRedisSerializer()
        template.hashValueSerializer = StringRedisSerializer()

        template.setEnableTransactionSupport(true); // transaction 허용

        return template
    }

    // JSON 데이터와 Java 객체 간의 변환을 수행하는데 사용
    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // timestamp 형식 안따르도록 설정
        mapper.registerModules(JavaTimeModule(), Jdk8Module()) // LocalDateTime 매핑을 위해 모듈 활성화
        mapper.registerModule(ParameterNamesModule()); //생성자의 매개변수 이름을 사용하여 JSON 속성과 매핑
        return mapper
    }

    // Redis 캐시 메니저를 생성하는 메서드. 레디스 연결 팩토리 를 인자로 받아 캐시 매니저를 생성함
    @Bean
    fun cacheManager(cf: RedisConnectionFactory, objectMapper: ObjectMapper): CacheManager {

        // Redis 캐싱 구성을 생성하는 부분
        val defaultCacheConfig =
            RedisCacheConfiguration.defaultCacheConfig() // 기본 캐시 구성 사용.
                .serializeKeysWith(  // 키를 직렬화 - StringRedisSerializer 사용
                    RedisSerializationContext.SerializationPair.fromSerializer(
                        StringRedisSerializer()
                    )
                )
                .serializeValuesWith( // 값을 직렬화 - GenericJackson2JsonRedisSerializer 사용
                    RedisSerializationContext.SerializationPair.fromSerializer(
                        GenericJackson2JsonRedisSerializer(objectMapper)
                    )
                )
                .entryTtl(Duration.ofMinutes(3L)) // 캐시 만료시간  3분으로 설정

        // "tickets" 라는 이름의 캐시에 대해 적용
        val ticketsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5))

        // "events" 라는 이름의 캐시에 대해 적용
        val eventsCacheConfig = defaultCacheConfig
            .entryTtl(Duration.ofMinutes(60))

        return RedisCacheManager // RedisCacheManager를 생성하기 위한 빌더 패턴을 사용
            .builder(cf) // RedisConnectionFactory 를 받아 CacheManager를 빌드
            .cacheDefaults(defaultCacheConfig) // 캐시 매니저의 기본 캐시 구성 설정. 위에서 만든 RedisCache 구성 사용
            .withCacheConfiguration("TICKETS", ticketsCacheConfig) // "tickets" 라는 이름의 캐시에 대해 적용
            .withCacheConfiguration("EVENT", eventsCacheConfig) // "tickets" 라는 이름의 캐시에 대해 적용
            .build() // 최종적으로 빌드하여 반환. 캐싱을 관리하고 사용할 수 있도록 함.
    }
}