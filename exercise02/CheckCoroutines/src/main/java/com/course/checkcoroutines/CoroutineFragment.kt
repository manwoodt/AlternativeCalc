package com.course.checkcoroutines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.course.checkcoroutines.databinding.FragmentCoroutineBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutineFragment : Fragment() {

    private var _binding: FragmentCoroutineBinding? = null
    private val binding get() = _binding!!
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentCoroutineBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}