package com.example.circles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.circles.databinding.FragmentCirclesBinding
import com.example.logger.FragmentLogger
import com.example.logger.Logger
import com.example.logger.addLogging

class CirclesFragment : FragmentLogger() {

    private var _binding: FragmentCirclesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CirclesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Logger.i(message = "CirclesFragment onCreateView")

        _binding = FragmentCirclesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i(message = "CirclesFragment onViewCreated")
        setupLogging()
        binding.buttonCheckIntersection.setOnClickListener {
                try {
                    viewModel.x1.value = binding.editTextX1.text.toString()
                    viewModel.y1.value = binding.editTextY1.text.toString()
                    viewModel.r1.value = binding.editTextR1.text.toString()
                    viewModel.x2.value = binding.editTextX2.text.toString()
                    viewModel.y2.value = binding.editTextY2.text.toString()
                    viewModel.r2.value = binding.editTextR2.text.toString()
                    viewModel.checkCircles()
                } catch (e: NumberFormatException) {
                    Logger.e("Invalid input", e)
                }

        }

        viewModel.result.observe(viewLifecycleOwner, Observer { result ->
            binding.textViewResult.text = result
        })
    }


    private fun setupLogging() {
        Logger.i(message = "Setting up logging and binding for EditText fields")
        binding.editTextX1.addLogging("x1")
        binding.editTextY1.addLogging("y1")
        binding.editTextR1.addLogging("r1")
        binding.editTextX2.addLogging("x2")
        binding.editTextY2.addLogging("y2")
        binding.editTextR2.addLogging("r2")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
