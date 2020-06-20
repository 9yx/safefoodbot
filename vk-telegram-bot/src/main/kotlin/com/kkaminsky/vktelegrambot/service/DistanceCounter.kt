package com.kkaminsky.vktelegrambot.service

interface DistanceCounter {

    fun getDistanceBeetwenTwoPoints(lat1: Float, lat2: Float, lon1: Float,
                                    lon2: Float, el1:Float, el2: Float):Float
}