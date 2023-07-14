package com.gopal.servicelayer.product.api

import com.gopal.servicelayer.product.model.response_model.ProductResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductAPI {

    @GET("/api/public/get")
    suspend fun getProductList(): Response<ProductResponseModel>

    @POST("/api/public/add")
    suspend fun addProduct()
}