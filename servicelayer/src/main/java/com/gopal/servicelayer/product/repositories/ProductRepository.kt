package com.gopal.servicelayer.product.repositories

import com.gopal.servicelayer.common.BaseApiResponse
import com.gopal.servicelayer.product.api.ProductAPI
import com.gopal.servicelayer.product.model.response_model.AddProductRequestBody
import com.gopal.servicelayer.product.model.response_model.ProductResponseModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import java.io.File

interface ProductRepository{
    fun getProduct(): Flow<BaseApiResponse<ProductResponseModel?>>
    fun addProduct(product_name: String, product_type: String, price: String,tax:String,image: File?): Flow<BaseApiResponse<AddProductRequestBody?>>
}