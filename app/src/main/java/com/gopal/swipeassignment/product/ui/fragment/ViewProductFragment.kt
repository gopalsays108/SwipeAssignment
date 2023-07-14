package com.gopal.swipeassignment.product.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.Visibility
import com.gopal.servicelayer.common.BaseApiResponse
import com.gopal.servicelayer.product.model.response_model.ProductResponseModel
import com.gopal.servicelayer.product.model.response_model.ProductResponseModelItem
import com.gopal.swipeassignment.R
import com.gopal.swipeassignment.databinding.FragmentAddProductBinding
import com.gopal.swipeassignment.databinding.FragmentViewProductBinding
import com.gopal.swipeassignment.product.adapters.ProductAdapter
import com.gopal.swipeassignment.product.viewmodel.ProductViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        if (error.equals("No Internet")){
            binding.noInternet.visibility = View.VISIBLE
        }
        Log.i(TAG, "handleError: $error")
    }

    private fun handleProgressBar(loading: Boolean) {
        if (loading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }
}