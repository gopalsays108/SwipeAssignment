package com.gopal.swipeassignment.product.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.gopal.servicelayer.common.BaseApiResponse
import com.gopal.swipeassignment.R
import com.gopal.swipeassignment.databinding.FragmentAddProductBinding
import com.gopal.swipeassignment.product.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.log

class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private val TAG = AddProductFragment::class.java.name
    private val productViewModel by viewModel<ProductViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentAddProductBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}