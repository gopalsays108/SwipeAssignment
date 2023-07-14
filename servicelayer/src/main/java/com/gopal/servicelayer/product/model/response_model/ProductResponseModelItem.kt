package com.gopal.servicelayer.product.model.response_model

import com.google.gson.annotations.SerializedName

data class ProductResponseModelItem(
    val image: String,
    val price: Double,

    @SerializedName("product_name")
    val productName: String,

    @SerializedName("product_type")
    val productType: String,
    val tax: Double
){

    override fun toString(): String {
        return "Product name is: $productName, type is $productType"
    }
}