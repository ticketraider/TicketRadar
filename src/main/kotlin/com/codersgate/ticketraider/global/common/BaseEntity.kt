package com.codersgate.ticketraider.global.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.ZoneId

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

    @UpdateTimestamp
    @Column(nullable = false)
    val updatedAt: LocalDateTime? = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

    @Column(nullable = false)
    var isDeleted: Boolean = false
}