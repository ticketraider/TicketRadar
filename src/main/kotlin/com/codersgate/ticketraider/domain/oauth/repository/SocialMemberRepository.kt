package com.codersgate.ticketraider.domain.oauth.repository

import com.codersgate.ticketraider.domain.oauth.entity.OAuth2Provider
import com.codersgate.ticketraider.domain.oauth.entity.SocialMember
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SocialMemberRepository: CrudRepository<SocialMember, Long> {
    fun existsByProviderAndProviderId(provider: OAuth2Provider, id: String): Boolean
    fun findByProviderAndProviderId(provider: OAuth2Provider, id: String): SocialMember
}