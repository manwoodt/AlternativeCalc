package com.example.speechmodule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.logger.FragmentLogger
import com.example.speechmodule.databinding.FragmentSpeechModuleBinding
import com.example.logger.Logger
import com.example.logger.addLogging

class SpeechModuleFragment : FragmentLogger() {

    private var _binding: FragmentSpeechModuleBinding? = null
    private val binding get() =  _binding!!
    private val viewModel: SpeechModuleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Logger.i(message = "SpeechModuleFragment onCreateView")
        Logger.d(message = "ViewModel created")
        _binding = FragmentSpeechModuleBinding.inflate(inflater,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        Logger.d(message = "ViewModel and lifecycleOwner set to binding")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i(message = "SpeechModuleFragment onViewCreated")
        setupLogging()
    }

    private fun setupLogging() {
        Logger.i(message = "Setting up logging and binding for EditText fields")
        binding.editTextNumber.addLogging("inputNumber")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
