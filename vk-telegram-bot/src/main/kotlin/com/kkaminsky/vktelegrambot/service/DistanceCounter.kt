package com.kkaminsky.vktelegrambot.service


/**
 * Базовый интерфейс для подсчета расстояния между двумя точками
 */
interface DistanceCounter {


    /**
     * Метод для подсчета расстояния между двумя точками, возвращает расстояние в метрах
     */
    fun getDistanceBeetwenTwoPoints(lat1: Float, lat2: Float, lon1: Float,
                                    lon2: Float, el1:Float, el2: Float):Float
}