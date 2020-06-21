package com.kkaminsky.vktelegrambot.repository


import com.kkaminsky.vktelegrambot.entity.DataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Интерфейс для работы с базаой данных, где хранятся посты
 */
interface DataRepository:JpaRepository<DataEntity,Long> {

    @Query("""
        select * from data where text is not null and lat is not null and lng is not null order by bread desc limit 25;
    """,nativeQuery = true)
    fun getTopBreadsPost():List<DataEntity>

    @Query("""
        select * from data where text is not null and lat is not null and lng is not null order by potato desc limit 25;
    """,nativeQuery = true)
    fun getTopPotatosPost():List<DataEntity>

    @Query("""
        select * from data where text is not null and lat is not null and lng is not null order by tea desc limit 25;
    """,nativeQuery = true)
    fun getTopByTea(): List<DataEntity>
}