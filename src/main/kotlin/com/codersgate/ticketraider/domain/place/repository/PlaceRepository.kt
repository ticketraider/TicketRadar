package com.codersgate.ticketraider.domain.place.repository

import com.codersgate.ticketraider.domain.place.model.Place
import org.springframework.data.jpa.repository.JpaRepository

interface PlaceRepository : JpaRepository<Place, Long> {
    fun findPlaceByName(name: String): Place?
}