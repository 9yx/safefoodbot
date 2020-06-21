package com.kkaminsky.demo.repository

import com.kkaminsky.demo.entity.DataEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DataRepository:JpaRepository<DataEntity,Long> {
}