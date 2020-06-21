package com.kkaminsky.demo.controller

import com.kkaminsky.demo.dto.AddressDto
import com.kkaminsky.demo.dto.AddressResponseDto
import com.kkaminsky.demo.dto.OauthDto
import com.kkaminsky.demo.dto.SoftcossimDto
import com.kkaminsky.demo.entity.DataEntity
import com.kkaminsky.demo.repository.DataRepository
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.queries.users.UserField
import org.springframework.http.HttpEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import javax.transaction.Transactional

@RestController
class SimpleController(
        private val dataRepository: DataRepository
) {

    val vk = VkApiClient(HttpTransportClient.getInstance())

    var userActor: UserActor? = null

    @PostMapping("/oauth")
    fun oauth(@RequestBody dto: OauthDto):String{

        val authResponse  = vk.oauth().userAuthorizationCodeFlow(6996710,"M7UghDqOMNYSE9uIoVEO","http://localhost:8080/redirect",dto.code).execute()



        userActor = UserActor(authResponse.userId,authResponse.accessToken)

        val userInfo = vk.users().get(userActor)
                .fields(mutableListOf(UserField.PHOTO_200))
                .execute()

        val a = 0;

        return ""
    }


    @GetMapping("/analyze")
    @Transactional
    fun analyze():String {
        val posts = mutableListOf<Any>()
        for (x in 0..3) {
            val partOfPost = vk.wall().get(userActor).ownerId(-109125816).offset(100*x).count(100).execute()
            val entities = partOfPost.items.map {wallPost ->
                val clearText = wallPost.text?.replace("\n"," ")?.trim()
                println(clearText)
                if (clearText==""){
                    return@map DataEntity()
                }
                val dataEntity = DataEntity()
                val restTemplate = RestTemplate()
                val listOfCategosries = listOf("картошка","чай","хлеб").map {categ->
                    val body = HttpEntity(SoftcossimDto(
                            doc1 = wallPost.text,
                            doc2 = categ
                    ))
                    val response = restTemplate.postForEntity("http://192.168.1.89:5000/softcossim",body,Float::class.java)
                    response.body
                }

                dataEntity.potato = listOfCategosries[0]
                dataEntity.tea = listOfCategosries[1]
                dataEntity.bread = listOfCategosries[2]

                if (wallPost.attachments?.firstOrNull()?.photo?.lat==null){
                    val body = HttpEntity(AddressDto(
                                text = wallPost.text
                    ))
                    val response = restTemplate.postForEntity("http://192.168.1.89:5000/address",body,AddressResponseDto::class.java)
                    dataEntity.location = response.body?.name
                    try {
                        dataEntity.lng = response.body?.coor!!.split(" ")[0]
                        dataEntity.lat = response.body?.coor!!.split(" ")[1]

                    }
                    catch (e:Exception){
                        dataEntity.lng = null
                        dataEntity.lat = null
                    }


                } else {
                    dataEntity.lat = wallPost.attachments?.firstOrNull()?.photo?.lat.toString()
                    dataEntity.lng = wallPost.attachments?.firstOrNull()?.photo?.lng.toString()
                }

                dataEntity.groupId = wallPost.ownerId.toString()
                dataEntity.postId = wallPost.id.toString()
                dataEntity.text = clearText
                dataEntity.imageUrl = wallPost.attachments?.firstOrNull()?.photo?.photo2560
                dataEntity
            }
            dataRepository.saveAll(entities)
            Thread.sleep(400)
            posts.addAll(listOf(partOfPost))
        }
        return ""
    }


}