package com.gopal.swipeassignment.product.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.gopal.servicelayer.product.model.response_model.ProductResponseModelItem
import com.gopal.swipeassignment.R
import com.gopal.swipeassignment.databinding.FragmentViewProductBinding
import com.gopal.swipeassignment.product.adapters.ProductAdapter
import com.gopal.swipeassignment.product.viewmodel.ProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


class ViewProductFragment : Fragment() {

    private lateinit var binding: FragmentViewProductBinding
    private val TAG = ViewProductFragment::class.java.name
    private val productViewModel by viewModel<ProductViewModel>()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var speechInputLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewProductBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        attachObervers()
        addTextWatcher()
        attClickListener()

        speechInputLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val speechResults =
                    result.data?.getStringArrayExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<Editable>
                binding.searchTxt.text = speechResults[0]
            }
        }

        addSpeechToText()
    }

    private fun attClickListener() {
        binding.addNewProduct.setOnClickListener{
            findNavController().navigate(R.id.action_viewProductFragment_to_addProductFragment)
        }
    }

    private fun addSpeechToText() {

        binding.microPhoneIcon.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to search")
            try {
                speechInputLauncher.launch(intent)
            } catch (e: Exception) {
                Log.e(TAG, "addSpeechToText: $e")
            }
        }
    }

    private fun addTextWatcher() {
        binding.searchTxt.addTextChangedListener(searchTextWatcher)
    }

    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val searchString = s.toString()
            filterProductList(searchString)
        }
    }

    private fun filterProductList(searchString: String) {
        val tempList = productViewModel.productList.value
        val newFilteredList = mutableListOf<ProductResponseModelItem>()
        if (tempList != null) {
            for (product in tempList) {
                if (product.productName.lowercase().contains(searchString.lowercase())
                    || product.productType.lowercase().contains(searchString.lowercase())
                    || product.price.toString() == searchString
                ) {
                    newFilteredList.add(product)
                }
            }
            if (newFilteredList.isEmpty() && searchString.isEmpty())
                productAdapter.differ.submitList(
                    tempList.toList()
                )

            productAdapter.differ.submitList(newFilteredList.toList())
        }
    }

    private fun initRecyclerView() {
        productAdapter = ProductAdapter()
        binding.viewRecyclerViewAdapter.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun attachObervers() {
        productViewModel.loading.observe(viewLifecycleOwner) {
            handleProgressBar(it)
        }

        productViewModel.error.observe(viewLifecycleOwner) {
            handleError(it)
        }

        productViewModel.productList?.observe(viewLifecycleOwner) { data ->
            binding.noInternet.visibility = View.GONE
            productAdapter.differ.submitList(data.toList())
        }
    }

    private fun handleError(error: String?) {
        if (error.equals("No Internet")) {
            binding.noInternet.visibility = View.VISIBLE
        }
        Log.i(TAG, "handleError: $error")
    }

    private fun handleProgressBar(loading: Boolean) {
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}