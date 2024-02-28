package com.codersgate.ticketraider.domain.like.controller

import com.codersgate.ticketraider.domain.like.service.LikeService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/likes")
class LikeController(
    private val likeService: LikeService
) {


    @Operation(summary = "좋아요 생성")
    @PostMapping()
    fun createLike(): ResponseEntity<Unit> {

        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @Operation(summary = "좋아요 취소")
    @DeleteMapping()
    fun deleteLike(): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.OK).build()
    }
}