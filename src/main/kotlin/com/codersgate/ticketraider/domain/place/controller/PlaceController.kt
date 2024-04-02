package com.codersgate.ticketraider.domain.place.controller

import com.codersgate.ticketraider.domain.event.dto.EventResponse
import com.codersgate.ticketraider.domain.place.dto.PlaceRequest
import com.codersgate.ticketraider.domain.place.dto.PlaceResponse
import com.codersgate.ticketraider.domain.place.service.PlaceService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/place")
class PlaceController(
    private val placeService: PlaceService
) {

    @Operation(summary = "장소 생성")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun createPlace(
        @Valid @RequestBody request: PlaceRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(placeService.createPlace(request))
    }

    @Operation(summary = "장소 수정")
    @PutMapping("/{placeId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun updatePlace(
        @PathVariable placeId: Long,
        @Valid @RequestBody request: PlaceRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(placeService.updatePlace(placeId, request))
    }

    @Operation(summary = "장소 삭제")
    @DeleteMapping("/{placeId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun deletePlace(
        @PathVariable placeId: Long
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(placeService.deletePlace(placeId))
    }

    @Operation(summary = "장소 목록 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun getPlaceList(): ResponseEntity<List<PlaceResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(placeService.getPlaceList())
    }

    @Operation(summary = "장소 조회")
    @GetMapping("/{placeId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun getPlace(
        @PathVariable placeId: Long
    ): ResponseEntity<PlaceResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(placeService.getPlace(placeId))
    }

//    @Operation(summary = "장소별 이벤트 조회")
//    @GetMapping("/getEventList/{placeId}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    fun getEventList(
//        @PathVariable placeId: Long
//    ): ResponseEntity<List<EventResponse>>? {
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .body(placeService.getEventList(placeId))
//    }


}