package com.example.primenumbers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrimeNumbersViewModel : ViewModel() {
    private val primeNumbersModel = PrimeNumbersModel()
    private val _result = MutableLiveData<String>()
    val result:LiveData<String> get() = _result

    fun analyzeNumber(number:Int, order: PrimeNumbersModel.Order){
    viewModelScope.launch(Dispatchers.IO) {
        Logger.i(message = "$this started")
        _result.postValue(primeNumbersModel.analyzeNumbers(number, order))
        Logger.i(message = "$this ended")
    }
    }


    init {
        Logger.i(message = "PrimeNumbersViewModel initialized")
    }





}


