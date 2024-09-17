package com.course.calc

import android.media.VolumeShaper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.logger.Logger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import kotlin.math.*

class CalcViewModel : ViewModel() {
    private val _results = MutableLiveData<String>()
    val results: LiveData<String> get() = _results

    private val _buttonText = MutableLiveData<Map<Operations, String>>()
    val buttonText: LiveData<Map<Operations, String>> get() = _buttonText

    private val jobs = mutableMapOf<Operations, Job?>()

    init {
        _buttonText.value = Operations.entries.associateWith { "RUN" }
    }

    private fun launchJob(operation: Operations, doingJob: suspend () -> Unit) {
        jobs[operation]?.cancel() // Отмена предыдущей операции, если она есть
        _buttonText.value =
            _buttonText.value?.mapValues { if (it.key == operation) "CANCEL" else it.value }
        // обновление _buttonText через создание новый map, mutableMap не подходит,
        // потому что изменения внутри нее не будут отправляться наблюдателям
        val job = CoroutineScope(Dispatchers.Default).launch {
            try {
                Logger.d(message = "$this start")
                doingJob()
                Logger.d(message = "$this end")
            } catch (e: CancellationException) {
                Logger.e(message = "$operation cancelled")
            } catch (e: Exception) {
                Logger.e(message = "Error in $operation: ${e.message}")
                withContext(Dispatchers.Main) {
                    _results.value = "Error: ${e.message}"
                }
            } finally {
                Logger.e(message = "Text = RUN")
                withContext(Dispatchers.Main) {
                    _buttonText.value =
                        _buttonText.value?.mapValues { if (it.key == operation) "RUN" else it.value }
                }
            }
        }
        jobs[operation] = job
    }

    // Calc operations
    fun calculateFactorial(number: BigInteger) {
        Logger.d(message = "calculateFactorial start")
        launchJob(Operations.FACTORIAL) {
            val result = factorial(number)
            withContext(Dispatchers.Main) {
                Logger.d(message = "Factorial of result $number is $result")
                _results.postValue("Factorial of result $number is $result")
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
        Logger.d(message = "calculateSquareAndCubeRoot start")
        launchJob(Operations.SQUARE_CUBE_ROOT) {
            val squareRoot = sqrt(number.toDouble())
            val cubeRoot = cbrt(number.toDouble())

            withContext(Dispatchers.Main) {
                Logger.d(message = "Square root: $squareRoot, Cube root: $cubeRoot")
                _results.value = "Square root: $squareRoot, Cube root: $cubeRoot"
            }
        }
    }

    fun calculateLogarithms(number: BigInteger) {
        Logger.d(message = "calculateLogarithms start")
        launchJob(Operations.LOGARITHMS) {
            val logBase10 = log10(number.toDouble())
            val naturalLog = ln(number.toDouble())
            withContext(Dispatchers.Main) {
                Logger.d(message = "Log10: $logBase10, Ln: $naturalLog")
                _results.value = "Log10: $logBase10, Ln: $naturalLog"
            }
        }
    }

    fun calculateSquareAndCube(number: BigInteger) {
        Logger.d(message = "calculateSquareAndCube start")
        launchJob(Operations.SQUARE_CUBE) {
            val square = number.multiply(number) // Используем BigInteger для умножения
            val cube = square.multiply(number) // cube = square * number

            withContext(Dispatchers.Main) {
                Logger.d(message = "Square: $square, Cube: $cube")
                _results.value = "Square: $square, Cube: $cube"
            }
        }
    }

    fun checkIsPrime(number: BigInteger) {
        Logger.d(message = "checkIsPrime start")
        launchJob(Operations.SIMPLICITY_TEST) {
            val isPrime = isPrime(number)

            withContext(Dispatchers.Main) {
                Logger.d(message = "Number $number is $isPrime")
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

//    fun calculateAll(number: BigInteger) {
//        launchJob(Operations.RUN_ALL) {
//            val factorialDeferred = async { factorial(number) }
//            val squareRootDeferred = async { sqrt(number.toDouble()) }
//            val cubeRootDeferred = async { cbrt(number.toDouble()) }
//            val logBase10Deferred = async { log10(number.toDouble()) }
//            val naturalLogDeferred = async { ln(number.toDouble()) }
//            val squareDeferred = async { number.multiply(number) }
//            val cubeDeferred = async { squareDeferred.await().multiply(number) }
//
//            val results = """
//                Factorial: ${factorialDeferred.await()}
//                Square root: ${squareRootDeferred.await()}
//                Cube root: ${cubeRootDeferred.await()}
//                Log10: ${logBase10Deferred.await()}
//                Ln: ${naturalLogDeferred.await()}
//                Square: ${squareDeferred.await()}
//                Cube: ${cubeDeferred.await()}
//            """.trimIndent()
//
//            withContext(Dispatchers.Main) {
//                _results.postValue(results)
//            }
//        }
//    }

    // extra func

    fun cancelCalculation(operation: Operations) {
        Logger.d(message = "cancel $operation job")
        jobs[operation]?.cancel()
        jobs[operation] = null
        _buttonText.value =
            _buttonText.value?.mapValues {
                if (it.key == operation) "RUN" else it.value
            }
    }


    override fun onCleared() {
        super.onCleared()
        jobs.values.forEach { it?.cancel() }
    }

}

