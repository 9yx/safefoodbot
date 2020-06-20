package com.kkaminsky.vktelegrambot.service

import org.springframework.stereotype.Service

@Service
class DistanceCounterImpl : DistanceCounter {

    override fun getDistanceBeetwenTwoPoints(lat1: Float, lat2: Float, lon1: Float, lon2:Float, el1: Float, el2: Float): Float {
        val R = 6371 // Radius of the earth
        val latDistance = Math.toRadians((lat2 - lat1).toDouble())
        val lonDistance = Math.toRadians((lon2 - lon1).toDouble())
        val a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + (Math.cos(Math.toRadians(lat1.toDouble())) * Math.cos(Math.toRadians(lat2.toDouble()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var distance = R * c * 1000 // convert to meters
        val height = el1 - el2
        distance = Math.pow(distance, 2.0) + Math.pow(height.toDouble(), 2.0)
        return Math.sqrt(distance).toFloat()
    }
}