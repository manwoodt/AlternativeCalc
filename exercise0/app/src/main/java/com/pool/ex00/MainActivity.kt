package com.pool.ex00

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pool.ex00.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var job: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.buttonLoadData.setOnClickListener { tryLoadData() }
        binding.buttonFreeze.setOnClickListener { freeze() }
        binding.buttonCancelLoading.setOnClickListener { cancelJob() }
    }


    private fun tryLoadData() {
        job = CoroutineScope(Dispatchers.Main).launch {
            showLoadingUI()
            logMessage("manwoodt", "Loading data started")
            try {
                val data: String = loadData()
                binding.textViewData.text = data

            } catch (e: Exception) {
                logMessage("manwoodt", "Data loading is canceled : $e")
                binding.textViewData.text = "Data loading is canceled"
            }
            finally {
                hideLoadingUI()
            }

            logMessage("manwoodt", "Loading data finished")
        }

    }

    suspend fun loadData(): String {
        delay(4000)
        return "Data is loaded"
    }

    private fun showLoadingUI(){
        binding.textViewData.text = "Data will appear here"
        binding.progressBar.visibility = View.VISIBLE
        binding.buttonCancelLoading.visibility = View.VISIBLE
        binding.buttonLoadData.visibility = View.INVISIBLE
    }

    private fun hideLoadingUI(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.buttonCancelLoading.visibility = View.INVISIBLE
        binding.buttonLoadData.visibility = View.VISIBLE
    }

    private fun cancelJob() {
        job?.cancel()
    }

    private fun logMessage(tag: String, message: String) {
        println("$tag: $message") // Используем print для упрощения примера
    }

    private fun freeze() = runBlocking {
        delay(3000)
    }

}