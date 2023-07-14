package com.gopal.servicelayer.product.repositories

import com.gopal.servicelayer.common.BaseApiResponse
import com.gopal.servicelayer.product.api.ProductAPI
import com.gopal.servicelayer.product.model.response_model.ProductResponseModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository{
    fun getProduct(): Flow<BaseApiResponse<ProductResponseModel?>>
    fun addProduct()
}