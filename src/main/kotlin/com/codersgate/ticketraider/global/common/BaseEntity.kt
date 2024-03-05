package com.codersgate.ticketraider.global.common

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    @UpdateTimestamp
    @Column(nullable = false)
    val updatedAt: LocalDateTime? = null

    @Column(nullable = false)
    var isDeleted: Boolean = false
}