package com.example.circles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import com.example.logger.Logger

class CirclesModel {


    init {
        Logger.i(message = "CirclesViewModel initialized")
    }


    fun checkIntersection(x1:Double, y1:Double, r1:Double, x2:Double, y2:Double, r2:Double):String {

        Logger.i(message = "Inputs - x1: $x1, y1: $y1, r1: $r1, x2: $x2, y2: $y2, r2: $r2")

        val distance = sqrt((x2 - x1).pow(2.0) + (y2 - y1).pow(2.0))
        Logger.i(message = "Calculated distance: $distance")

        return when {
            abs(distance - (r1 + r2)) < 1e-9 -> {
                Logger.i(message = "The circles coincide")
                "The circles coincide"
            }

            distance <= r1 + r2 -> {
                Logger.i(message = "The circles intersect")
                "The circles intersect"
            }

            abs(r1 - r2) >= distance -> {
                Logger.i(message = "One circle is inside the other")
                "One circle is inside the other"
            }

            else -> {
                Logger.i(message = "The circles do not intersect")
                "The circles do not intersect"
            }
        }
    }
}