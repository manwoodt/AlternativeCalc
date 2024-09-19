package com.example.thermometer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.thermometer.databinding.FragmentThermometerBinding
import com.example.logger.Logger
import com.example.logger.addLogging
import com.example.logger.FragmentLogger

class ThermometerFragment : FragmentLogger() {

    private lateinit var binding: FragmentThermometerBinding
    private val viewModel: ThermometerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Logger.i(message = "ThermometerFragment created")
        binding = FragmentThermometerBinding.inflate(inflater, container, false)
        setupSpinners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i(message = "ThermometerFragment onViewCreated")
        setupLogging()
        binding.buttonCheckTemperature.setOnClickListener {
            viewModel.inputTemperature.value = binding.editTextTemperature.text.toString()
            viewModel.checkTemperature()
        }

        viewModel.result.observe(viewLifecycleOwner, Observer { result ->
            binding.textViewResult.text = result
        })
    }

    private fun setupLogging() {
        Logger.i(message = "Setting up logging and binding for EditText fields")
        binding.editTextTemperature.addLogging("temperatureInput")
    }

    private fun setupSpinners() {
        Logger.i(message = "Setting up spinners in ThermometerFragment")

        val unitAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Celsius", "Kelvin", "Fahrenheit")
        )
        binding.spinnerUnit.adapter = unitAdapter
        binding.spinnerUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long,
            ) {
                val unit = when (position) {
                    0 -> "C"
                    1 -> "K"
                    2 -> "F"
                    else -> "C"
                }
                Logger.i(message = "Temperature unit selected: $unit")
                viewModel.temperatureUnit.value = unit
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Logger.i(message = "No temperature unit selected")
            }
        }

        val seasonAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Summer", "Winter")
        )
        binding.spinnerSeason.adapter = seasonAdapter
        binding.spinnerSeason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long,
            ) {
                val season = when (position) {
                    0 -> "s"
                    1 -> "w"
                    else -> "s"
                }
                Logger.i(message = "Season selected: $season")
                viewModel.season.value = season
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Logger.i(message = "No season selected")
            }
        }
    }
}
