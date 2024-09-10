package com.example.circles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CirclesViewModel : ViewModel() {

    init {
        Logger.i(message = "CirclesViewModel initialized")
    }

    private val circlesModel = CirclesModel()
    private val _result = MutableLiveData<String>()
    val result:LiveData<String> get()  = _result

    val x1 = MutableLiveData<String>()
    val y1 = MutableLiveData<String>()
    val r1 = MutableLiveData<String>()
    val x2 = MutableLiveData<String>()
    val y2 = MutableLiveData<String>()
    val r2 = MutableLiveData<String>()

    fun checkCircles()
    {
        viewModelScope.launch(Dispatchers.IO) {
            Logger.i(message = "$this started")

            val x1 = validateDoubleRad(x1.value)
            val y1 = validateDoubleRad(y1.value)
            val r1 = validateDoubleRad(r1.value, true)
            val x2 = validateDoubleRad(x2.value)
            val y2 = validateDoubleRad(y2.value)
            val r2 = validateDoubleRad(r2.value, true)

            if (x1 == null || y1 == null || r1 == null || x2 == null || y2 == null || r2 == null) {
                _result.value = "Couldn't parse a number. Please, try again"
                Logger.e(message = "Invalid input detected")
                return@launch
            }
            _result.value = circlesModel.checkIntersection(x1, y1, r1, x2, y2, r2)
            Logger.i(message = "$this ended")
        }
    }

    private fun validateDoubleRad(input: String?, isRad:Boolean=false): Double? {
        val value = input?.toDoubleOrNull()
        if (value == null || (isRad && value <= 0)) {
            Logger.e(message = "Invalid value: $input")
            return null
        }
        return value
    }

}