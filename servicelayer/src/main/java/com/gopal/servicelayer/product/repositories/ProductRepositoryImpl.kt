package com.gopal.servicelayer.product.repositories

import com.gopal.servicelayer.common.result
import com.gopal.servicelayer.product.api.ProductAPI

class ProductRepositoryImpl(
    private val productAPI: ProductAPI
) : ProductRepository {

    override fun getProduct() = result {
        productAPI.getProductList()
    }

    override fun addProduct() {
        TODO("Not yet implemented")
    }
}