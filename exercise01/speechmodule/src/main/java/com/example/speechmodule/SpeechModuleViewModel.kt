package com.example.speechmodule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class SpeechModuleViewModel : ViewModel() {

    val inputNumber = MutableLiveData<String>()
    private val _outputText = MutableLiveData<String>()
    val outputText: MutableLiveData<String> get() = _outputText

    init {
        Logger.i(message = "SpeechModuleViewModel initialized")
    }

    fun onSubmitClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            Logger.i(message = "$this started")
            val numberString = inputNumber.value?.trim() ?: ""
            Logger.d(message = "Submit button clicked with input: $numberString")
            val number = numberString.toIntOrNull()

            if (number != null && number in -1_000_000_000..1_000_000_000) {
                val result = convertNumberToWords(number)
                _outputText.postValue(result)
                Logger.d(message = "Converted number to words: $result")
            } else {
                val errorMessage = if (number == null) {
                    "Incorrect format, try again"
                } else {
                    "The number is out of bounds, try again."
                }
                _outputText.postValue(errorMessage)
                Logger.w(message = "Invalid input: $errorMessage")
            }
            Logger.i(message = "$this eded")
        }
    }

    private fun convertNumberToWords(number: Int): String {
        if (number == 0) return "zero"
        val isNegative = number < 0
        val absNum = number.absoluteValue
        val words = StringBuilder()

        val units =
            arrayOf("", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        val teens = arrayOf(
            "ten",
            "eleven",
            "twelve",
            "thirteen",
            "fourteen",
            "fifteen",
            "sixteen",
            "seventeen",
            "eighteen",
            "nineteen"
        )
        val tens = arrayOf(
            "",
            "",
            "twenty",
            "thirty",
            "forty",
            "fifty",
            "sixty",
            "seventy",
            "eighty",
            "ninety"
        )
        val thousands = arrayOf("", "thousand", "million", "billion")

        fun chunkToWords(chunk: Int): String {
            val words = StringBuilder()
            if (chunk >= 100) {
                words.append("${units[chunk / 100]} hundred ")
            }
            val remainder = chunk % 100
            if (remainder >= 20) {
                words.append("${tens[remainder / 10]} ${units[remainder % 10]}".trim())
            } else if (remainder >= 10) {
                words.append(teens[remainder - 10])
            } else {
                words.append(units[remainder])
            }
            return words.toString().trim().replace(" ", "-")
        }

        var num = absNum
        var i = 0
        while (num > 0) {
            val chunk = num % 1000
            if (chunk > 0) {
                val chunkWords = chunkToWords(chunk)
                words.insert(0, "$chunkWords ${thousands[i]} ")
            }
            num /= 1000
            i++
        }

        val result = words.toString().trim().replace(" ", "-").replace("-$", "")

        return if (isNegative) "minus-$result" else result
    }
}
