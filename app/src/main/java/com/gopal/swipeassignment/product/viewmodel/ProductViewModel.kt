package com.gopal.swipeassignment.product.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gopal.servicelayer.common.BaseApiResponse
import com.gopal.servicelayer.product.model.response_model.ProductResponseModelItem
import com.gopal.servicelayer.product.repositories.ProductRepository
import com.gopal.swipeassignment.utilities.Utility
import kotlinx.coroutines.launch

class ProductViewModel(
    repository: ProductRepository,
    val app: Context
) : ViewModel() {

    private val productDataFlow = repository.getProduct()
    var productList: MutableLiveData<ArrayList<ProductResponseModelItem>> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getProduct()
    }

    private fun getProduct() {
        viewModelScope.launch {
            if (Utility.hasInternetConnection(app)) {
                productDataFlow.collect {
                    when (it) {
                        is BaseApiResponse.Success -> {
                            val data: ArrayList<ProductResponseModelItem> = it.data!!
                            loading.postValue(false)
                            productList.postValue(data)
                        }

                        is BaseApiResponse.Failure -> {
                            loading.postValue(false)
                            error.postValue(it.msg)
                        }

                        is BaseApiResponse.Loading -> {
                            loading.postValue(true)
                        }
                    }
                }
            } else {
                error.postValue("No Internet")
            }
        }
    }

}