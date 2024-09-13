package com.course.calc

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.course.calc.databinding.FragmentCalcBinding
import com.example.logger.FragmentLogger
import com.example.logger.Logger

class CalcFragment : FragmentLogger() {

    private val viewModel: CalcViewModel by viewModels()
    private var _binding: FragmentCalcBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Logger.d(message = "CalcFragment onCreateView")
        _binding = FragmentCalcBinding.inflate(inflater, container, false)
        Logger.i(message = "CalcFragment view created")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
        observeViewModel()

    }

    private fun observeViewModel(){
        viewModel.results.observe(viewLifecycleOwner, Observer { results ->
            hideProgressBar()
            binding.resultTextView.text = results
        })
        viewModel.buttonText.observe(viewLifecycleOwner, Observer{
            text ->
            binding.btnFactorial.text = text
            binding.btnSquareCubeRoot.text = text
            binding.btnLogarithms.text = text
            binding.btnSquareCube.text = text
            binding.btnSimplicityTest.text = text
            binding.btnRunAll.text = text
        })
    }

    private fun setupButtons() {

        binding.btnFactorial.setOnClickListener {
            if (binding.inputNumber.text.toString().isNotEmpty()) {
                if (viewModel.buttonText.value == "Run") {
                    showProgressBar()
                    viewModel.startCalculation()
                    viewModel.calculateFactorial(binding.inputNumber.text.toString().toBigInteger())
                }
                else {
                    viewModel.cancelCalculation()
                    hideProgressBar()
                }
            }

        }

        binding.btnSquareCubeRoot.setOnClickListener {
            if (binding.inputNumber.text.toString().isNotEmpty()) {
                showProgressBar()
                viewModel.calculateSquareAndCubeRoot(
                    binding.inputNumber.text.toString().toBigInteger()
                )
            }
        }

        binding.btnLogarithms.setOnClickListener {
            if (binding.inputNumber.text.toString().isNotEmpty()) {
                showProgressBar()
                viewModel.calculateLogarithms(
                    binding.inputNumber.text.toString().toBigInteger()
                )
            }
        }

        binding.btnSquareCube.setOnClickListener {
            if (binding.inputNumber.text.toString().isNotEmpty()) {
                showProgressBar()
                viewModel.calculateSquareAndCube(
                    binding.inputNumber.text.toString().toBigInteger()
                )
            }
        }

        binding.btnSimplicityTest.setOnClickListener {
            if (binding.inputNumber.text.toString().isNotEmpty()) {
                showProgressBar()
                viewModel.checkIsPrime(
                    binding.inputNumber.text.toString().toBigInteger()
                )
            }
        }

        binding.btnRunAll.setOnClickListener {
            if (binding.inputNumber.text.toString().isNotEmpty()) {
                showProgressBar()
                viewModel.calculateAll(
                    binding.inputNumber.text.toString().toBigInteger()
                )
            }
        }

    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}