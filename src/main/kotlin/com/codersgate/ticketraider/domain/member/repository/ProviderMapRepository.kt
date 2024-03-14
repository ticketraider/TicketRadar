package com.codersgate.ticketraider.domain.member.repository

import com.codersgate.ticketraider.domain.member.entity.ProviderMap
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProviderMapRepository: JpaRepository<ProviderMap, Long>{
    fun findByMemberId(memberId: Long): ProviderMap
    fun deleteByMemberId(memberId: Long) : Any
    @Query(value = "select * from provider_map where member_id = :memberId limit 1", nativeQuery = true)
    fun restoreProviderMapByMemberId(@Param("memberId") memberId: Long): ProviderMap?

}