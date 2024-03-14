package com.codersgate.ticketraider.domain.member.repository

import com.codersgate.ticketraider.domain.member.entity.ProviderMap
import org.springframework.data.repository.CrudRepository

interface ProviderMapRepository: CrudRepository<ProviderMap, Long>{
    fun findByMemberId(memberId: Long): ProviderMap
}