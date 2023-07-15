package com.gopal.servicelayer.product.model.response_model

import okhttp3.MultipartBody

class AddProductWithImageRequestBody(
    var message: String,
    var product_id: String,
    var success: Boolean,
    var files: MultipartBody.Part?
) {
}