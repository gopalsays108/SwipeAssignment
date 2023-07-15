package com.gopal.swipeassignment.product.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.gopal.servicelayer.product.model.response_model.ProductResponseModelItem
import com.gopal.swipeassignment.R
import com.gopal.swipeassignment.databinding.FragmentViewProductBinding
import com.gopal.swipeassignment.product.adapters.ProductAdapter
import com.gopal.swipeassignment.product.ui.MainActivity
import com.gopal.swipeassignment.product.viewmodel.ProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


class ViewProductFragment : Fragment() {

    private lateinit var binding: FragmentViewProductBinding
    private val TAG = ViewProductFragment::class.java.name
    private val productViewModel by viewModel<ProductViewModel>()
    private lateinit var productAdapter: ProductAdapter

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
    }

    private fun attClickListener() {
        binding.addNewProduct.setOnClickListener {
            (activity as? MainActivity)?.navigateToNavBarDestination(R.id.addProductFragment)

//            findNavController().navigate(
//                R.id.action_viewProductFragment_to_addProductFragment,
//                null,
//                NavOptions.Builder().setPopUpTo(R.id.viewProductFragment, true).build()
//            )
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
            if (isVisible) {
                if (newFilteredList.isEmpty() && searchString.isEmpty())
                    productAdapter.differ.submitList(
                        tempList.toList()
                    )

                productAdapter.differ.submitList(newFilteredList.toList())
            }
        }
    }

    private fun initRecyclerView() {
        productAdapter = ProductAdapter(requireContext())
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
            if (isVisible)
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