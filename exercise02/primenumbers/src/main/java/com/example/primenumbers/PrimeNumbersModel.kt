package com.example.primenumbers

import com.example.logger.Logger
import kotlin.math.sqrt

class PrimeNumbersModel {

    private fun isPrime(numberToCheck: Int): Boolean {
        if (numberToCheck < 2) return false

        val maxSqrtNumber = sqrt(numberToCheck.toDouble()).toInt()
        for (i in 2..maxSqrtNumber) {
            if (numberToCheck % i == 0) return false
        }
        return true
    }

    enum class Order {
        LOWER, HIGHER
    }


    fun analyzeNumbers(number: Int, order: Order): String  {
        Logger.i(message = "checkPrimeNumbers called (btn pushed)")
        Logger.i(message = "Grouping order: $order")
        Logger.i(message = "Numbers: $number")

        val numberStr = number.toString()
        val numbersList = mutableListOf<Int>()

        when(order){
            Order.LOWER -> {
                for (i in numberStr.indices) {
                    numbersList.add(numberStr.takeLast(i + 1).toInt())
                }
            }
            Order.HIGHER ->
                for (i in numberStr.indices) {
                    numbersList.add(numberStr.take(i + 1).toInt())
                }
        }
        return checkPrime(numbersList)
    }


    private fun checkPrime(numbersList:List<Int>):String{
        val resultWithoutN = numbersList.map { it -> if (isPrime(it)) "$it - prime" else it.toString() }
        val result = resultWithoutN.joinToString("\n")
        Logger.i(message = "Prime numbers check complete")
        return result
    }

}