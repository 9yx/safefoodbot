package com.kkaminsky.demo.repository

import com.kkaminsky.demo.entity.DataEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Конечные точки для авторизации и получения новых постов
 */
interface DataRepository:JpaRepository<DataEntity,Long> {
}