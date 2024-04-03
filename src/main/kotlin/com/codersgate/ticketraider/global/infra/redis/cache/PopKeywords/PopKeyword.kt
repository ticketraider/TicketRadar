package com.codersgate.ticketraider.global.infra.redis.cache.popularity

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLDelete(sql = "UPDATE popkeywords SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
@Table(name = "popkeywords")
class PopKeyword(
    @Column(name = "keyword", nullable = false)
    val keyword: String,

    @Column(name = "score", nullable = false)
    var score: Int,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}