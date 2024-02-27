package com.codersgate.ticketraider.domain.category.model


import com.codersgate.ticketraider.global.common.BaseTime
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLDelete(sql = "UPDATE category SET is_deleted = true WHERE id = ?") // DELETE 쿼리 날아올 시 대신 실행
@SQLRestriction("is_deleted = false")
@Table(name = "category")
class Category(

    @Column(name = "title")
    var title: String,

//    @OneToMany(
//        mappedBy = "category",
//        fetch = FetchType.LAZY,
//        cascade = [CascadeType.ALL],
//        orphanRemoval = true
//    )
//    var eventList: MutableList<Event> = mutableListOf(),

    ) : BaseTime() {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

}