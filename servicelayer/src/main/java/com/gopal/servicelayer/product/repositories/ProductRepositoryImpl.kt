package com.gopal.servicelayer.product.repositories

import com.gopal.servicelayer.common.BaseApiResponse
import com.gopal.servicelayer.common.result
import com.gopal.servicelayer.product.api.ProductAPI
import com.gopal.servicelayer.product.model.response_model.AddProductRequestBody
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProductRepositoryImpl(
    private val productAPI: ProductAPI
) : ProductRepository {

    override fun getProduct() = result {
        productAPI.getProductList()
    }

    override fun addProduct(
        product_name: String,
        product_type: String,
        price: String,
        tax: String,
        image: File?
    ): Flow<BaseApiResponse<AddProductRequestBody?>> {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("product_name", product_name)
            .addFormDataPart("product_type", product_type)
            .addFormDataPart("price", price)
            .addFormDataPart("tax", tax);

        return if (image != null) {
            val productNameRequestBody =
                product_name.toRequestBody("text/plain".toMediaTypeOrNull())
            val productTypeRequestBody =
                product_type.toRequestBody("text/plain".toMediaTypeOrNull())
            val priceRequestBody = price.toRequestBody("text/plain".toMediaTypeOrNull())
            val taxRequestBody = tax.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestImage = image.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("files[]", image.name, requestImage)
            result {
                productAPI.addProductWithImage(
                    productNameRequestBody,
                    productTypeRequestBody,
                    priceRequestBody,
                    taxRequestBody,
                    imagePart
                )
            }
        } else {
            result {
                productAPI.addProduct(requestBody.build())
            }
        }
    }
}