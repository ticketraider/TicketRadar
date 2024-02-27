package com.codersgate.ticketraider.domain.review.model

import com.codersgate.ticketraider.global.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.bytebuddy.agent.builder.AgentBuilder.Identified
import org.hibernate.annotations.GenericGenerator


@Entity
@Table(name = "reviews")
class Review (
    @Column(name = "title")
    val title : String,

    @Column(name = "content")
    val content : String,

    @Column(name = "rating")
    val rating : Int,

//    @ManyToOne
//    @Column(name = "user")
//    val user : User,
//
//    @ManyToOne
//    @Column(name = "event")
//    val event : Event,
    ) : BaseEntity()
    {
        @Id
        @GeneratedValue( strategy = GenerationType.IDENTITY)
        val id : Long? = null
}



