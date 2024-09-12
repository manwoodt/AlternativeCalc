package com.course.calc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import kotlin.math.*

class CalcViewModel : ViewModel() {
    private val _results = MutableLiveData<String>()
    val results: LiveData<String> get() = _results
    private var job: Job? = null

    fun calculateFactorial(number: BigInteger) {
        Logger.d(message = "calculateFactorial start")
        job = CoroutineScope(Dispatchers.Default).launch {
            Logger.d(message = "$this start")
            val result = factorial(number)
            Logger.d(message = "$this end")
            withContext(Dispatchers.Main) {
                Logger.d(message = "Factorial of result $number is $result")
                _results.value = "Factorial of result $number is $result"
            }
        }
    }

    private fun factorial(n: BigInteger): BigInteger {
        var result = BigInteger.ONE
        var i = BigInteger.ONE
        while (i <= n) {
            result *= i
            i++
        }
        return result
    }

    fun calculateSquareAndCubeRoot(number: BigInteger) {
        Logger.d(message = "calculateFactorial start")
        job = CoroutineScope(Dispatchers.Default).launch {
            Logger.d(message = "$this start")
            val squareRoot = sqrt(number.toDouble())
            val cubeRoot = cbrt(number.toDouble())
            Logger.d(message = "$this end")
            withContext(Dispatchers.Main) {
                Logger.d(message = "Square root: $squareRoot, Cube root: $cubeRoot")
                _results.value = "Square root: $squareRoot, Cube root: $cubeRoot"
            }
        }
    }

    fun calculateLogarithms(number: BigInteger) {
        Logger.d(message = "calculateLogarithms start")
        job = CoroutineScope(Dispatchers.Default).launch {
            Logger.d(message = "$this start")
            val logBase10 = log10(number.toDouble())
            val naturalLog = ln(number.toDouble())
            Logger.d(message = "$this end")
            withContext(Dispatchers.Main) {
                Logger.d(message = "Log10: $logBase10, Ln: $naturalLog")
                _results.value = "Log10: $logBase10, Ln: $naturalLog"
            }
        }
    }


    fun calculateSquareAndCube(number: BigInteger) {
        Logger.d(message = "calculateSquareAndCube start")
        job = CoroutineScope(Dispatchers.Default).launch {
            Logger.d(message = "$this start")
            val square = number.multiply(number) // Используем BigInteger для умножения
            val cube = square.multiply(number) // cube = square * number
            Logger.d(message = "$this end")
            withContext(Dispatchers.Main) {
                Logger.d(message = "Square: $square, Cube: $cube")
                _results.value = "Square: $square, Cube: $cube"
            }
        }
    }

    fun checkIsPrime(number: BigInteger) {
        Logger.d(message = "checkIsPrime start")
        job = CoroutineScope(Dispatchers.Default).launch {
            Logger.d(message = "$this start")
            val isPrime = isPrime(number)
            Logger.d(message = "$this end")
            withContext(Dispatchers.Main) {
                Logger.d(message ="Number $number is $isPrime")
                _results.value = "Number $number is $isPrime"
            }
        }
    }

    private fun isPrime(n: BigInteger): Boolean {
        val two = BigInteger.valueOf(2)
        val three = BigInteger.valueOf(3)
        if (n < two) return false
        if (n == two) return true
        if (n % two == BigInteger.ZERO) return false

        val limit = n.shiftRight(1).add(BigInteger.ONE) // n/2 + 1
        var i = three
        while (i < limit) {
            if (n % i == BigInteger.ZERO) return false
            i = i.add(two)
        }
        return true
    }

}

