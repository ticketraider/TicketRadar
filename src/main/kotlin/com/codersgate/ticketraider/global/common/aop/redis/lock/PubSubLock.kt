package com.codersgate.ticketraider.global.common.aop.redis.lock

@Target(AnnotationTarget.FUNCTION)  // 함수에만 적용되도록 함
@Retention(AnnotationRetention.RUNTIME) // 어노테이션이 런타임까지 유지되도록. Reflection 이용.
annotation class PubSubLock