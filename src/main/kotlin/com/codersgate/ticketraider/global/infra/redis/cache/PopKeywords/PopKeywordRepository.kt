package com.codersgate.ticketraider.global.infra.redis.cache.PopKeywords

import com.codersgate.ticketraider.global.infra.redis.cache.popularity.PopKeyword

interface PopKeywordRepository {
    fun findByKeyword(keyword: String): PopKeyword
}