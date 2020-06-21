package com.kkaminsky.vktelegrambot.entity

import javax.persistence.*



/**
 * Класс данных для постов, хранящихся в базе
 */
@Entity
@Table(name = "data")
class DataEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    var id:Int = 0

    @Column(name = "post_id")
    var postId: String? = null

    @Column(name="group_id")
    var groupId: String? = null

    @Column(name="lat")
    var lat: String? = null

    @Column(name="lng")
    var lng: String? = null

    @Column(name="location")
    var location: String? = null

    @Column(name = "text")
    var text: String? = null

    @Column(name = "bread")
    var bread: Float? = null

    @Column(name = "potato")
    var potato: Float? = null

    @Column(name = "tea")
    var tea: Float? = null

    @Column(name="img_url")
    var imageUrl: String? = null

}