package com.gopal.swipeassignment.product.viewmodel

import androidx.lifecycle.ViewModel
import com.gopal.servicelayer.product.repositories.ProductRepository

class ProductViewModel(
    repository: ProductRepository
) : ViewModel() {

    val productDataFlow = repository.getProduct()
}