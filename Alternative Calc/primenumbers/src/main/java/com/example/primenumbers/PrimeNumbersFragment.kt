package com.example.primenumbers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.primenumbers.databinding.FragmentPrimeNumbersBinding
import com.example.logger.Logger
import com.example.logger.FragmentLogger

class PrimeNumbersFragment : FragmentLogger() {

    private var _binding: FragmentPrimeNumbersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PrimeNumbersViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Logger.d(message = "PrimeNumbersFragment onCreateView")
        _binding = FragmentPrimeNumbersBinding.inflate(inflater, container, false)

        setupSpinner()
        Logger.i(message = "PrimeNumbersFragment view created")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i(message = "PrimeNumbersFragment onViewCreated")
        binding.buttonCheckPrime.setOnClickListener {
            val input = binding.editTextNumber.text.toString()
            val order = PrimeNumbersModel.Order.valueOf(binding.spinnerOrder.selectedItem.toString().uppercase())
            try {
                val number = input.toInt()
                viewModel.analyzeNumber(number, order)
            }
            catch (e:NumberFormatException){
                Toast.makeText(requireContext(),"Неверный ввод", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.result.observe(viewLifecycleOwner, Observer { result ->
            binding.textViewResult.text = result
        })
    }

    fun setupSpinner(){
        val items = listOf("Higher", "Lower")
        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,items)
        binding.spinnerOrder.adapter= adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
