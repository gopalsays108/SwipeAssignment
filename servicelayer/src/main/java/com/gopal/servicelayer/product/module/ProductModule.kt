package com.gopal.servicelayer.product.module

import com.gopal.servicelayer.BuildConfig
import com.gopal.servicelayer.product.api.ProductAPI
import com.gopal.servicelayer.product.repositories.ProductRepository
import com.gopal.servicelayer.product.repositories.ProductRepositoryImpl
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var ProductModule = module {

    // This helps us to create singelton object
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ProductAPI> {
        val retrofit: Retrofit = get()
        retrofit.create(ProductAPI::class.java)
    }

    single<ProductRepository> {
        ProductRepositoryImpl(get())
    }
}