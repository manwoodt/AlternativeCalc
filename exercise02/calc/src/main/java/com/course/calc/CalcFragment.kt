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

    private fun observeViewModel() {
        viewModel.results.observe(viewLifecycleOwner, Observer { results ->
            hideProgressBar()
            binding.resultTextView.text = results
        })

        viewModel.buttonText.observe(viewLifecycleOwner, Observer { buttonTexts ->
            binding.btnFactorial.text = buttonTexts[Operations.FACTORIAL]
            binding.btnSquareCubeRoot.text = buttonTexts[Operations.SQUARE_CUBE_ROOT]
            binding.btnLogarithms.text = buttonTexts[Operations.LOGARITHMS]
            binding.btnSquareCube.text = buttonTexts[Operations.SQUARE_CUBE]
            binding.btnSimplicityTest.text = buttonTexts[Operations.SIMPLICITY_TEST]
            binding.btnRunAll.text = buttonTexts[Operations.RUN_ALL]
        })
    }

    private fun setupButtons() {


        binding.btnFactorial.setOnClickListener { handleButtonClick(Operations.FACTORIAL) }
        binding.btnSquareCubeRoot.setOnClickListener { handleButtonClick(Operations.SQUARE_CUBE_ROOT) }
        binding.btnLogarithms.setOnClickListener { handleButtonClick(Operations.LOGARITHMS) }
        binding.btnSquareCube.setOnClickListener { handleButtonClick(Operations.SQUARE_CUBE) }
        binding.btnSimplicityTest.setOnClickListener { handleButtonClick(Operations.SIMPLICITY_TEST) }


//        binding.btnRunAll.setOnClickListener {
//            if (binding.inputNumber.text.toString().isNotEmpty()) {
//                showProgressBar()
//                viewModel.calculateAll(
//                    binding.inputNumber.text.toString().toBigInteger()
//                )
//            }
//        }

    }


    private fun handleButtonClick(operation: Operations) {
        if (binding.inputNumber.text.toString().isNotEmpty()) {
            showProgressBar()
            val number = binding.inputNumber.text.toString().toBigInteger()
            if (viewModel.buttonText.value?.get(operation) == "RUN") {
                when (operation) {
                    Operations.FACTORIAL -> viewModel.calculateFactorial(number)
                    Operations.SQUARE_CUBE_ROOT -> viewModel.calculateSquareAndCubeRoot(number)
                    Operations.LOGARITHMS -> viewModel.calculateLogarithms(number)
                    Operations.SQUARE_CUBE -> viewModel.calculateSquareAndCube(number)
                    Operations.SIMPLICITY_TEST -> viewModel.checkIsPrime(number)
                    Operations.RUN_ALL -> viewModel.checkIsPrime(number) // change
                }
            } else {
                viewModel.cancelCalculation(operation)
                hideProgressBar()
            }
        } else {
            hideProgressBar()
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