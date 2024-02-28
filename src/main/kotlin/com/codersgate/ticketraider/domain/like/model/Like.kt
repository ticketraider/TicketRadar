package com.codersgate.ticketraider.domain.like.model

import jakarta.persistence.*

@Entity
class Like (


    @Column(name = "userId")
    val userId : Long,

    @Column(name = "eventId")
    val eventId : Long,
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    val user:User,
//
//    @ManyToOne
//    @JoinColumn(name = "event_id")
//    val event:Event,


    ){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long? = null
}