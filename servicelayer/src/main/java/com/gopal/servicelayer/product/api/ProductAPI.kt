package com.gopal.servicelayer.product.api

import com.gopal.servicelayer.product.model.response_model.AddProductRequestBody
import com.gopal.servicelayer.product.model.response_model.ProductResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProductAPI {

    @GET("/api/public/get")
    suspend fun getProductList(): Response<ProductResponseModel>

    @POST("/api/public/add")
    suspend fun addProduct(
        @Body body: RequestBody
    ): Response<AddProductRequestBody>

    @Multipart
    @POST("/api/public/add")
    suspend fun addProductWithImage(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<AddProductRequestBody>
}