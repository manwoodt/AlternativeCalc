package com.course.calc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logger.Logger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.math.BigInteger
import kotlin.math.cbrt
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.sqrt

class CalcViewModel : ViewModel() {
    private val _results = MutableLiveData<String>()
    val results: LiveData<String> get() = _results

    private val _buttonText = MutableLiveData<Map<Operations, String>>()
    val buttonText: LiveData<Map<Operations, String>> get() = _buttonText

    private val jobs = mutableMapOf<Operations, Job?>()
    val errorMessage = MutableLiveData<String>()

    init {
        _buttonText.value = Operations.entries.associateWith { "RUN" }
    }

    private fun launchJob(operation: Operations, doingJob: suspend () -> Unit) {
        jobs[operation]?.cancel() // Отмена предыдущей операции, если она есть

        updateButtonState(operation, "CANCEL")
        // обновление _buttonText через создание новый map, mutableMap не подходит,
        // потому что изменения внутри нее не будут отправляться наблюдателям

        val job = viewModelScope.launch {
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
                Logger.d(message = "Text = RUN")
                withContext(Dispatchers.Main) {
                    updateButtonState(operation, "RUN")
                }
            }
        }

        jobs[operation] = job
    }

    // Calc operations
    fun calculateFactorial(number: BigInteger) {
        Logger.d(message = "calculateFactorial start")
        launchJob(Operations.FACTORIAL) {
            val result = withContext(Dispatchers.Default) { factorial(number) }
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
            val square = number.multiply(number)
            val cube = square.multiply(number)

            withContext(Dispatchers.Main) {
                Logger.d(message = "Square: $square, Cube: $cube")
                _results.value = "Square: $square, Cube: $cube"
            }
        }
    }

    fun checkIsPrime(number: BigInteger) {
        Logger.d(message = "checkIsPrime start")
        launchJob(Operations.SIMPLICITY_TEST) {
            try {
                withTimeout(1000) {
                    val isPrime = isPrime(number)

                    withContext(Dispatchers.Main) {
                        Logger.d(message = "Number $number is prime: $isPrime")
                        _results.value = "Number $number is prime:$isPrime"
                    }
                }
            } catch (e: TimeoutCancellationException) {
                Logger.d(message = "Primality test timed out")
                errorMessage.postValue("An error has occurred. Please try again.")
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


    fun calculateAll(number: BigInteger) {
        launchJob(Operations.RUN_ALL) {
            try {
                val results = withContext(Dispatchers.Default) {
                    val factorial = factorial(number)
                    val squareRoot = sqrt(number.toDouble())
                    val cubeRoot = cbrt(number.toDouble())
                    val logBase10 = log10(number.toDouble())
                    val naturalLog = ln(number.toDouble())
                    val square = number.multiply(number)
                    val cube = square.multiply(number)
                    val isPrime = isPrime(number)

                    """
                    Factorial: $factorial
                    Square root: $squareRoot
                    Cube root: $cubeRoot
                    Log10: $logBase10
                    Ln: $naturalLog
                    Square: $square
                    Cube: $cube
                    Is prime: $isPrime
                """.trimIndent()
                }
                withContext(Dispatchers.Main) {
                    _results.value = results
                }
            } catch (e: CancellationException) {
                Logger.i(message = "Calculations were canceled: ${e.message}")
                withContext(Dispatchers.Main) {
                    _results.value = "Calculation canceled"
                }
            }
        }
    }


    // extra func

    fun cancelCalculation(operation: Operations) {
        Logger.d(message = "cancel $operation job")
        jobs[operation]?.cancel()
        jobs[operation] = null
        updateButtonState(operation, "RUN")
    }

    private fun updateButtonState(operation: Operations, newState: String) {
        _buttonText.value = _buttonText.value?.mapValues {
            if (it.key == operation) newState else it.value
        }
    }


    override fun onCleared() {
        super.onCleared()
        jobs.values.forEach { it?.cancel() }
    }

}

